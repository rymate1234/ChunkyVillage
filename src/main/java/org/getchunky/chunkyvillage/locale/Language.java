package org.getchunky.chunkyvillage.locale;

import org.bukkit.util.config.Configuration;
import org.getchunky.chunkyvillage.ChunkyVillage;

import java.io.File;

public class Language {

    private static Configuration file;

    public static void load() {
       if(!ChunkyVillage.getInstance().getDataFolder().exists()) ChunkyVillage.getInstance().getDataFolder().mkdir();
       file = new Configuration(new File(ChunkyVillage.getInstance().getDataFolder(), "english.yml"));
       file.load();
       loadDefaults();
       file.save();
    }

    public static Configuration getFile() {
        return file;
    }

    private static void loadDefaults() {
       for(Strings string : Strings.values()) {
           string.get();}
    }
}

