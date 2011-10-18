package org.getchunky.chunkyvillage.commands.set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;

public class SetTitle implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;}

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        if(strings.length < 1) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "Proper usage is /t set title [player] <title>");
            return;}

        ChunkyResident target = chunkyResident;
        ChunkyTown chunkyTown = chunkyResident.getTown();
        String title = strings[0];

        if(strings.length ==2) {
            target = new ChunkyResident(strings[0]);
            if(!chunkyTown.isResident(target)) {
                Language.sendBad(chunkyResident.getChunkyPlayer(), "This player is not a part of your town.");
                return;
            }
            title = strings[1];
        }

        if(!chunkyResident.isMayor()) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You do not have the authority to do this");
            return;}

        target.setTitle(title.replace("&","§"));
        Language.sendGood(chunkyResident.getChunkyPlayer(), "You have changed " + target.getName()+ "'s to " + title);
    }
}
