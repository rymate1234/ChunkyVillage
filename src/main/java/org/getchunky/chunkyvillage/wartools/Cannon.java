package org.getchunky.chunkyvillage.wartools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;


public class Cannon {

    public static void fire(Player player) {
        int amount = player.getItemInHand().getAmount();
        if(amount < 4) return;
        if(amount == 4) player.setItemInHand(null);
        else player.getItemInHand().setAmount(amount - 4);

        Location location = findLocation(player);
        location.getWorld().createExplosion(location, 4, false);
        Strings.CANNON.good(new ChunkyResident(player));
    }

    private static Location findLocation(Player player) {
        Location location = player.getLocation().clone().add(0, 1, 0);
        BlockIterator blockIterator = new BlockIterator(player.getWorld(), location.toVector(), player.getLocation().getDirection(), 0, 200);
        blockIterator.next();
        while (blockIterator.hasNext()) {
            Block b = blockIterator.next();
            if(b.getType() != Material.AIR) return b.getLocation();}
        return null;
    }

}
