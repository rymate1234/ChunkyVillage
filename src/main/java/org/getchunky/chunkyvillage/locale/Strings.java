package org.getchunky.chunkyvillage.locale;

import org.bukkit.ChatColor;
import org.getchunky.chunkyvillage.objects.ChunkyResident;

public enum Strings {


    CANNOT_AFFORD("cannotAfford", "You cannot afford &e%1&f."),
    NO_COMMAND_PERMISSION("noPermissions", "You do not have permission to do this."),
    IN_GAME_ONLY("inGameOnly", "You must be in game to do this."),
    SPECIFY_TOWN_AND_PLAYER("specifyTownAndPlayer", "Please specify town and player to add."),
    SPECIFY_PLAYER("specifyPlayer", "Please specify player"),
    SPECIFY_AMOUNT("specifyAmount", "Please specify amount."),
    SPECIFY_NUMBER("specifyNumber", "Please a proper number."),
    UNKNOWN_TOWN("unknownTown", "This town could not be found: %1."),
    UNKNOWN_PLAYER("unknownPlayer", "This player does not exist: %1"),
    ALREADY_IN_TOWN("alreadyInTown", "%1 is already a part of %2"),
    ADDED_PLAYER("addedPlayer", "%1 was added to %2."),
    NOTIFY_ADDED("notifyAdded", "You were added to %1."),
    KICKED_PLAYER("kickedPlayer", "%1 was kicked from %2"),
    NOTIFY_KICKED("notifyKicked", "You were kicked from %1"),
    NOT_IN_A_TOWN("notInTown", "%1 is not in a town."),
    NO_TOWN("noTown", "You are not in a town."),
    NOT_IN_YOUR_TOWN("notYourTown", "%1 is not in %2."),
    NO_AUTHORITY("noAuthority", "You do not have the authority to do this."),
    NO_KICK("noKick", "You may not kick assistants or the mayor."),
    NOT_TOWN_LAND("notTownLand", "This is not town land."),
    NOT_OWNED("notOwned", "You do not own this land."),
    FORSALE("forsale", "This plot is on sale for &e%1&f")
    ;

    private String path;
    private String string;

    private static String prefix = ChatColor.AQUA + "[Village] ";

    Strings(String path, String string) {
        this.path = path;
        this.string = string;
    }

    public String get() {
        return Language.getFile().getString(path, string);
    }

    public void good(ChunkyResident chunkyResident, Object... args) {
        org.getchunky.chunky.locale.Language.sendMessage(
                chunkyResident.getChunkyPlayer(),
                prefix + ChatColor.GREEN + string,
                args);
    }

    public void bad(ChunkyResident chunkyResident, Object... args) {
        org.getchunky.chunky.locale.Language.sendMessage(
                chunkyResident.getChunkyPlayer(),
                prefix + ChatColor.RED + string,
                args);
    }

    public void send(ChunkyResident chunkyResident, Object... args) {
        org.getchunky.chunky.locale.Language.sendMessage(
                chunkyResident.getChunkyPlayer(),
                string,
                args);
    }
}
