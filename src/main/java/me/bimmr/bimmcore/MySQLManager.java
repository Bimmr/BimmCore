package me.bimmr.bimmcore;


import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class MySQLManager {

    private final boolean DEBUG;
    private       MySQL   mysql;
    private       Plugin  plugin;

    /**
     * Create a mysql connection
     *
     * @param plugin
     * @param host
     * @param port
     * @param database
     * @param username
     * @param password
     */
    public MySQLManager(Plugin plugin, String host, String port, String database, String username, String password) {
        this.plugin = plugin;
        this.mysql = new MySQL(plugin, host, port, database, username, password);

        DEBUG = false;
    }

    /**
     * Closes connections
     */
    public void unload() {
        mysql.closeConnection();
    }

    public MySQL getMySQL() {
        return mysql;
    }

    /**
     * First columns is the player's UUID, so you don't need that For example:
     * <p/>
     * "Kills INT(10), Deaths INT(10), Points INT(10), Score INT(10), PlayingTime INT(15), HighestKillStreak INT(10)"
     * <p/>
     * ^ Would check if a table with all that exists, and if not it creates it
     *
     * @param tableName
     * @param columns
     * @throws SQLException
     */
    public void createTableIfDoesntExist(String tableName, Column... columns) throws SQLException {
        String mySQLData = "";
        for (int i = 0; i != columns.length; i++) {
            Column c = columns[i];
            mySQLData += c.toString() + (i + 1 != columns.length ? ", " : "");
        }
        mysql.updateSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (UUID VARCHAR(40), " + mySQLData + ");");
    }

    /**
     * Get the value of the column
     *
     * @param tableName  - The tables name
     * @param columnName - The stats name
     * @param uuid
     * @return the players stats
     * @throws SQLException
     */
    public Object get(String tableName, String columnName, UUID uuid) {
        Object obj = 0;
        if (DEBUG)
            System.out.println("Getting value from " + tableName + " - " + uuid + "- " + columnName);

        if (!mysql.hasOpenConnection())
            mysql.openConnection();

        Connection con = mysql.getConnection();

        ResultSet set = mysql.querySQL(con, "SELECT " + columnName + " FROM " + tableName + " WHERE UUID = '" + uuid.toString() + "';");
        if (set != null) {
            try {
                if (set.next())
                    obj = set.getObject(columnName);

            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error getting uuid " + uuid + " from column " + columnName + " from table " + tableName);
                e.printStackTrace();
            }

        }
        if (obj == null)
            obj = 0;

        mysql.closeConnection();
        return obj;

    }

    public TreeMap<UUID, Integer> getTop10(String tableName, String columnName) {
        Map<UUID, Integer> map = new HashMap<UUID, Integer>();
        ValueComparator bvc = new ValueComparator(map);
        TreeMap<UUID, Integer> players = new TreeMap<UUID, Integer>(bvc);

        if (!mysql.hasOpenConnection())
            mysql.openConnection();

        Connection con = mysql.getConnection();

        if (DEBUG)
            System.out.println("Getting top 10 uuids from column " + columnName);

        try {
            ResultSet set = this.mysql.querySQL(con, "SELECT * FROM `" + tableName + "` ORDER BY `" + columnName + "` DESC");

            int i = 0;
            while (set.next() || i < 10) {
                map.put(UUID.fromString(set.getString("UUID")), set.getInt(columnName));
                i++;
            }
            players.putAll(map);

        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error getting stats from table " + tableName);
            e.printStackTrace();
        }
        mysql.closeConnection();
        return players;
    }

    /**
     * Assumes all players are in columns 1
     *
     * @param tableName - The tables name
     * @return All the players in the table
     */
    public ArrayList<UUID> getAllPlayers(String tableName) {
        ArrayList<UUID> players = new ArrayList<UUID>();
        if (!mysql.hasOpenConnection())
            mysql.openConnection();

        Connection con = mysql.getConnection();
        if (DEBUG)
            System.out.println("Getting all UUIDs");

        try {

            ResultSet set = mysql.querySQL(con, "SELECT * FROM `" + tableName + "` ");

            while (set.next())
                players.add(UUID.fromString(set.getString("UUID")));

        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error getting uuids from table " + tableName);
            e.printStackTrace();
        }
        mysql.closeConnection();
        return players;
    }

    /**
     * Set values into the MySQL Database, this method should not be used as the
     * update method works for setting new and existing values
     *
     * @param tableName
     * @param columnName
     * @param value
     * @param uuid
     */
    private void set(String tableName, String columnName, Object value, UUID uuid) {
        if (DEBUG)
            System.out.println("Setting value into " + tableName + " - " + uuid + "- " + columnName + " - " + value);

        mysql.updateSQL("INSERT INTO " + tableName + " (`UUID`, `" + columnName + "`) VALUES ('" + uuid + "', '" + value + "');");

    }

    /**
     * Safely update/set the value Will set the value only if the table doesn't
     * have the player already, otherwise it'll just update the players values
     *
     * @param tableName
     * @param columnName
     * @param value
     * @param uuid
     */
    public void update(String tableName, String columnName, Object value, UUID uuid) {

        if (DEBUG)
            System.out.println("Updating value into " + tableName + " - " + uuid + "- " + columnName + " - " + value);

        if (getAllPlayers(tableName).contains(uuid))
            mysql.updateSQL("UPDATE " + tableName + " SET " + columnName + "='" + value + "' WHERE UUID ='" + uuid + "';");
        else set(tableName, columnName, value, uuid);

    }

    /**
     * Alter the table by adding another column to it
     *
     * @param table
     * @param column
     * @throws SQLException
     */
    public void alterTable(String table, Column column) throws SQLException {

        Connection c = null;
        PreparedStatement s = null;

        if (!mysql.hasOpenConnection())
            mysql.openConnection();

        c = mysql.getConnection();

        s = c.prepareStatement("ALTER TABLE " + table + " ADD " + column.getName() + " " + column.getDataType().toString() + "(" + column.getLength() + ");");
        s.executeUpdate();

        mysql.closeConnection();

    }

    public static enum DataType {
        INT,
        TINYINT,
        SMALLINT,
        MEDIUMINT,
        BIGINT,
        FLOAT,
        DOUBLE,
        DECIMAL,
        DATE,
        DATETYPE,
        TIMESTAMP,
        TIME,
        YEAR,
        CHAR,
        VARCHAR,
        TEXT,
        TINYTEXT,
        MEDIUMTEXT,
        LONGTEXT,
        ENUM;
    }

    public static class Column {

        private int      length;
        private DataType type;
        private String   name;

        public Column(String name, DataType type, int length) {
            this.name = name;
            this.type = type;
            this.length = length;
        }

        /**
         * @return the type
         */
        public DataType getDataType() {
            return type;
        }

        /**
         * @return the length
         */
        public int getLength() {
            return length;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + " " + type.toString() + "(" + length + ")";
        }
    }

    public class MySQL {

        private final String hostname, port, database, username, password;
        private Plugin     plugin;
        private Connection connection;

        public MySQL(Plugin plugin, String hostname, String port, String database, String username, String password) {
            this.plugin = plugin;
            this.hostname = hostname;
            this.port = port;
            this.database = database;
            this.username = username;
            this.password = password;
            this.connection = null;

        }

        /**
         * Checks if there is an open connection
         *
         * @return
         */
        public boolean hasOpenConnection() {
            try {
                return this.connection != null && !this.connection.isClosed();
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error checking the MySQL Connection!");
                e.printStackTrace();
                return false;
            }
        }

        /**
         * Opens a connection
         */
        public void openConnection() {
            try {
                if (!hasOpenConnection()) {
                    Class.forName("com.mysql.jdbc.Driver");
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.username, this.password);
                }
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Could not connect to MySQL server! because: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                this.plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found!");
            }
        }

        /**
         * Closes the connection
         */
        public void closeConnection() {
            if (hasOpenConnection())
                try {
                    this.connection.close();
                    this.connection = null;
                } catch (SQLException e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Error closing the MySQL Connection!");
                    e.printStackTrace();
                }
        }

        /**
         * Gets the connection
         *
         * @return
         */
        public Connection getConnection() {
            return this.connection;
        }

        /**
         * Querys the SQL
         *
         * @param query
         * @return
         */
        public ResultSet querySQL(Connection con, String query) {
            PreparedStatement state = null;
            ResultSet set = null;

            try {
                state = con.prepareStatement(query);
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error creating the query statement!");
                e.printStackTrace();
            }

            try {
                set = state.executeQuery();
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error getting the ResultSet from the query!");
                e.printStackTrace();
            }

            return set;
        }

        /**
         * Updates the SQL
         */
        public void updateSQL(String update) {

            Connection c = null;
            PreparedStatement s = null;

            if (!hasOpenConnection())
                openConnection();

            c = getConnection();

            try {
                s = c.prepareStatement(update);
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error creating the update statement!");
                e.printStackTrace();
            }
            try {
                s.executeUpdate();
            } catch (SQLException e) {
                this.plugin.getLogger().log(Level.SEVERE, "Error executing the update statement!");
                e.printStackTrace();
            }
            closeConnection();
        }
    }

    class ValueComparator implements Comparator<UUID> {

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