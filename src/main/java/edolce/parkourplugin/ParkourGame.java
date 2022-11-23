package edolce.parkourplugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

//Class that manage the parkour game system

//IMPORTANT: checkpoints must be inside the parkour_game region

public class ParkourGame implements Listener {
    private final Player player;
    //Preset for the parkour (A preset store all chekpoints)
    private final ParkourPreset preset;
    //Index to track current checkpoint to reach
    private int checkpointCounter=0;
    //Start Millisecond time, start when checkpointCounter 1 is reached for the first time (player reach checkpoint 0)
    private long timeStartMilliSecond;

    public ParkourGame(Player player, ParkourPreset preset) {
        this.player = player;
        this.preset = preset;
        Bukkit.getPluginManager().registerEvents(this, ParkourPlugin.getInstance());
        addScoreBoard();
        player.sendMessage("CheckPoints in order:");
        preset.getAllCheckPoints().forEach( location -> player.sendMessage(String.valueOf(location)) );
    }

    public void destroyListener() {
        HandlerList.unregisterAll(this);
    }

    public void addScoreBoard() {
        player.setScoreboard(ParkourScoreboardUtil.getScoreboard(player));
    }

    private void removeScoreboard() {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    //Check if player exits the worldguard region AND if player hit a checkpoint
    @EventHandler
    public void playerLocationHandler(PlayerMoveEvent event) {
        if (event.getPlayer()!=player) return;
        //WORLDGUARD
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        String regionId = "parkour_region";
        ProtectedRegion region = regions.getRegion(regionId);
        Location destLoc = BukkitAdapter.adapt(event.getTo());
        if (!region.contains(destLoc.getBlockX(), destLoc.getBlockY(), destLoc.getBlockZ())) {
            endGame();
            return;
        }
        //CHECKPOINT

        //Get checkpoint number (if not in list checkpointnumber=-1)
        int checkpointNumber = preset.getAllCheckPoints().indexOf(event.getTo().toVector().toBlockVector());

        //If checkpoint is right one, update!
        if (checkpointNumber == checkpointCounter) {
            //If checkpoint is 0 START TIMER
            if (checkpointNumber == 0) timeStartMilliSecond = System.currentTimeMillis();
            checkpointCounter++;
            event.getPlayer().sendMessage("Hit!");
        }
        //If is final checkpoint end game and store data
        if (checkpointCounter == preset.getAllCheckPoints().size()) {
            event.getPlayer().sendMessage("Nice End!");
            storeData();
            endGame();
        }
    }

    private void storeData() {
        //CALC DELTA TIME
        long milliSec = System.currentTimeMillis() - timeStartMilliSecond;


        //PUSH TO MYSQL
        MySqlHandler.pushAttempt(new ParkourAttempt(player, milliSec));
    }

    private void endGame() {
        destroyListener();
        //Remove ScoreBoard
        removeScoreboard();
        //Remove from global list
        ParkourPlugin.getInstance().removePlayerFromGame(player);
    }


}
