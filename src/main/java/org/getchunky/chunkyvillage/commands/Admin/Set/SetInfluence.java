package org.getchunky.chunkyvillage.commands.Admin.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.permissions.Permissions;
import org.getchunky.chunkyvillage.util.Tools;

public class SetInfluence implements ChunkyCommandExecutor {
    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if(!Permissions.ADMIN.has(sender)) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;
        }

        ChunkyResident chunkyResident = new ChunkyResident(sender);
        ChunkyResident target = chunkyResident;

        if(args.length < 0) {
            Strings.SPECIFY_AMOUNT.bad(chunkyResident);
            return;
        }
        int influence = Tools.parseInt(args[0]);

        if(args.length < 1) {
            Strings.SPECIFY_NUMBER.bad(chunkyResident);
            return;}

        if(args.length > 1) {
            target = new ChunkyResident(args[0]);
            if(target == null) {
                Strings.UNKNOWN_PLAYER.bad(chunkyResident);
                return;}
            influence = Tools.parseInt(args[1]);
            if(args.length < 0) {
                Strings.SPECIFY_AMOUNT.bad(chunkyResident);
                return;}}
        target.setPlayTime(influence);
        Strings.INFLUENCE_SET.good(chunkyResident, influence, target.getName());



    }
}
