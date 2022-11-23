package edolce.parkourplugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.util.gson.BlockVectorAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

import static edolce.parkourplugin.ParkourPlugin.*;

/* Listener that detects if a player enter the parkour area
** If the listener and the condition is triggered add the player to the global list and start the PARKOUR GAME
**
*/


public class WorldGuardListener implements Listener {

    //Get the WorldGuard "parkour_region" region
    private final World parkourWorld = Objects.requireNonNull(Bukkit.getWorld("world"));
    private final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    private final RegionManager regions = container.get(BukkitAdapter.adapt(parkourWorld));
    private final String regionId = "parkour_region";
    private final ProtectedRegion region = regions.getRegion(regionId);


    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {

        //new location of the player
        Location destLoc = event.getTo();
        if(destLoc==null) return;
        if(region==null) return;

        //Check if player is now inside the region and that it wasn't already.
        if (region.contains(destLoc.getBlockX(), destLoc.getBlockY(), destLoc.getBlockZ()) & !getInstance().getPlayersInParkourGame().contains(event.getPlayer())) {
            //Add player to the global list
            getInstance().addPlayerFromGame(event.getPlayer());
            //Start ParkourGame for player
            new ParkourGame(event.getPlayer(), ParkourConfig.getDefaultPreset());
        }

    }
}
