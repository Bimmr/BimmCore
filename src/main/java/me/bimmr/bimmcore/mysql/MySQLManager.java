package me.bimmr.bimmcore.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.*;

/**
 * Created by Randy on 05/20/16.
 */
public class MySQLManager {

    private Plugin plugin;

    private HikariDataSource hikari;

    public MySQLManager(Plugin plugin, String host, String port, String database, String username, String password) {
        this.plugin = plugin;

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", host);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", database);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);
    }

    /**
     * Close the connection
     */
    public void unload() {
        hikari.close();
    }

    /**
     * Create the table if it doesn't exist
     *
     * @param table
     * @param columns
     * @throws SQLException
     */
    public void loadTable(String table, Column... columns) throws SQLException {
        String mySQLData = "";

        if (!columns[0].getName().toLowerCase().contains("uuid"))
            mySQLData += "UUID VARCHAR(40), ";

        for (int i = 0; i != columns.length; i++) {
            Column column = columns[i];
            mySQLData += column.toString() + (i + 1 != columns.length ? ", " : "");
        }
        //updateSQL("CREATE TABLE IF NOT EXISTS " + table + " (" + mySQLData + ")");
        updateSQL("CREATE TABLE IF NOT EXISTS " + table + " (" + mySQLData + ", PRIMARY KEY (UUID))");
        updateSQL("ALTER TABLE " + table + " ADD PRIMARY KEY (uuid)");
    }

    /**
     * Get the top UUID's with values from the table
     *
     * @param table
     * @param column
     * @param positions
     * @return
     */
    public TreeMap<UUID, Integer> getTop(String table, String column, int positions) {
        int playerCount = 0;
        Map<UUID, Integer> map = new HashMap<UUID, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<UUID, Integer> topPlayers = new TreeMap<UUID, Integer>(bvc);
        List<HashMap<String, Object>> result = querySQL("SELECT UUID, " + column + " FROM " + table + " ORDER BY " + column + " DESC");
        while (playerCount < result.size() && playerCount < positions) {
            HashMap<String, Object> set = result.get(playerCount);
            map.put(UUID.fromString((String) set.get("UUID")), (int) set.get(column));
            playerCount++;
        }
        topPlayers.putAll(map);
        return topPlayers;
    }

    /**
     * Get a value from the table
     *
     * @param table
     * @param column
     * @param uuid
     * @return
     */
    public Object get(String table, final String column, UUID uuid) {
        List<HashMap<String, Object>> result = querySQL("SELECT " + column + " FROM " + table + " WHERE UUID=?", uuid.toString());
        if (result.isEmpty())
            return 0;

        HashMap<String, Object> set = result.get(0);

        if (set.containsKey(column))
            return set.get(column);
        else
            return 0;
    }

    /**
     * Check if a UUID is in a table
     *
     * @param uuid
     * @return
     */
    public boolean isInTable(String table, UUID uuid) {
        List<?> result = querySQL("SELECT UUID FROM " + table + " WHERE UUID=?", uuid.toString());
        return !result.isEmpty();
    }

    /**
     * Put a value into the table using the uuid as a key
     *
     * @param table
     * @param column
     * @param uuid
     * @param value
     */
    public void set(String table, String column, UUID uuid, Object value) {
        try {
            updateSQL("INSERT INTO " + table + "(UUID, " + column + ") VALUES (?, ?) ON DUPLICATE KEY UPDATE " + column + "=?", uuid.toString(), value, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Alter the table to add a column
     *
     * @param table
     * @param column
     */
    public void addColumn(String table, Column column) {
        try {
            updateSQL("ALTER TABLE " + table + " ADD " + column.getName() + " " + column.getDataType().toString() + "(" + column.getLength() + ");");
        } catch (SQLException e) {
        }
    }

    /**
     * Handles executing either an update or a query statement
     *
     * @param update
     * @param query
     * @return
     */
    private List<HashMap<String, Object>> handleSQL(final boolean update, final String query, final Object... values) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<HashMap<String, Object>> result = null;

        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(query);

            for (int i = 1; i <= values.length; i++)
                statement.setObject(i, values[i - 1]);

            if (update)
                statement.executeUpdate();
            else {
                resultSet = statement.executeQuery();
                result = convertResultSetToList(resultSet);
            }
        } finally {

            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    if (!query.contains("ALTER TABLE"))
                        e.printStackTrace();
                }

            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    if (!query.contains("ALTER TABLE"))
                        e.printStackTrace();
                }
        }
        return result;
    }

    /**
     * Execute an update statement
     *
     * @param query
     */
    private void updateSQL(String query, Object... values) throws SQLException {
        handleSQL(true, query, values);
    }

    /**
     * Execute a query statement
     *
     * @param query
     * @return
     */
    private List<HashMap<String, Object>> querySQL(String query, Object... values) {
        try {
            return handleSQL(false, query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert a ResultSet to a List<Map> So you can use it after the ResultSet is closed
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private List<HashMap<String, Object>> convertResultSetToList(ResultSet resultSet) {

        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), resultSet.getObject(i) == null ? 0 : resultSet.getObject(i));
                }
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private class ValueComparator implements Comparator<UUID> {

        Map<UUID, Integer> base;

        public ValueComparator(Map<UUID, Integer> base) {
            this.base = base;
        }

        public int compare(UUID a, UUID b) {
            // sorting from high to low
            if (base.get(a) > base.get(b)) {
                return -1;
            }
            if (base.get(a) < base.get(b)) {
                return 1;
            }
            // entries with the same values are sorted alphabetically
            return a.toString().compareTo(b.toString());
        }
    }
}
