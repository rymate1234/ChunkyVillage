package org.getchunky.chunkyvillage.tasks;

import org.bukkit.block.Block;

public abstract class BlockTask implements Runnable{

    protected Block block;

    public BlockTask(Block block) {
        this.block = block;
    }
}
