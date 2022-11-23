package edolce.parkourplugin;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParkourScoreboardUtil {

    /*  I know this method might seem too complicated for what it should do,
     *  but I already had this preset ready, so I modified it a bit and used that.
     */
    public static Scoreboard getScoreboard(Player player) {
        //Get MySqlData playerBest
        ParkourAttempt playerBest= MySqlHandler.getPlayerBest(player);

        //Get MySqlData top5
        List<ParkourAttempt> top5AttemptsDESC = MySqlHandler.getTop5DESC();



        Scoreboard board =  Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("dummy", "title","random");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("Parkour");

        HashMap<String,String> infoXtitles = new HashMap<>();

        String bestAttemptTime = playerBest==null ? "N.D.": (String) playerBest.getTime();

        List<String> data = Lists.newArrayList(
                "Leaderboard:",
                "",
                String.format("Best Attempt: %s",bestAttemptTime),
                "",
                "Parkour"
        );


        for(int i=0;i<5;i++){
            if(i>=top5AttemptsDESC.size()){
                data.add(0,String.format("  #%s - %s - %s",i+1,"N.D.","N.D."));
            }else {
                System.out.println(i);
                data.add(0,String.format("  #%s - %s - %s",i+1,top5AttemptsDESC.get(i).getPlayerName(),top5AttemptsDESC.get(i).getTime()));
            }
        }

        int i=0;
        for (String s : data) {
            infoXtitles.put(String.valueOf(i),s);
            i++;
        }



        i=0;
        for (Map.Entry<String, String> set : infoXtitles.entrySet()) {
            Team line = board.registerNewTeam(set.getKey());
            line.addEntry(String.format("§%s",i));
            line.setPrefix(set.getValue());
            obj.getScore(String.format("§%s",i)).setScore(i++);
        }



        return board;
    }
}
