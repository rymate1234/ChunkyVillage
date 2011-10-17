package org.getchunky.chunkyvillage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunkyvillage.objects.ChunkyResident;
import org.getchunky.chunkyvillage.objects.ChunkyTown;
import org.getchunky.chunkyvillage.util.Config;

import java.util.HashMap;
import java.util.HashSet;

public class ChatManager {

    private static HashMap<String, ChatMode> chatter = new HashMap<String, ChatMode>();

    public enum ChatMode {
        NONE,
        TOWN_CHAT;

        @Override
        public String toString() {
            return this.name().toLowerCase().replace("_"," ");
        }

    }

    public static ChatMode toggleChat(String player, ChatMode chatMode) {
        ChatMode currentMode = getMode(player);
        if(currentMode == chatMode) {
            chatter.remove(player);
            return ChatMode.NONE;}

        chatter.put(player, chatMode);
        return chatMode;
    }

    public static ChatMode getMode(String player) {
        ChatMode chatMode = chatter.get(player);
        if(chatMode == null) return ChatMode.NONE;
        return chatMode;
    }

    public static boolean handleMessage(Player player, String message) {
        ChatMode chatMode = getMode(player.getName());
        if(chatMode == ChatMode.NONE) return false;
        ChunkyResident sender = new ChunkyResident(player);

        if(chatMode == ChatMode.TOWN_CHAT) {
            String formatted = formatChat(sender, message, chatMode);
            ChunkyTown chunkyTown = sender.getTown();
            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                if(!chunkyTown.isResident(new ChunkyResident(p))) continue;
                p.sendMessage(formatted);}}

        return true;
    }

    public static String formatChat(ChunkyResident sender, String message, ChatMode chatMode) {
        String filter = "";
        Player player;
        try {player = sender.getChunkyPlayer().getPlayer();} catch (ChunkyPlayerOfflineException e) {return "";}
        if(chatMode == ChatMode.TOWN_CHAT) filter = Config.Options.TOWN_CHAT_FORMAT.getString();
        return filter
                .replace("%town%", sender.getTown().getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%name%", player.getName())
                .replace("%msg%", message);
    }

}
