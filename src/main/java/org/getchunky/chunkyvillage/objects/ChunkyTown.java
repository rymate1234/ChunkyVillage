package org.getchunky.chunkyvillage.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyPermissions;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyGroup;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.commands.List;
import org.getchunky.chunkyvillage.util.Config;
import org.getchunky.register.payment.Method;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ChunkyTown extends ChunkyObject {

    public ChunkyGroup getAssistantGroup() {
        ChunkyGroup assistants = (ChunkyGroup)ChunkyManager.getObject(ChunkyGroup.class.getName(), getAssistantGroupId());
        if(assistants == null) {
            assistants = new ChunkyGroup();
            assistants.setId(getAssistantGroupId()).setName(getAssistantGroupId());
            HashMap<PermissionFlag, Boolean> flags = new HashMap<PermissionFlag, Boolean>();
            flags.put(ChunkyPermissions.BUILD, true);
            flags.put(ChunkyPermissions.SWITCH, true);
            flags.put(ChunkyPermissions.DESTROY, true);
            flags.put(ChunkyPermissions.ITEM_USE, true);
            ChunkyManager.setPermissions(this, assistants, flags);}

        return assistants;
    }

    public HashSet<ChunkyObject> getAssistants() {
        HashSet<ChunkyObject> result = getAssistantGroup().getMembers().get(ChunkyPlayer.class.getName());
        if(result != null) return result;
        return new HashSet<ChunkyObject>();
    }

    private String getAssistantGroupId() {
        return getId() + "-assistants";
    }

    public boolean addAssistant(ChunkyResident chunkyResident) {
        if(chunkyResident.isAssistant()) return false;
        ChunkyGroup assistants = getAssistantGroup();
        assistants.addMember(chunkyResident.getChunkyPlayer());
        return true;
    }

    public boolean isAssistant(ChunkyResident chunkyResident) {
        return getAssistants().contains(chunkyResident.getChunkyPlayer());
    }

    public boolean removeAssistant(ChunkyResident chunkyResident) {
        if(!isAssistant(chunkyResident)) return false;
        getAssistantGroup().removeMember(chunkyResident.getChunkyPlayer());
        return true;
    }

    public ChunkyPlayer getMayor() {
        return (ChunkyPlayer)this.getOwner();
    }

    public ChunkyTown setMayor(ChunkyResident mayor) {
        ChunkyObject oldOwner = this.getOwner();
        this.setOwner(mayor.getChunkyPlayer(), true, false);
        if(oldOwner!=null) {
            oldOwner.setOwner(this,true,false);
            oldOwner.getData().remove("mayor");}
        mayor.getData().put("mayor",this.getId());
        mayor.save();
        return this;
    }

    public ChunkyTown setHome(ChunkyChunk chunk) {
        this.getData().put("home", chunk.getFullId());
        return this;
    }

    public ChunkyChunk getHome() {
        return (ChunkyChunk)ChunkyManager.getObject(this.getData().getString("home"));
    }

    public Method.MethodAccount getAccount() {
        return Chunky.getMethod().getAccount("town-" + this.getId());
    }

    public HashSet<ChunkyObject> getResidents() {
        HashSet<ChunkyObject> ret = new HashSet<ChunkyObject>();
        if(this.getOwnables().get(ChunkyPlayer.class.getName())!=null) ret.addAll(this.getOwnables().get(ChunkyPlayer.class.getName()));
        ret.add(getMayor());
        return ret;
    }

    public boolean deposit(ChunkyResident buyer, double amount) {
        Method.MethodAccount source = buyer.getAccount();
        if(!source.hasEnough(amount)) {
            Language.sendBad(buyer.getChunkyPlayer(),"You cannot afford " + Chunky.getMethod().format(amount));
            return false;
        }
        source.subtract(amount);
        this.getAccount().add(amount);
        this.goodMessageTown("The town has received " + Chunky.getMethod().format(amount) + " from " + buyer.getName());
        return true;
    }

    public boolean withdraw(ChunkyResident receiver, double amount) {
        if(!receiver.pay(getAccount(), amount)) {
            Language.sendBad(receiver.getChunkyPlayer(),"The town doesn't have " + Chunky.getMethod().format(amount));
            return false;}
        this.goodMessageTown(receiver.getName() + " has received " + Chunky.getMethod().format(amount) + " from the town bank.");
        return true;
    }

    public boolean isResident(ChunkyResident player) {
        return getResidents().contains(player.getChunkyPlayer());
    }

    public ChunkyTown addResident(ChunkyResident chunkyResident) {
        chunkyResident.getChunkyPlayer().setOwner(this, false, true);
        chunkyResident.setPlayTime(0);
        return this;
    }

    public ChunkyTown kickResident(ChunkyResident chunkyResident) {
        chunkyResident.getChunkyPlayer().setOwner(null, false, true);
        return this;
    }

    public int maxChunks() {
        return (int)(getAverageInfluence() * Config.getChunkBonusPerPlayer()) + Config.getStartingChunks();
    }

    public int claimedChunkCount() {
        int i =0;
        i+= this.getOwnables().get(ChunkyChunk.class.getName()).size();
        for(ChunkyObject chunkyPlayer : getResidents()) {
            HashSet<ChunkyObject> chunks = chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName());
            if(chunks != null) i+= chunks.size();
        }
        return i;
    }

    public double taxPlayers(double tax) {
        Method.MethodAccount account = getAccount();
        double sum=0;

        for(ChunkyObject chunkyObject : getResidents()) {
            ChunkyResident chunkyResident = new ChunkyResident(chunkyObject);
            Method.MethodAccount res = chunkyResident.getAccount();
            double amount = res.balance()*tax;
            sum+=amount;
            res.subtract(amount);
            Language.sendMessage(chunkyResident.getChunkyPlayer(), ChatColor.AQUA + "You paid " + Chunky.getMethod().format(amount) + " in taxes.");
        }
        account.add(sum);
        return sum;
    }

    public JSONObject getVotes() {
        if(this.getData().has("votes"))return this.getData().getJSONObject("votes");
        JSONObject jsonObject = new JSONObject();
        this.getData().put("votes",jsonObject);
        return jsonObject;
    }

    public int addVote(ChunkyResident chunkyResident, ChunkyResident candidate) {
        JSONObject votes = getVotes();
        votes.put(chunkyResident.getName(), candidate.getName());
        save();
        int i=0;
        Iterator keys = votes.keys();
        String name = candidate.getName();
        while(keys.hasNext()) {
            if(votes.getString(keys.next().toString()).equals(name)) i++;}
        return i;
    }

    public void clearVotes() {
        this.getData().remove("votes");
    }

    public void printVotes(ChunkyResident chunkyResident) {
        ChunkyPlayer chunkyPlayer = chunkyResident.getChunkyPlayer();
        HashMap<String, Integer> standings = new HashMap<String, Integer>();
        JSONObject votes = getVotes();
        Iterator keys = votes.keys();
        while (keys.hasNext()) {
            String voter = keys.next().toString();
            String candidate = null;
            candidate = votes.getString(voter);
            if(!standings.containsKey(candidate)) standings.put(candidate,1);
            else {
                Integer v = standings.get(candidate);
                v++;
                standings.put(candidate,v);
            }
        }
        Language.sendMessage(chunkyPlayer,ChatColor.GRAY + "|-------------------" +ChatColor.GREEN + "[Votes]" + ChatColor.GRAY + "-------------------|");
        for(String candidate : standings.keySet()) {
            Language.sendMessage(chunkyPlayer,ChatColor.GREEN + candidate + ": " + ChatColor.YELLOW + standings.get(candidate) + " votes");
        }
    }

    public void deleteTown() {
        for(HashSet<ChunkyObject> chunkyObjects : ((HashMap<String, HashSet<ChunkyObject>>)(this.getOwnables().clone())).values()) {
            for(ChunkyObject chunkyObject : (HashSet<ChunkyObject>)chunkyObjects.clone()) {
                if(chunkyObject instanceof ChunkyGroup) {
                    chunkyObject.delete();
                    continue;
                }
                chunkyObject.setOwner(null,true,false);
            }}
        this.getOwner().getData().remove("mayor");
        this.getOwner().save();
        this.setOwner(null,false,true);
        save();
        delete();
    }

    public void goodMessageTown(String message) {
        for(Player player : Bukkit.getServer().getOnlinePlayers()) {
            ChunkyResident chunkyResident = new ChunkyResident(player);
            if(this.isResident(chunkyResident)) Language.sendGood(chunkyResident.getChunkyPlayer(),message);
        }
    }

    public int getAverageInfluence() {
        int influence = 0;
        HashSet<ChunkyObject> residents = getResidents();
        for(ChunkyObject chunkyObject : residents) {
            influence+= new ChunkyResident(chunkyObject.getName()).getPlayTime();}
        return influence/residents.size();
    }

    public enum Stance {
        ENEMY,
        NEUTRAL,
        ALLY;

        @Override
        public String toString() {
            switch (this) {
                case ENEMY:
                    return ChatColor.RED + "Enemy";
                case ALLY:
                    return ChatColor.DARK_GREEN + "Ally";
                case NEUTRAL:
                    return ChatColor.WHITE + "Neutral";
            }
            return this.name();
        }
    }

    public Stance getStanceFromString(String string) {
        for(Stance stance : Stance.values()) {
            if(stance.name().equalsIgnoreCase(string)) return stance;
        }
        return null;
    }

    public Stance setStance(ChunkyTown chunkyTown, String stance) {
        return setStance(chunkyTown, getStanceFromString(stance));
    }

    public Stance setStance(ChunkyTown chunkyTown, Stance stance) {
        if(getStance(chunkyTown).equals(stance)) return null;
        getDiplomacy().put(chunkyTown.getId(),stance.name());
        save();
        return stance;
    }

    public Stance getStance(ChunkyTown chunkyTown) {
        if(chunkyTown == this) return Stance.ALLY;
        if(chunkyTown == null) return Stance.NEUTRAL;
        if(getDiplomacy().has(chunkyTown.getId())) return getStanceFromString(getDiplomacy().getString(chunkyTown.getId()));
        return Stance.NEUTRAL;
    }


    public Stance getEffectiveStance(ChunkyTown otherTown) {
        if(otherTown == this) return Stance.ALLY;
        if(otherTown == null) return Stance.NEUTRAL;
        Stance myStance = getStance(otherTown);
        Stance theirStance = otherTown.getStance(this);
        if(myStance.equals(Stance.ENEMY) || theirStance.equals(Stance.ENEMY)) return Stance.ENEMY;
        if(myStance.equals(Stance.ALLY) && theirStance.equals(Stance.ALLY)) return Stance.ALLY;
        return Stance.NEUTRAL;
    }

    public JSONObject getDiplomacy() {
        JSONObject ret = this.getData().optJSONObject("diplomacy");
        if(ret==null) {
            ret =  new JSONObject();
            this.getData().put("diplomacy",ret);
        }
        return ret;
    }


}

