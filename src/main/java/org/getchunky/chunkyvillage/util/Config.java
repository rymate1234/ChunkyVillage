package org.getchunky.chunkyvillage.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.config.Configuration;
import org.getchunky.chunkyvillage.ChunkyVillage;

public class Config {
    private static Configuration configuration;
    private static YamlConfiguration yml;

    public enum Options {

        CHUNKS_PER_INFLUENCE("settings.town.chunksPerInfluence", 0.5),
        BASE_CHUNKS("settings.town.baseChunks", 10),
        ELECTION_PERCENTAGE("settings.town.electionPercentage", 50),
        DEATH_TOLL("settings.resident.influenceLostOnDeath", 10),
        TOWN_CHAT_FORMAT("settings.town.townChatFormat", "&3[%town%]&f %displayname%: &3%msg%"),
        MAYOR_TITLE("settings.town.defaultMayorTitle", "&6Mayor&f"),
        ASSISTANT_TITLE("settings.town.defaultAssistantTitle", "&5Assistant&f")
        ;

        private String path;
        private Object def;

        Options(String path, Object def) {
            this.path = path;
            this.def = def;
        }

        public String getString() {
            return configuration.getString(path, def.toString());}

        public int getInt() {
            return configuration.getInt(path, (Integer)def);}

        public double getDouble() {
            return configuration.getDouble(path, (Double)def);}


    }

    public static void load() {
        if(!ChunkyVillage.getInstance().getDataFolder().exists()) ChunkyVillage.getInstance().getDataFolder().mkdir();
        configuration = ChunkyVillage.getInstance().getConfiguration();
        configuration.load();
        loadDefaults();
        configuration.save();
    }

    private static void loadDefaults() {
        for(Options option : Options.values()) {
            option.getString();}
    }



}
