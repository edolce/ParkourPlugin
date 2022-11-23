package edolce.parkourplugin;

import org.bukkit.Location;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkourPreset {

    private final BlockVector start;
    private final List<BlockVector> checkPoints;
    private final BlockVector finish;

    public ParkourPreset(BlockVector start, List<BlockVector> checkPoints, BlockVector finish) {
        this.start = start;
        this.checkPoints = checkPoints;
        this.finish = finish;
    }

    public ParkourPreset(List<BlockVector> allCheckPoints) {
        this.start = allCheckPoints.remove(0);
        this.finish = allCheckPoints.remove(allCheckPoints.size()-1);
        this.checkPoints = allCheckPoints;
    }

    public BlockVector getStart() {
        return start;
    }

    public BlockVector getFinish() {
        return finish;
    }

    public List<BlockVector> getCheckPoints() {
        return checkPoints;
    }

    public List<BlockVector> getAllCheckPoints() {
        List<BlockVector> temp = new ArrayList<>(checkPoints);
        temp.add(0,start);
        temp.add(finish);
        return temp;
    }
}
