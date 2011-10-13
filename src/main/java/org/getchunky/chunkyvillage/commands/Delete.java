package org.getchunky.chunkyvillage.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class Delete implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(sender.getName());

        ChunkyTown chunkyTown = ChunkyTownManager.isMayor(chunkyPlayer);

        if(chunkyTown==null) {
            Language.sendBad(chunkyPlayer, "You do not have the authority to do this.");
            return;
        }

        chunkyTown.goodMessageTown("The town has been disbanded.");
        chunkyTown.deleteTown();

    }
}
