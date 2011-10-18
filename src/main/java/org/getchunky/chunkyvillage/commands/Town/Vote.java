package org.getchunky.chunkyvillage.commands.Town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.util.Config;

import static org.getchunky.chunkyvillage.util.Config.Options.ELECTION_PERCENTAGE;
import static org.getchunky.chunkyvillage.util.Config.Options.INFLUENCE_PER_VOTE;

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

        if(chunkyResident.getVotingPower() < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You have 0 votes. Increase your Influence.");
            return;}

        int i = chunkyTown.addVote(chunkyResident,candidate);
        int required = chunkyTown.getTotalInfluence()/ INFLUENCE_PER_VOTE.getInt() * ELECTION_PERCENTAGE.getInt()/100;
        chunkyTown.goodMessageTown("Someone has voted for " + candidate.getName() + ", " + i + " total votes with " + (required - i) + " left to go.");
        if(required <= i) {
            if(!candidate.getName().equals(chunkyTown.getOwner().getName()))chunkyTown.setMayor(candidate);
            chunkyTown.clearVotes();
            chunkyTown.save();
            chunkyTown.goodMessageTown(candidate.getName() + " has been elected mayor!");
        }
    }

}
