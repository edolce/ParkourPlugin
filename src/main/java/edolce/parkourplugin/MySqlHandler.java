package edolce.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySqlHandler {

    private final static String URL = "jdbc:mysql://localhost:3306/testing";
    private final static String USER = "root";
    private final static String PASSWORD = "";

    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createConnection(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ResultSet getData(String select) {
        try {
            if (connection.isClosed()) createConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(select);
        }catch (SQLException ex) {
            Logger.getLogger(MySqlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static void pushAttempt(ParkourAttempt attempt) {
        try {
            if (connection.isClosed()) createConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `testing`.`parkourattempt` (`player_uuid`, `time`) VALUES (?, ?);");
            ps.setString(1, attempt.getPlayer().getUniqueId().toString());
            ps.setLong(2, attempt.getMilliTime());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ParkourAttempt getPlayerBest(Player player) {
        ResultSet rs=getData(String.format("SELECT * FROM parkourattempt WHERE player_uuid='%s' ORDER BY time ASC",player.getUniqueId()));
        try {
            if (connection.isClosed()) createConnection();
            if(!rs.next()) return null;
            return new ParkourAttempt(player,rs.getLong("time"));
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    public static List<ParkourAttempt> getTop5DESC() {

        List<ParkourAttempt> attempts= new ArrayList<>();
        ResultSet rs=getData("SELECT * FROM parkourAttempt ORDER BY time ASC LIMIT 5");
        try {
            if (connection.isClosed()) createConnection();
            while(rs.next()){
                System.out.println("GET ONE");
                attempts.add(new ParkourAttempt(Bukkit.getPlayer(UUID.fromString(rs.getString("player_uuid"))),rs.getLong("time")));
            }
            return attempts;
        } catch (SQLException ex) {
            Logger.getLogger(MySqlHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }



    }
}
