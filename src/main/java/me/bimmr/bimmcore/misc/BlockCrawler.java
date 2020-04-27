package me.bimmr.bimmcore.misc;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * Collects all adjacent blocks of the same material
 */
public class BlockCrawler {

    public static final int[][] ADJ_LOC = new int[][]{new int[]{-1, 0, 0}, new int[]{1, 0, 0}, new int[]{0, -1, 0}, new int[]{0, 1, 0}, new int[]{0, 0, -1},
            new int[]{0, 0, 1}};
    private int   maxSize;
    private Block mOrigBlock;
    private ArrayList<Location> mProcessedBlocks = new ArrayList<Location>();

    public BlockCrawler(int maxSize) {
        this.maxSize = maxSize;
    }

    private void processAdjacent(Block block, Material type) {
        if ((block != null) && (block.getType() == type)) if (!this.mProcessedBlocks.contains(block.getLocation())) {
            this.mProcessedBlocks.add(block.getLocation());
            for (int[] element : BlockCrawler.ADJ_LOC) {
                Location nextLoc = block.getLocation();
                nextLoc.setX(block.getX() + element[0]);
                nextLoc.setY(block.getY() + element[1]);
                nextLoc.setZ(block.getZ() + element[2]);
                if (this.mProcessedBlocks.size() < this.maxSize) processAdjacent(nextLoc.getBlock(), block.getType());
            }
        }

    }

    /**
     * Will return a list of the location of all blocks that match the block's type and are adjacent to it
     *
     * @param origBlock
     * @return
     */
    public ArrayList<Location> start(Block origBlock) {
        this.mOrigBlock = origBlock;
        processAdjacent(this.mOrigBlock, this.mOrigBlock.getType());
        return this.mProcessedBlocks;
    }
}
