package org.getchunky.chunkyvillage.commands.Admin;

import org.bukkit.command.CommandSender;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunkyvillage.config.Config;
import org.getchunky.chunkyvillage.locale.Strings;
import org.getchunky.chunkyvillage.permissions.Permissions;

public class Reload implements ChunkyCommandExecutor {
    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if(!Permissions.ADMIN.has(sender)) {
            Language.NO_COMMAND_PERMISSION.bad(sender);
            return;}

        Config.load();
        Strings.CONFIG_RELOADED.good(sender);

    }
}
