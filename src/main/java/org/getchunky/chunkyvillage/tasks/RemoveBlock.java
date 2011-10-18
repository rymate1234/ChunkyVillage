package org.getchunky.chunkyvillage.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RemoveBlock extends BlockTask{

    public RemoveBlock(Block block) {
        super(block);
    }

    public void run() {
        block.setType(Material.AIR);
    }
}
