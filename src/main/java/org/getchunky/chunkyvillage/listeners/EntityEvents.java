package org.getchunky.chunkyvillage.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class EntityEvents extends EntityListener {
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event instanceof EntityDamageByEntityEvent)) return;
        EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)event;
        if(!(subEvent.getEntity() instanceof Player) || !(subEvent.getDamager() instanceof Player)) return;
        Player a = (Player)subEvent.getEntity();
        Player b = (Player)subEvent.getDamager();
        ChunkyPlayer ca = ChunkyManager.getChunkyPlayer(a.getName());
        ChunkyPlayer cb = ChunkyManager.getChunkyPlayer(b.getName());
        if(ChunkyTownManager.getStance(ca,cb) == ChunkyTown.Stance.ALLY) event.setCancelled(true);
    }
}
