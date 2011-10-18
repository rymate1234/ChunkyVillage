package org.getchunky.chunkyvillage.commands.Town;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class AddResident implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"Please specify player to add.");
            return;
        }

        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You are not part of a town.");
            return;
        }

        if(!chunkyResident.isAssistantOrMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You do not have the authority to do this.");
            return;
        }

        ChunkyResident newResident = new ChunkyResident(strings[0]);

        if(newResident.getChunkyPlayer()==null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "This player does not exist.");
            return;
        }

        if(newResident.getTown() != null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"This player is already part of a town");
            return;
        }

        chunkyTown.addResident(newResident);
        Language.sendGood(chunkyResident.getChunkyPlayer(),newResident.getName() + " added to " + chunkyTown.getName());
        Language.sendGood(newResident.getChunkyPlayer(),"You were added to town " + chunkyTown.getName());



    }
}
