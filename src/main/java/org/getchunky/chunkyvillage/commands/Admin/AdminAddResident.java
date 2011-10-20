package org.getchunky.chunkyvillage.commands.Admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.permissions.Permissions;

public class AdminAddResident implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {

        if(!Permissions.ADMIN.has(sender)) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 2) {
            Strings.SPECIFY_TOWN_AND_PLAYER.bad(chunkyResident);
            return;
        }

        ChunkyTown chunkyTown = ChunkyTownManager.matchTown(strings[0]);
        if(chunkyTown == null) {
            Strings.UNKNOWN_TOWN.bad(chunkyResident, strings[0]);
            return;
        }

        ChunkyResident newResident = new ChunkyResident(strings[1]);

        if(newResident.getChunkyPlayer()==null) {
            Strings.UNKNOWN_PLAYER.bad(chunkyResident, strings[1]);
            return;}

        if(newResident.getTown() != null) {
            Strings.ALREADY_IN_TOWN.bad(chunkyResident, newResident.getName(), newResident.getTown().getName());
            return;}

        chunkyTown.addResident(newResident);
        Strings.ADDED_PLAYER.good(chunkyResident, newResident.getName(), chunkyTown.getName());
        Strings.NOTIFY_ADDED.good(newResident, chunkyTown.getName());



    }
}
