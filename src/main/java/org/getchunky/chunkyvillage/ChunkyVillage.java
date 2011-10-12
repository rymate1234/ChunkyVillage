package org.getchunky.chunkyvillage;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunkyvillage.commands.*;
import org.getchunky.chunkyvillage.commands.set.Set;
import org.getchunky.chunkyvillage.commands.set.SetMayor;
import org.getchunky.chunkyvillage.commands.set.SetName;
import org.getchunky.chunkyvillage.commands.toggle.Toggle;
import org.getchunky.chunkyvillage.commands.toggle.ToggleAssistant;
import org.getchunky.chunkyvillage.listeners.ChunkyEvents;
import org.getchunky.chunkyvillage.util.Config;

import java.util.Arrays;

public class ChunkyVillage extends JavaPlugin {
    private ChunkyEvents chunkyEvents;

    private static Plugin plugin;

    public static Plugin getInstance() {
        return plugin;
    }

    public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }

    public void onEnable() {
        plugin = this;

        Config.load();

        Chunky.getModuleManager().registerModule(this);

        chunkyEvents = new ChunkyEvents();
        Chunky.getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_CHUNK_CLAIM, chunkyEvents, ChunkyEvent.Priority.Normal,this);
        Chunky.getModuleManager().registerEvent(ChunkyEvent.Type.PLAYER_CHUNK_CHANGE, chunkyEvents, ChunkyEvent.Priority.Normal,this);

        registerChunkyCommands();

        System.out.println(this + " is now enabled!");
    }

    private void registerChunkyCommands(){
        ChunkyCommand root = Chunky.getModuleManager().getCommandByName("chunky");
        try {
            ChunkyCommand town = new ChunkyCommand("town",new Town(),root).setAliases("t").setHelpLines("/chunky town or /c t").setDescription("Shows information about town");

            ChunkyCommand list = new ChunkyCommand("list",new List(),town).setAliases("l").setHelpLines("/c town list or /c t l").setDescription("Lists towns.");

            ChunkyCommand newTown = new ChunkyCommand("new",new NewTown(),town).setAliases("n").setHelpLines("/c t new <name>").setDescription("Create a new town.");

            ChunkyCommand forSale = new ChunkyCommand("forsale",new ForSale(),town).setAliases("fs").setHelpLines("/c t fs <cost>").setDescription("Set plot for sale.");

            ChunkyCommand notForSale = new ChunkyCommand("notforsale",new NotForSale(),town).setAliases("nfs").setHelpLines("/c t nfs").setDescription("Removes a plot from sale");

            ChunkyCommand addResident = new ChunkyCommand("add",new AddResident(),town).setAliases("a").setDescription("Adds resident to town.").setHelpLines("/c town add <player> or /c t k <player>");

            ChunkyCommand kickResident = new ChunkyCommand("kick",new KickResident(),town).setAliases("k").setDescription("Removes resident from town.").setHelpLines("/c town kick <player> or /c t r <player>");

            ChunkyCommand tax = new ChunkyCommand("tax",new Tax(),town).setAliases("tax").setDescription("Taxes all residents.").setHelpLines("/c town tax <1-100> or /c t tax <1-100>");

            ChunkyCommand vote = new ChunkyCommand("vote",new Vote(),town).setAliases("v").setDescription("Votes for a mayor.").setHelpLines("/c town vote <player> or /c t v <player>");

            ChunkyCommand spawn = new ChunkyCommand("spawn",new Spawn(),town).setAliases("s").setDescription("Teleport to town.").setHelpLines("/c town spawn or /c t s");

            ChunkyCommand leave = new ChunkyCommand("leave",new Leave(),town).setAliases("lv").setDescription("Leave the town").setHelpLines("/c town leave or /c t lv");

            ChunkyCommand withdraw = new ChunkyCommand("withdraw",new Withdraw(),town).setAliases("w").setDescription("Withdraw money from town bank.").setHelpLines("c town withdraw <amount> or /c t w <amount>");

            ChunkyCommand set = new ChunkyCommand("set",new Set(),town).setAliases("s").setDescription("Set various options.").setHelpLines("/c town set ? or /c t s ?");

            ChunkyCommand setName = new ChunkyCommand("name",new SetName(),set).setAliases("n").setDescription("Set town name.").setHelpLines("/c town set name <name> or /c t s n <name>");

            ChunkyCommand setMayor = new ChunkyCommand("mayor",new SetMayor(),set).setAliases("m").setDescription("Set town mayor.").setHelpLines("/c town set mayor <name> or /c t s m <name>");

            ChunkyCommand toggle = new ChunkyCommand("toggle",new Set(),town).setAliases("t").setDescription("Toggle various options.").setHelpLines("/c town toggle ? or /c t t ?");

            ChunkyCommand toggleAssistant = new ChunkyCommand("assistant",new ToggleAssistant(),toggle).setAliases("a").setDescription("Toggles an assistant.").setHelpLines("/c town toggle assistant <name> or /c t t a <name>");

            Chunky.getModuleManager().registerCommand(town);
            Chunky.getModuleManager().registerCommand(newTown);
            Chunky.getModuleManager().registerCommand(vote);
            Chunky.getModuleManager().registerCommand(list);
            Chunky.getModuleManager().registerCommand(spawn);
            Chunky.getModuleManager().registerCommand(leave);
            Chunky.getModuleManager().registerCommand(withdraw);

            Chunky.getModuleManager().registerCommand(forSale);
            Chunky.getModuleManager().registerCommand(notForSale);
            Chunky.getModuleManager().registerCommand(tax);

            Chunky.getModuleManager().registerCommand(addResident);
            Chunky.getModuleManager().registerCommand(kickResident);

            Chunky.getModuleManager().registerCommand(set);
            Chunky.getModuleManager().registerCommand(setName);
            Chunky.getModuleManager().registerCommand(setMayor);

            Chunky.getModuleManager().registerCommand(toggle);
            Chunky.getModuleManager().registerCommand(toggleAssistant);

        } catch (ChunkyUnregisteredException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
