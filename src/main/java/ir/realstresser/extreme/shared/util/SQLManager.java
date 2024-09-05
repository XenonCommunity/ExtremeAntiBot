package ir.realstresser.extreme.shared.util;

import ir.realstresser.extreme.velocity.Main;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.sql.*;

@SuppressWarnings("unused")
@UtilityClass public class SQLManager {
    @Getter private Connection connection;
    public void init(){
        Main.getInstance().getLogger().info("Initializing SQLManager...");
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Main.getInstance().getFileManager().getSQL_DB());
            @Cleanup final Statement statement = getConnection().createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Players (" +
                    "ip TEXT NOT NULL, " +
                    "uuid TEXT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "isWhiteListed BOOLEAN DEFAULT 0, " +
                    "violationCount INTEGER DEFAULT 0" +
                    ");");
        } catch(final Exception e){
            Main.getInstance().getLogger().info(e.getMessage());
        }
        Main.getInstance().getLogger().info("Successfully initialized!");
    }
    public void addPlayer(final String ip, final String uuid, final String username, final boolean isWhiteListed, final int violationCount) {
        try{
            @Cleanup final PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO Players (ip, uuid, username, isWhiteListed, violationCount) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (uuid) DO NOTHING;");
            preparedStatement.setString(1, ip);
            preparedStatement.setString(2, uuid);
            preparedStatement.setString(3, username);
            preparedStatement.setBoolean(4, isWhiteListed);
            preparedStatement.setInt(5, violationCount);
            preparedStatement.executeUpdate();
        } catch(Exception e){
            Main.getInstance().getLogger().error(e.getMessage());
        }
    }
    public void updatePlayer(final String username, final String valueName, final Object value) {
        try{
            @Cleanup final PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE Players SET " + valueName + " = ? WHERE username = ?;");
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, username);
        } catch (final Exception e) {
            Main.getInstance().getLogger().error(e.getMessage());
        }
    }
    public Object getPlayerData(final String uuid, final String fieldName) {
        try{
            @Cleanup final PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT " + fieldName + " FROM Players WHERE uuid = ?");
            preparedStatement.setString(1, uuid);
            @Cleanup final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? resultSet.getObject(fieldName) : null;
        } catch(final Exception e){
            Main.getInstance().getLogger().error(e.getMessage());
        }
        return null;
    }
}
