package edolce.parkourplugin;

import com.sk89q.squirrelid.Profile;
import org.bukkit.entity.Player;


//Class that contains all parkour attempt's info

public class ParkourAttempt {

    private final Player player;
    private final long milliTime;

    public ParkourAttempt(Player player, long milliTime) {
        this.player = player;
        this.milliTime = milliTime;
    }

    public String getPlayerName() {
        return player.getDisplayName();
    }


    //return time in mm M - ss.MMM S Format
    public Object getTime() {
        String s = "";
        s+=milliTime/1000/60;
        s+="M - ";
        s+=milliTime/1000%60;
        s+=".";
        s+=milliTime%1000;
        s+="S";
        return s;
    }

    public Player getPlayer() {
        return player;
    }

    public long getMilliTime() {
        return milliTime;
    }
}
