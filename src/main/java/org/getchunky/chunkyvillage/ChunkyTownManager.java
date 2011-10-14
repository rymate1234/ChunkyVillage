package org.getchunky.chunkyvillage;

import com.nijikokun.register.payment.Method;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.json.JSONException;

import java.util.HashMap;

public class ChunkyTownManager {

    public static ChunkyTown getTown(String name) {
        return (ChunkyTown)ChunkyManager.getObject(ChunkyTown.class.getName(),name);
    }

    public static ChunkyTown getTown(ChunkyPlayer chunkyPlayer) {
        if(chunkyPlayer.getOwner() != null && chunkyPlayer.getOwner() instanceof ChunkyTown) return (ChunkyTown)chunkyPlayer.getOwner();
        ChunkyTown chunkyTown = isMayor(chunkyPlayer);
        if(chunkyTown != null) return chunkyTown;
        return null;
    }

    public static ChunkyTown matchTown(String input) {
        String n = input.toLowerCase();
        for(ChunkyObject chunkyObject : ChunkyTownManager.getTowns().values()) {
            if(chunkyObject.getName().toLowerCase().contains(n)) return (ChunkyTown)chunkyObject;}
        return null;
    }

    public static ChunkyTown isMayor (ChunkyPlayer chunkyPlayer) {
        try {
            String id = chunkyPlayer.getData().getString("mayor");
            return getTown(id);
        } catch (JSONException e) {return null;}
    }

    public static boolean pay(ChunkyPlayer seller, ChunkyPlayer buyer, double amount) {
        Method.MethodAccount source = getAccount(buyer);
        Method.MethodAccount target = getAccount(seller);
        if(!source.hasEnough(amount)) {
            Language.sendBad(buyer, "You cannot afford " + Chunky.getMethod().format(amount));
            return false;
        }
        source.subtract(amount);
        target.add(amount);
        return true;
    }

    public static Method.MethodAccount getAccount(ChunkyObject chunkyObject) {
        return Chunky.getMethod().getAccount(chunkyObject.getName());
    }

    public static HashMap<String, ChunkyObject> getTowns() {
        return ChunkyManager.getObjectsOfType(ChunkyTown.class.getName());
    }

    public static long getPlayTime(ChunkyObject chunkyPlayer) {
        long curTime = System.currentTimeMillis();
        long joinTime = curTime;
        if(chunkyPlayer.getData().has("village-lastJoin")) joinTime = chunkyPlayer.getData().getLong("village-lastJoin");
        long playTime = 0;
        if(chunkyPlayer.getData().has("village-playTime")) playTime = chunkyPlayer.getData().getLong("village-playTime");
        return playTime + (curTime-joinTime)/(1000*60);

    }

    public static ChunkyTown.Stance getStance(ChunkyPlayer a, ChunkyPlayer b) {
        ChunkyTown aTown = getTown(a);
        ChunkyTown bTown = getTown(b);
        return aTown.getEffectiveStance(bTown);
    }


}
