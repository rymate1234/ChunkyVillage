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
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class AddResident implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 1) {
            Strings.SPECIFY_PLAYER.bad(chunkyResident);
            return;}

        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Strings.NO_TOWN.bad(chunkyResident);
            return;}

        if(!chunkyResident.isAssistantOrMayor()) {
            Strings.NO_AUTHORITY.bad(chunkyResident);
            return;}

        ChunkyResident newResident = new ChunkyResident(strings[0]);

        if(newResident.getChunkyPlayer()==null) {
            Strings.UNKNOWN_PLAYER.bad(chunkyResident, strings[0]);
            return;}

        if(newResident.getTown() != null) {
            Strings.ALREADY_IN_TOWN.bad(chunkyResident, newResident.getName(), newResident.getTown().getName());
            return;}

        chunkyTown.addResident(newResident);
        Strings.ADDED_PLAYER.good(chunkyResident, newResident.getName(), chunkyTown.getName());
        Strings.NOTIFY_ADDED.good(newResident, chunkyTown.getName());
    }
}
