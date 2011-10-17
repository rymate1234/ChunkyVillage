package org.getchunky.chunkyvillage.commands.toggle;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.ChatManager;
import org.getchunky.chunkyvillage.objects.ChunkyResident;

public class ToggleTownChat implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        if(chunkyResident.getTown() == null) {
            Language.sendBad(chunkyResident.getChunkyPlayer(), "You are not in a town.");
            return;}

        Language.sendGood(chunkyResident.getChunkyPlayer(), "Your chat mode has been set to " + ChatManager.toggleChat(chunkyResident.getName(), ChatManager.ChatMode.TOWN_CHAT).toString());
    }
}
