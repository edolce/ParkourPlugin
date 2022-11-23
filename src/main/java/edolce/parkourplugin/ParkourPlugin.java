package edolce.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ParkourPlugin extends JavaPlugin {


    private static ParkourPlugin plugin;

    //List of players in parkour AREA
    private List<Player> playersInParkourGame = new ArrayList<>();


    @Override
    public void onEnable() {
        plugin=this;
        //Add Listener To Check If A Player Enter The Parkour Area [More Info In Class]
        Bukkit.getPluginManager().registerEvents(new WorldGuardListener(),this);
    }

    @Override
    public void onDisable() {
    }


    //GETTER/SETTERS
    public static ParkourPlugin getInstance() {
        return plugin;
    }
    public List<Player> getPlayersInParkourGame() {
        return playersInParkourGame;
    }
    public void addPlayerFromGame(Player player){
        playersInParkourGame.add(player);
    }
    public void removePlayerFromGame(Player player){
        playersInParkourGame.remove(player);
    }
}
