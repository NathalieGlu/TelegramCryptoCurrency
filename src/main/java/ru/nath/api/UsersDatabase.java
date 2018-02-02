package ru.nath.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.postgresql.util.PGobject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.nath.map.UserRowMapper;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class UsersDatabase {

    private static final String driverClassName = "org.postgresql.Driver";
    private static final String url = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String dbUsername = "nathalie";
    private static final String dbPassword = "123";

    private static final Logger log = Logger.getLogger(UsersDatabase.class.getName());

    private JdbcTemplate jdbc;

    public UsersDatabase() {
        DataSource dataSource = getDataSource();
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(dataSource);

        jdbc = new JdbcTemplate(dataSource);
    }

    public void addUser(String user) {
        try {
            if (!userExists(user)) {
                jdbc.update("INSERT INTO user_base (chatid) VALUES(?)", user);
                log.info("Inserted user to database");
            }
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

    private boolean userExists(String user) {
        try {
            String sql = jdbc.queryForObject("SELECT chatid FROM user_base WHERE chatid = ?", String.class, user);
            return true;
        } catch (EmptyResultDataAccessException emptyResult) {
            log.info(emptyResult.toString());
            return false;
        }
    }

    private PGobject getSettings(String user) {
        return jdbc.queryForObject("SELECT settings FROM user_base WHERE chatid = ?",
                new Object[]{user}, new UserRowMapper());
    }

    public String addToSettings(String user, String coin, Double count) {
        try {
            PGobject jsonb = jdbc.queryForObject("SELECT settings FROM user_base WHERE chatid = ?",
                    new Object[]{user}, new UserRowMapper());

            if (jsonb.getValue() == null) {
                PGobject jsonbObj = new PGobject();
                jsonbObj.setType("json");
                jsonbObj.setValue(String.format(Locale.US, "{\"%s\" : \"%f\"}", coin, count));
                jdbc.update("UPDATE user_base SET settings = ? WHERE chatid = ?",
                        new Object[]{jsonbObj, user}, new int[]{Types.OTHER, Types.OTHER});

            } else {
                PGobject jsonbObj = getSettings(user);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonbObj.getValue());

                if (jsonNode.has(coin)) {
                    Double amountOfCoin = jsonNode.get(coin).asDouble() + count;

                    ((ObjectNode) jsonNode).remove(coin);
                    ((ObjectNode) jsonNode).put(coin, amountOfCoin.toString());

                    jsonbObj.setValue(objectMapper.writeValueAsString(jsonNode));
                } else {
                    ((ObjectNode) jsonNode).put(coin, count.toString());
                    jsonbObj.setValue(objectMapper.writeValueAsString(jsonNode));
                }

                jdbc.update("UPDATE user_base SET settings = ? WHERE chatid = ?",
                        new Object[]{jsonbObj, user}, new int[]{Types.OTHER, Types.OTHER});

            }

            log.info(String.format("Coin %s added to user wallet %s", coin, user));
            return "Coin added to wallet";
        } catch (EmptyResultDataAccessException emptyResult) {
            log.info(emptyResult.toString());
            return "Failed. Type \'/start to open wallet\'";
        } catch (Exception otherException) {
            log.info(otherException.toString());
            return "Failed";
        }
    }

    public String calculateWallet(CoinMarket coinMarket, String user) {
        try {
            Double summaryUSD = 0.0;
            Double summaryRUB = 0.0;
            String result = "";

            PGobject jsonb = jdbc.queryForObject("SELECT settings FROM user_base WHERE chatid = ?",
                    new Object[]{user}, new UserRowMapper());

            if (jsonb.getValue() == null) {
                return "Wallet is empty";
            } else {
                PGobject jsonbObj = new PGobject();
                jsonbObj = getSettings(user);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonbObj.getValue());

                Iterator<JsonNode> elementNames = jsonNode.elements();

                Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
                while (fieldsIterator.hasNext()) {
                    Map.Entry<String, JsonNode> field = fieldsIterator.next();

                    Double usd = Double.valueOf(coinMarket.getPrice_usd(field.getKey())) *
                            field.getValue().asDouble();
                    summaryUSD += usd;

                    Double rub = Double.valueOf(coinMarket.getPrice_rub(field.getKey())) *
                            field.getValue().asDouble();
                    summaryRUB += rub;

                    result += coinMarket.getName(field.getKey()) + "." +
                            field.getValue().asText() + " " + field.getKey() +
                            String.format(": %.2f USD, %.2f RUB\n", usd, rub);
                }
                result += String.format("Total: %.2f USD, %.2f RUB", summaryUSD, summaryRUB);

            }

            log.info("Calculated wallet");
            return result;
        } catch (Exception e) {
            log.info(e.toString());
            return "Wallet is empty";
        }
    }

    public String deleteUser(String user) {
        if (userExists(user)) {
            jdbc.execute("DELETE FROM user_base WHERE " +
                    "chatid = \'" + user + "\'");

            log.info(String.format("Deleted user %s", user));
            return "User deleted";
        }

        log.info(String.format("Requested user %s doesn's exist", user));
        return "User doesn't exist";
    }

    private static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

}