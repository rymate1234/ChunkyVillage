package org.getchunky.chunkyvillage.commands.Town;

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
import org.getchunky.chunkyvillage.util.Config;
import org.getchunky.chunkyvillage.util.Tools;

public class Vote implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
                   Language.IN_GAME_ONLY.bad(sender);
                   return;}

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        ChunkyTown chunkyTown = chunkyResident.getTown();

        if(chunkyTown == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(),"You are not part of a town.");
            return;}

        if(strings.length < 1) {
            chunkyTown.printVotes(chunkyResident);
            return;
        }

        ChunkyResident candidate = new ChunkyResident(strings[0]);


        if(!chunkyTown.isResident(candidate)) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), candidate.getName() + " is not part of your town.");
            return;}

        int i = chunkyTown.addVote(chunkyResident,candidate);
        chunkyTown.goodMessageTown(chunkyResident.getName() + " has voted for " + candidate.getName() + ", " + i + " total votes.");
        if(chunkyTown.getResidents().size() * (Config.Options.ELECTION_PERCENTAGE.getDouble()/100) <= i) {
            if(!candidate.getName().equals(chunkyTown.getOwner().getName()))chunkyTown.setMayor(candidate);
            chunkyTown.clearVotes();
            chunkyTown.save();
            chunkyTown.goodMessageTown(candidate.getName() + " has been elected mayor!");
        }
    }

}
