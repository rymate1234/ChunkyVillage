package org.getchunky.chunkyvillage.wartools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        if(location.distance(player.getLocation()) < 5) return;
        location.getWorld().createExplosion(location, 4, false);
        Entity fakeEntity = location.getWorld().dropItem(location, new ItemStack(1));
        for(Entity e : fakeEntity.getNearbyEntities(5,5,5)) {
            if(e instanceof LivingEntity) {
                ((LivingEntity)e).damage(100, player);}}
        fakeEntity.remove();
        Strings.CANNON.good(new ChunkyResident(player));
    }

    private static Location findLocation(Player player) {
        Location location = player.getLocation().clone().add(0, 1, 0);
        BlockIterator blockIterator = new BlockIterator(player.getWorld(), location.toVector(), player.getLocation().getDirection(), 0, 200);
        int safe=0;
        while (blockIterator.hasNext()) {
            Block b = blockIterator.next();
            safe++;
            if(safe<5) continue;
            if(b.getType() == Material.AIR || b.getType() == Material.WATER) continue;
            return b.getLocation();}
        return null;
    }

}
