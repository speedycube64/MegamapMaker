package speedy.megamaptracker;

import java.io.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
Tool to dump basic-er minimap data to a file
so that it can be post-processed into a minimap

Author: speedycube64 (adapted from jr5000)
*/

public class MegamapTracker
{
    public static MegamapTracker tracker;

    // how many lines to store at a time before flushing to file
    private static final int BUFFER_SIZE = 100;

    private static final Logger LOGGER = LogManager.getLogger("MegamapTracker");

    //these are the buffers
    private static double[] xPositions;
    private static double[] zPositions;
    private static int[] dimensions;
    private static float[] rotations;
    private static long[] times;

    private static int tag = 0;

    private static PrintWriter printWriter;
    private static String saveFolder;

    public MegamapTracker()
    {
        xPositions = new double[BUFFER_SIZE + 1];
        zPositions = new double[BUFFER_SIZE + 1];
        dimensions = new int[BUFFER_SIZE + 1];
        rotations = new float[BUFFER_SIZE + 1];
        times = new long[BUFFER_SIZE + 1];
    }

    public static void fillUpBuffer(PlayerEntity player)
    {
        //save player data to the arrays
        times[tag] = System.currentTimeMillis();
        xPositions[tag] = player.getX();
        zPositions[tag] = player.getZ();
        dimensions[tag] = tracker.legacyDimensionType(player.world.getDimension());
        rotations[tag] = player.yaw % 360;

        tag++;

        //if the arrays are full, save the data and reset the buffers
        if(tag >= BUFFER_SIZE)
        {
            flushToDisk();
        }
    }

    public static void flushToDisk()
    {
        try
        {

            String saveDirectory = saveFolder + "/";
            new File(saveDirectory).mkdirs();
            String filePath = saveDirectory + "map_log.txt";
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
            //instantiate the filewriter in append mode to not overwrite previous buffers
            printWriter = new PrintWriter(new FileWriter(file, true));

            //print everything in the buffers to the file
            for(int i = 0; i < tag; i++)
            {
                long time = times[i];
                double x = xPositions[i];
                double z = zPositions[i];
                int dim = dimensions[i];
                float yaw = rotations[i];

                printWriter.println(String.format("%d %.2f %.2f %.1f %d", time, x, z, yaw, dim));
            }

            //clear the buffers
            xPositions = new double[BUFFER_SIZE + 1];
            zPositions = new double[BUFFER_SIZE + 1];
            dimensions = new int[BUFFER_SIZE + 1];
            rotations = new float[BUFFER_SIZE + 1];
            times = new long[BUFFER_SIZE + 1];
            tag = 0;

            //close the file
            printWriter.close();

            LOGGER.info(String.format("Wrote data in %s", saveFolder));
        }
        catch (Exception e)
        {
            LOGGER.info(String.format("Could NOT write data in %s. Overwriting buffer", saveFolder));
            tag = 0;
        }

    }

    public static void setSaveFolder(String saveFolder) {
        MegamapTracker.saveFolder = saveFolder;
    }

    private int legacyDimensionType(DimensionType dim)
    {
        // idk shit about registries dog :sob:
        if (dim.hasCeiling()) return -1;
        else if (dim.hasEnderDragonFight()) return 1;
        else return 0;
    }
}
