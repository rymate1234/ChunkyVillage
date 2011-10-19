package org.getchunky.chunkyvillage.commands.Town;

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

public class Delete implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}
        ChunkyResident chunkyResident = new ChunkyResident(sender);

        if(!chunkyResident.isMayor()) {
            Strings.NO_AUTHORITY.bad(chunkyResident);
            return;
        }
        ChunkyTown chunkyTown = chunkyResident.getTown();

        chunkyTown.goodMessageTown("The town has been disbanded.");
        chunkyTown.deleteTown();

    }
}
