package edolce.parkourplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkourConfig {

    public static ParkourPreset getDefaultPreset()  {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        String path =ParkourPlugin.getInstance().getDataFolder().getAbsolutePath();
        try(FileReader reader = new FileReader(path+ File.separator+"checkpoints.json")){
            //Read JSON file
            Object obj = jsonParser.parse(reader);


            JSONObject preset = (JSONObject) obj;
            JSONArray locationsJSON = (JSONArray) preset.get("checkpointsData");

            List<BlockVector> parsedLocations = new ArrayList<>();

            for(Object loc:locationsJSON){
                JSONObject locJSON = (JSONObject) loc;

                String worldName = (String) locJSON.get("worldName");

                long x = (long) locJSON.get("x");
                long y = (long) locJSON.get("y");
                long z = (long) locJSON.get("z");

                Location parsedLoc = new Location(Bukkit.getWorld(worldName),x,y,z);
                parsedLocations.add(parsedLoc.toVector().toBlockVector());
            }

            return new ParkourPreset(parsedLocations);
        }catch (IOException | ParseException exception){
            exception.printStackTrace();
            return null;
        }
    }
}
