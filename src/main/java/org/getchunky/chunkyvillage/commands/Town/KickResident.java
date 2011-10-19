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

public class KickResident implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(strings.length < 1) {
            Strings.SPECIFY_PLAYER.bad(chunkyResident);
            return;
        }

        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Strings.NO_TOWN.bad(chunkyResident);
            return;
        }

        if(!chunkyResident.isAssistantOrMayor()) {
            Strings.NO_AUTHORITY.bad(chunkyResident);
            return;
        }

        ChunkyResident toKick = new ChunkyResident(strings[0]);

        if(toKick.isAssistantOrMayor()) {
            Strings.NO_KICK.bad(chunkyResident);
            return;
        }

        if(!chunkyTown.isResident(toKick)) {
            Strings.NOT_IN_YOUR_TOWN.bad(chunkyResident, toKick.getName(), chunkyTown.getName());
            return;
        }

        chunkyTown.kickResident(toKick);
        Strings.KICKED_PLAYER.good(chunkyResident, toKick.getName(), chunkyTown.getName());
        Strings.NOTIFY_KICKED.bad(toKick, chunkyTown.getName());
    }
}
