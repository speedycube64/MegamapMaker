package net.minecraft.src;
import java.io.*;
import java.util.*;

import net.minecraft.entity.player.EntityPlayer;

/*
Tool to dump basic-er minimap data to a file
so that it can be post-processed into a minimap

Author: speedycube64
*/
public class MegamapTracker
{
    private static int BUFFER_SIZE = 100;

    //these are the buffers
    private double[] xPositions;
    private double[] zPositions;
    private int[] dimensions;
    private float[] rotations;
    private long[] times;

    private int tag = 0;

    private PrintWriter printwriter;

    //temporary data set during loop
    private long time = 0;
    private double x, z = 0;
    private float yaw = 0;
    private int dim = 0;

    public MegamapTracker()
    {
        xPositions = new double[BUFFER_SIZE + 1];
        zPositions = new double[BUFFER_SIZE + 1];
        dimensions = new int[BUFFER_SIZE + 1];
        rotations = new float[BUFFER_SIZE + 1];
        times = new long[BUFFER_SIZE + 1];
    }

    public void fillUpBuffer(String saveFolder, long time, EntityPlayer player)
    {
        //save player data to the arrays
        xPositions[tag] = player.posX;
        zPositions[tag] = player.posZ;
        dimensions[tag] = player.dimension;
        rotations[tag] = player.rotationYaw % 360;
        times[tag] = time;

        tag++;

        //if the arrays are full, save the data and reset the buffers
        if(tag >= BUFFER_SIZE)
        {
            flushToDisk(saveFolder);
        }
    }

    public void flushToDisk(String saveFolder)
    {
        try 
            {
                //make/open the file in the current save folder
                String saveDirectory = "saves/" + saveFolder + "/";
                new File(saveDirectory).mkdirs();
                String filePath = saveDirectory + "map_log.txt";
                File file = new File(filePath);
                if(!file.exists()){
                    file.createNewFile();
                }
                //instantiate the filewriter in append mode to not overwrite previous buffers
                printwriter = new PrintWriter(new FileWriter(file, true));
                
                //print everything in the buffers to the file
                for(int i = 0; i < tag; i++)
                {
                    time = times[i];
                    x = xPositions[i];
                    z = zPositions[i];
                    dim = dimensions[i];
                    yaw = rotations[i];

                    printwriter.println(String.format("%d %.2f %.2f %.1f %d", time, x, z, yaw, dim));
                }

                //clear the buffers
                xPositions = new double[BUFFER_SIZE + 1];
                zPositions = new double[BUFFER_SIZE + 1];
                dimensions = new int[BUFFER_SIZE + 1];
                rotations = new float[BUFFER_SIZE + 1];
                times = new long[BUFFER_SIZE + 1];       
                tag = 0;         

                //close the file    
                printwriter.close();

                System.out.println(String.format("Wrote data in %s", saveFolder));
            } 
            catch (Exception e) 
            {
                
            }


    }

}

