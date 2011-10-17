package org.getchunky.chunkyvillage.commands.set;

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
import org.getchunky.chunkyvillage.util.Tools;

public class SetMayor implements ChunkyCommandExecutor{


    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
         if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyResident chunkyResident = new ChunkyResident(sender);

        ChunkyTown chunkyTown = chunkyResident.getTown();

        if(!chunkyResident.isMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You do not have the authority to do this");
            return;}

        if(strings.length < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "Please specify the new mayor.");
            return;
        }
        ChunkyResident newMayor = new ChunkyResident(strings[0]);

        if(newMayor.equals(chunkyResident)) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You are already the mayor.");
            return;
        }

        if(!chunkyTown.isResident(newMayor)) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "This player is not part of your town");
            return;
        }

        if(chunkyResident.isAssistant()) chunkyTown.removeAssistant(newMayor);

        chunkyTown.setMayor(newMayor);

        chunkyTown.goodMessageTown(newMayor.getName() + " is now the new mayor.");

    }
}
