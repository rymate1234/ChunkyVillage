package org.getchunky.chunkyvillage.objects;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.register.payment.Method;
import org.json.JSONObject;


public class ChunkyResident {

    private ChunkyPlayer chunkyPlayer = null;

    public ChunkyResident(ChunkyObject chunkyObject) {
        chunkyPlayer = (ChunkyPlayer)chunkyObject;
    }

    public ChunkyResident(CommandSender sender) {
        this(sender.getName());
    }

    public ChunkyResident(String player) {
        this(ChunkyManager.getChunkyPlayer(player));
    }

    public ChunkyResident(Player player) {
        this(ChunkyManager.getChunkyPlayer(player));

    }

    public ChunkyResident(ChunkyPlayer chunkyPlayer) {
        this.chunkyPlayer = chunkyPlayer;
    }

    public ChunkyTown getTown() {
        return ChunkyTownManager.getTown(chunkyPlayer);
    }

    public ChunkyPlayer getChunkyPlayer() {
        return this.chunkyPlayer;
    }

    public void logout() {
        setPlayTime(getPlayTime());
        chunkyPlayer.getData().remove("village-lastJoin");
        chunkyPlayer.save();
    }

    public void login() {
        chunkyPlayer.getData().put("village-lastJoin",System.currentTimeMillis());
        chunkyPlayer.save();
    }

    public void setPlayTime(long playTime) {
        chunkyPlayer.getData().put("village-playTime", playTime);
    }

    public long getPlayTime() {
        long curTime = System.currentTimeMillis();
        long joinTime = curTime;
        if(chunkyPlayer.getData().has("village-lastJoin")) joinTime = chunkyPlayer.getData().getLong("village-lastJoin");
        long playTime = 0;
        if(chunkyPlayer.getData().has("village-playTime")) playTime = chunkyPlayer.getData().getLong("village-playTime");
        return playTime + (curTime-joinTime)/(1000*60);

    }

    public boolean isMayor() {
        ChunkyTown chunkyTown = getTown();
        return chunkyTown != null && chunkyTown.getMayor() == this.chunkyPlayer;
    }

    public boolean isAssistant() {
        ChunkyTown chunkyTown = getTown();
        return chunkyTown.getAssistants().contains(chunkyPlayer.getName());
    }

    public boolean isAssistantOrMayor() {
        return (isMayor() || isAssistant());
    }

    public String getName() {
        return chunkyPlayer.getName();
    }

    public Method.MethodAccount getAccount() {
        return Chunky.getMethod().getAccount(this.getName());
    }

    public boolean pay(Method.MethodAccount fromAccount, double amount) {
        if(!fromAccount.hasEnough(amount)) return false;
        fromAccount.subtract(amount);
        getAccount().add(amount);
        return true;
    }

    public boolean owns(ChunkyObject chunkyObject) {
        return chunkyPlayer.isOwnerOf(chunkyObject);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChunkyResident && ((ChunkyResident) obj).getName().equals(getName());
    }

    public JSONObject getData() {
        return chunkyPlayer.getData();
    }

    public void save() {
        chunkyPlayer.save();
    }


}