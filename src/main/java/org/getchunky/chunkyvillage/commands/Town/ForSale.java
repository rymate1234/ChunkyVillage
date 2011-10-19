package org.getchunky.chunkyvillage.commands.Town;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.objects.TownChunk;
import org.getchunky.chunkyvillage.util.Tools;

public class ForSale implements ChunkyCommandExecutor{
    public void onCommand(CommandSender sender, ChunkyCommand chunkyCommand, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        Player player = (Player)sender;
        ChunkyResident chunkyResident = new ChunkyResident(player);
        ChunkyChunk chunkyChunk = chunkyResident.getChunkyPlayer().getCurrentChunk();
        ChunkyTown chunkyTown = chunkyResident.getTown();
        if(chunkyTown == null) {
            Strings.NO_TOWN.bad(chunkyResident);
            return;
        }

        if(!chunkyTown.isOwnerOf(chunkyChunk)) {
            Strings.NOT_TOWN_LAND.bad(chunkyResident);
            return;
        }

        if(!chunkyResident.owns(chunkyChunk) && !chunkyResident.isAssistantOrMayor()) {
            Strings.NOT_OWNED.bad(chunkyResident);
            return;
        }

        double cost = 100;

        if(strings.length > 0) {
            cost = Tools.parseDouble(strings[0]);
            if(cost<0)
                Strings.SPECIFY_NUMBER.bad(chunkyResident);}

        new TownChunk(chunkyChunk).setForSale(cost);
        Strings.FORSALE.good(chunkyResident, Chunky.getMethod().format(cost));
    }
}
