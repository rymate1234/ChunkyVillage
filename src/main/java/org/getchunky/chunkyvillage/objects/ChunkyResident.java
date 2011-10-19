package org.getchunky.chunkyvillage.objects;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.config.Config;
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
        chunkyPlayer.getData().put("village-lastJoin", System.currentTimeMillis());
        chunkyPlayer.save();
    }

    public void setPlayTime(long playTime) {
        chunkyPlayer.getData().put("village-playTime", playTime);
        login();
    }

    public void subtractPlayTime(long playTime) {
        long time = (getPlayTime()-Math.abs(playTime));
        setPlayTime( time < 0 ? 0 : time);
    }

    public long getVotingPower() {
        return this.getPlayTime() / Config.Options.INFLUENCE_PER_VOTE.getInt();
    }


    public long getPlayTime() {
        long curTime = System.currentTimeMillis();
        long joinTime = curTime;
        if(chunkyPlayer.getData().has("village-lastJoin")) joinTime = chunkyPlayer.getData().getLong("village-lastJoin");
        long playTime = 0;
        if(chunkyPlayer.getData().has("village-playTime")) playTime = chunkyPlayer.getData().getLong("village-playTime");
        return playTime + (curTime-joinTime)/(1000*60);

    }

    public void setTitle(String title) {
        chunkyPlayer.getData().put("village-title", title);
        chunkyPlayer.save();
        applyTitle();
    }

    public void applyTitle() {
        if(!hasTitle()) return;
        try {chunkyPlayer.getPlayer().setDisplayName(getTitle() + " " + chunkyPlayer.getName());
        } catch (ChunkyPlayerOfflineException e) {}}

    public void removeTitle() {
        chunkyPlayer.getData().remove("village-title");
        chunkyPlayer.save();
    }

    public boolean hasTitle() {
        return chunkyPlayer.getData().has("village-title");
    }

    public String getTitle() {
        return chunkyPlayer.getData().getString("village-title");}

    public boolean isMayor() {
        ChunkyTown chunkyTown = getTown();
        return chunkyTown != null && chunkyTown.getMayor().equals(this);
    }

    public boolean isAssistant() {
        ChunkyTown chunkyTown = getTown();
        if(chunkyTown == null) return false;
        return chunkyTown.isAssistant(this);
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

    public ChunkyTown.Stance getEffectiveStance(ChunkyResident chunkyResident) {
        return ChunkyTownManager.getStance(this.getChunkyPlayer(), chunkyResident.getChunkyPlayer());
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
