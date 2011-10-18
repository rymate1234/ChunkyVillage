package org.getchunky.chunkyvillage.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Explode extends BlockTask {

    public Explode(Block block) {
        super(block);
    }

    public void run() {
        block.setType(Material.AIR);
        block.getWorld().createExplosion(block.getLocation(), 4F);
    }
}
