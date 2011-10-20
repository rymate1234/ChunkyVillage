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

public class AdminKickResident implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!Permissions.ADMIN.has(sender)) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 1) {
            Strings.SPECIFY_PLAYER.bad(chunkyResident);
            return;
        }


        ChunkyResident toKick = new ChunkyResident(strings[1]);

        ChunkyTown chunkyTown = toKick.getTown();

        if(chunkyTown == null) {
            Strings.NOT_IN_A_TOWN.bad(chunkyResident, toKick.getName());
            return;}


        if(toKick.isAssistantOrMayor()) {
            Strings.NO_KICK.bad(chunkyResident);
            return;}

        chunkyTown.kickResident(toKick);
        Strings.KICKED_PLAYER.good(chunkyResident, toKick.getName(), chunkyTown.getName());
        Strings.NOTIFY_KICKED.bad(chunkyResident, chunkyTown.getName());
    }
}
