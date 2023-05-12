package me.bimmr.bimmcore.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.*;


/**
 * The type My sql manager.
 */
public class MySQLManager {

    private Plugin plugin;

    private boolean          debug;
    private HikariDataSource hikari;

    /**
     * Instantiates a new My sql manager.
     *
     * @param plugin   the plugin
     * @param host     the host
     * @param port     the port
     * @param database the database
     * @param username the username
     * @param password the password
     */
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
     * Sets debug.
     *
     * @param debug the debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Unload.
     */
    public void unload() {
        hikari.close();
    }

    /**
     * Load table.
     *
     * @param table   the table
     * @param columns the columns
     * @throws SQLException the sql exception
     */
    public void loadTable(String table, Column... columns) throws SQLException {
        if (debug)
            System.out.println("Loading Table: " + table + "; Columns:" + columns);

        String mySQLData = "";

        if (!columns[0].getName().toLowerCase().contains("uuid"))
            mySQLData += "UUID VARCHAR(40), ";

        for (int i = 0; i != columns.length; i++) {
            Column column = columns[i];
            mySQLData += column.toString() + (i + 1 != columns.length ? ", " : "");
        }
        //updateSQL("CREATE TABLE IF NOT EXISTS " + table + " (" + mySQLData + ")");
        updateSQL("CREATE TABLE IF NOT EXISTS " + table + " (" + mySQLData + ", PRIMARY KEY (UUID))");
        try {
            updateSQL("ALTER TABLE " + table + " ADD PRIMARY KEY (uuid)");
        } catch (SQLException e) {
        }
    }

    /**
     * Gets top.
     *
     * @param table     the table
     * @param column    the column
     * @param positions the positions
     * @return the top
     */
    public TreeMap<UUID, Integer> getTop(String table, String column, int positions) {
        if (debug)
            System.out.println("Using Table: " + table + "; Getting Top " + positions + " " + column);

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
     * Get object.
     *
     * @param table  the table
     * @param column the column
     * @param uuid   the uuid
     * @return the object
     */
    public Object get(String table, final String column, UUID uuid) {
        if (debug)
            System.out.println("Using Table: " + table + "; Getting  " + uuid + " - " + column);
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
     * Is in table boolean.
     *
     * @param table the table
     * @param uuid  the uuid
     * @return the boolean
     */
    public boolean isInTable(String table, UUID uuid) {
        if (debug)
            System.out.println("Using Table: " + table + "; Checking for  " + uuid);
        List<?> result = querySQL("SELECT UUID FROM " + table + " WHERE UUID=?", uuid.toString());
        return !result.isEmpty();
    }

    /**
     * Set.
     *
     * @param table  the table
     * @param column the column
     * @param uuid   the uuid
     * @param value  the value
     */
    public void set(String table, String column, UUID uuid, Object value) {
        if (debug)
            System.out.println("Using Table: " + table + "; Setting  " + uuid + " - " + column);
        try {
            updateSQL("INSERT INTO " + table + "(UUID, " + column + ") VALUES (?, ?) ON DUPLICATE KEY UPDATE " + column + "=?", uuid.toString(), value, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add columns.
     *
     * @param table the table
     * @param uuid  the uuid
     * @param set   the set
     */
    public void addColumns(String table, UUID uuid, Map<String, Object> set) {
        if (debug)
            System.out.println("Using Table: " + table + "; Adding  " + uuid + " - " + set);
        String columns = "UUID";
        String value = "?";
        Object[] values = new Object[set.size()];
        int i = 0;

        for (Map.Entry<String, Object> entry : set.entrySet()) {
            columns += ", " + entry.getKey();
            values[i] = entry.getValue();
            value += ", ?";
        }
        try {
            updateSQL("INSERT INTO " + table + "(" + columns + ") VALUES (" + value + ")", values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add column.
     *
     * @param table  the table
     * @param column the column
     */
    public void addColumn(String table, Column column) {
        if (debug)
            System.out.println("Using Table: " + table + "; Adding column" + column);
        try {
            updateSQL("ALTER TABLE " + table + " ADD " + column.getName() + " " + column.getDataType().toString() + "(" + column.getLength() + ");");
        } catch (SQLException e) {
        }
    }

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

    private void updateSQL(String query, Object... values) throws SQLException {
        handleSQL(true, query, values);
    }

    private List<HashMap<String, Object>> querySQL(String query, Object... values) {
        try {
            return handleSQL(false, query, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

        /**
         * The Base.
         */
        Map<UUID, Integer> base;

        /**
         * Instantiates a new Value comparator.
         *
         * @param base the base
         */
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
