package astaro.midmmo.core.handlers;

import astaro.midmmo.core.data.connectors.dbConnector;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

//Find user in site DB
public class UserFinder {

    private static final Object lockConn = new Object();
    private static final dbConnector dbc = new dbConnector();

    /**
     * Return username if he exists.
     * @param username
     * @return GameProfile or null.
     */
    @Nullable
    public static GameProfile findUser(String username) {
        synchronized (lockConn) {
            try (Connection conn = dbc.connect()) {
                String query = "SELECT username, accessToken, uuid FROM users WHERE username = ? ;";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, username);
                    try (ResultSet resultSet = stmt.executeQuery()) {
                        if (resultSet.next()) {
                            String resultUser = resultSet.getString("username");
                            String resultToken = resultSet.getString("accessToken");
                            String resultUuid = resultSet.getString("uuid");

                            UUID uuid = UUID.fromString(resultUuid);
                            GameProfile userProfile = new GameProfile(uuid, resultUser);
                            Logger.getLogger(UserFinder.class.getName()).log(Level.INFO, () -> "Found user: " + userProfile);
                            return userProfile;
                        } else {
                            return null;
                        }
                    }
                }
            } catch (SQLException e) {
                Logger.getLogger(UserFinder.class.getName()).log(Level.WARNING, e.toString());
                return null;
            }
        }
    }
}
