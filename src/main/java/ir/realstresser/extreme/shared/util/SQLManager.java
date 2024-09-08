package ir.realstresser.extreme.shared.util;

import ir.realstresser.extreme.velocity.VelocityMain;
import lombok.Cleanup;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter public class SQLManager {
    private Connection connection;

    public SQLManager(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + VelocityMain.getInstance().getFileManager().getSQL_DB());
        } catch(Exception e){
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
    }

    public void init() {
        VelocityMain.getInstance().getLogger().info("Initializing SQLManager...");
        try {
            if(this.connection == null) {
                VelocityMain.getInstance().getLogger().warn("We don't know why but connection seems to be null.");
                VelocityMain.getInstance().getLogger().warn("trying to set it manually.....");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + VelocityMain.getInstance().getFileManager().getSQL_DB());
            }
            @Cleanup final Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Players (" +
                    "ip TEXT NOT NULL, " +
                    "uuid TEXT NOT NULL, " +
                    "username TEXT PRIMARY KEY, " +
                    "isWhiteListed BOOLEAN DEFAULT 0, " +
                    "violationCount INTEGER DEFAULT 0, " +
                    "lastBlacklist TEXT NOT NULL" +
                    ");");
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
        VelocityMain.getInstance().getLogger().info("Successfully initialized!");
    }

    public void addPlayer(final String ip, final String uuid, final String username, final boolean isWhiteListed, final int violationCount, final String lastDC) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Players (ip, uuid, username, isWhiteListed, violationCount, lastBlacklist) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (username) DO NOTHING;");
            preparedStatement.setString(1, ip);
            preparedStatement.setString(2, uuid);
            preparedStatement.setString(3, username);
            preparedStatement.setBoolean(4, isWhiteListed);
            preparedStatement.setInt(5, violationCount);
            preparedStatement.setString(6, lastDC);
            preparedStatement.executeUpdate();
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
    }

    public void updatePlayerByUsername(final String username, final String valueName, final Object value) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Players SET " + valueName + " = ? WHERE username = ?;");
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
    }

    public void updatePlayerByIP(final String ip, final String valueName, final Object value) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Players SET " + valueName + " = ? WHERE ip = ?;");
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, ip);
            preparedStatement.executeUpdate();
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
    }

    public void updatePlayerByUUID(final String uuid, final String valueName, final Object value) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Players SET " + valueName + " = ? WHERE uuid = ?;");
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
    }

    public Object getPlayerDataByUUID(final String uuid, final String fieldName) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + fieldName + " FROM Players WHERE uuid = ?");
            preparedStatement.setString(1, uuid);
            @Cleanup final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getObject(fieldName) : null;
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
        return null;
    }

    public Object getPlayerDataByIP(final String ip, final String fieldName) {
        try {
            @Cleanup final PreparedStatement preparedStatement = connection.prepareStatement("SELECT " + fieldName + " FROM Players WHERE ip = ?");
            preparedStatement.setString(1, ip);
            @Cleanup final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getObject(fieldName) : null;
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
        return null;
    }
    public List<Map<String, Object>> getAllPlayers() {
        final List<Map<String, Object>> players = new ArrayList<>();
        try {
            @Cleanup final Statement statement = connection.createStatement();
            @Cleanup final ResultSet resultSet = statement.executeQuery("SELECT * FROM Players");
            while (resultSet.next()) {
                Map<String, Object> playerData = new HashMap<>();
                playerData.put("ip", resultSet.getString("ip"));
                playerData.put("uuid", resultSet.getString("uuid"));
                playerData.put("username", resultSet.getString("username"));
                playerData.put("isWhiteListed", resultSet.getBoolean("isWhiteListed"));
                playerData.put("violationCount", resultSet.getInt("violationCount"));
                playerData.put("lastBlacklist", resultSet.getString("lastBlacklist"));
                players.add(playerData);
            }
        } catch (final Exception e) {
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
        return players;
    }
}
