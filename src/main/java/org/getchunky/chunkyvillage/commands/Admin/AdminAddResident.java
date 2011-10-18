package org.getchunky.chunkyvillage.commands.Admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.permissions.Permissions;

public class AdminAddResident implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if(!Permissions.ADMIN.has(sender)) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 2) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"Please specify town and player to add.");
            return;
        }

        ChunkyTown chunkyTown = ChunkyTownManager.matchTown(strings[0]);
        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"This town could not be found.");
            return;
        }

        ChunkyResident newResident = new ChunkyResident(strings[1]);

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
