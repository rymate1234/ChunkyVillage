package org.getchunky.chunkyvillage.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Town implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }


        ChunkyResident chunkyResident = new ChunkyResident(sender);
        ChunkyTown myTown = chunkyResident.getTown();

        //Defaults to player's town.
        ChunkyTown chunkyTown = myTown;

        //Match Town in param
        if(strings.length > 0) chunkyTown = ChunkyTownManager.matchTown(strings[0]);

        //Return if no town found.
        if(chunkyTown==null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "Town not found.");
            return;}

        //Print info
        sender.sendMessage(ChatColor.GRAY + "|-------------------" + ChatColor.GREEN + "["+ChatColor.GOLD + chunkyTown.getName()+ChatColor.GREEN + "]" + ChatColor.GRAY + "-------------------|");

        //If Other Town
        if(myTown != null && myTown != chunkyTown) {
            ChunkyTown.Stance ourStance = myTown.getStance(chunkyTown);
            ChunkyTown.Stance theirStance = chunkyTown.getStance(myTown);
            ChunkyTown.Stance effectiveStance = myTown.getEffectiveStance(chunkyTown);
            sender.sendMessage(String.format(ChatColor.GRAY + "| " + ChatColor.GREEN + "Our Stance: %s"+ ChatColor.GRAY + " | " + ChatColor.GREEN + "Their Stance: %s ",ourStance.toString(), theirStance.toString()));
            sender.sendMessage(String.format(ChatColor.GRAY + "| " + ChatColor.GREEN + "Effective Stance: %s", effectiveStance.toString()));
        }

        String res = "";
        int i=0;
        for(ChunkyObject chunkyObject : chunkyTown.getResidents()) {
            res += ChatColor.WHITE + chunkyObject .getName() + ChatColor.GRAY + ", ";
            i++;
            if(i>40) break;
        }
        if(res.length()>2) res = res.substring(0,res.length()-2);

        String ass = "";
        int a=0;
        for(ChunkyObject chunkyObject : chunkyTown.getAssistants()) {
            ass += ChatColor.BLUE + chunkyObject.getName() + ChatColor.GRAY + ", ";
            i++;
            if(i>40) break;
        }
        if(ass.length()>2) ass = ass.substring(0,ass.length()-2);
        sender.sendMessage(
                ChatColor.GRAY + "| " + ChatColor.GREEN + "Size: " + ChatColor.YELLOW + chunkyTown.claimedChunkCount() + "/" +chunkyTown.maxChunks() +
                ChatColor.GRAY + " | "+ ChatColor.GREEN + "Influence: " + ChatColor.YELLOW + chunkyTown.getAverageInfluence());
        sender.sendMessage(
                ChatColor.GRAY + "| "+ ChatColor.GREEN + "Bank: " + ChatColor.YELLOW + chunkyTown.getAccount().balance() +
                ChatColor.GRAY + " | "+ ChatColor.GREEN + "Mayor: " + ChatColor.YELLOW + chunkyTown.getMayor().getName());

        sender.sendMessage(ChatColor.GRAY + "| " + ChatColor.GREEN + "Assistant: " + ChatColor.YELLOW + ass + ((i>40) ? " and more" : ""));
        sender.sendMessage(ChatColor.GRAY + "| " + ChatColor.GREEN + "Population: " + ChatColor.YELLOW + res + ((i>40) ? " and more" : ""));


    }
}
