package org.getchunky.chunkyvillage.commands.Admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.permissions.Permissions;

public class AdminKickResident implements ChunkyCommandExecutor{

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

        if(strings.length < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"Please specify player to kick.");
            return;
        }


        ChunkyResident toKick = new ChunkyResident(strings[1]);

        ChunkyTown chunkyTown = toKick.getTown();

        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"This player is not part of a town.");
            return;
        }


        if(toKick.isAssistantOrMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You may not kick assistants or the mayor.");
            return;
        }

        chunkyTown.kickResident(toKick);
        Language.sendGood(chunkyResident.getChunkyPlayer(),toKick.getName() + " was kicked from " + chunkyTown.getName());
        Language.sendGood(toKick.getChunkyPlayer(),"You were kicked from " + chunkyTown.getName());



    }
}
