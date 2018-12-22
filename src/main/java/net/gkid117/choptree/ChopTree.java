package net.gkid117.choptree;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChopTree
  extends JavaPlugin
{
  private final ChopTreeBlockListener blockListener = new ChopTreeBlockListener(this);
  public final HashMap<Player, Block[]> trees = new HashMap<Player, Block[]>();
  private FileConfiguration config;
  protected boolean defaultActive;
  protected boolean useAnything;
  protected boolean moreDamageToTools;
  protected boolean interruptIfToolBreaks;
  protected boolean logsMoveDown;
  protected boolean onlyTrees;
  protected boolean popLeaves;
  protected int leafRadius;
  protected String[] allowedTools;
  private File playerFile;
  protected FileConfiguration playersDb;
  
  
  public ChopTree() {}
  
  public void onEnable() {
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(blockListener, this);
    loadConfig();
    playersDb = getPlayers();
  }
  
  public void onDisable() {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    if ((commandLabel.equalsIgnoreCase("ChopTree")) || (commandLabel.equalsIgnoreCase("ct")))
    {
      if (Array.getLength(args) == 0)
      {
        if (!sender.hasPermission("choptree.commands.choptree.info"))
        {
          sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
          return false;
        }
        sender.sendMessage(ChatColor.GOLD + "ChopTree v4.0.1" + getDescription().getVersion() + " : By Gkid117");
        sender.sendMessage(ChatColor.GRAY + "===================================");
        sender.sendMessage(ChatColor.GOLD + "ActiveByDefault : " + ChatColor.GRAY + defaultActive);
        sender.sendMessage(ChatColor.GOLD + "UseAnything : " + ChatColor.GRAY + useAnything);
        sender.sendMessage(ChatColor.GOLD + "MoreDamageToTools : " + ChatColor.GRAY + moreDamageToTools);
        sender.sendMessage(ChatColor.GOLD + "InterruptIfToolBreaks : " + ChatColor.GRAY + interruptIfToolBreaks);
        sender.sendMessage(ChatColor.GOLD + "LogsMoveDown : " + ChatColor.GRAY + logsMoveDown);
        sender.sendMessage(ChatColor.GOLD + "OnlyTrees : " + ChatColor.GRAY + onlyTrees);
        if (useAnything) {
          sender.sendMessage(ChatColor.GOLD + "AllowedTools : " + ChatColor.GRAY + "ANYTHING!");
        } else {
          sender.sendMessage(ChatColor.GOLD + "AllowedTools : " + ChatColor.GRAY + arrayToString(allowedTools, ","));
        }
        sender.sendMessage(ChatColor.GOLD + "PopLeaves : " + ChatColor.GRAY + popLeaves);
      }
      else if ((args[0].equalsIgnoreCase("reload")) || (args[0].equalsIgnoreCase("r")))
      {
        if (!sender.hasPermission("choptree.commands.choptree.reload"))
        {
          sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
          return false;
        }
        loadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reloaded settings from properties file.");
      }
      else if (args[0].equalsIgnoreCase("toggle"))
      {
        if (args.length == 1)
        {
          sender.sendMessage(ChatColor.RED + "You must specify an option to toggle!");
          sender.sendMessage(ChatColor.GRAY + "(ActiveByDefault|UseAnything|MoreDamageToTools|InterruptIfToolBreaks|LogsMoveDown|OnlyTrees|PopLeaves)");
          return false;
        }
        if (!sender.hasPermission("choptree.commands.choptree.toggle." + args[1].toLowerCase()))
        {
          sender.sendMessage(ChatColor.RED + "You do not have permission to toggle that setting!");
          return false;
        }
        if (args[1].equalsIgnoreCase("ActiveByDefault"))
        {
          if (defaultActive)
          {
            defaultActive = false;
            config.set("ActiveByDefault", Boolean.valueOf(false));
          }
          else
          {
            defaultActive = true;
            config.set("ActiveByDefault", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "ActiveByDefault set to : " + ChatColor.GRAY + defaultActive);
        }
        else if (args[1].equalsIgnoreCase("UseAnything"))
        {
          if (useAnything)
          {
            useAnything = false;
            config.set("UseAnything", Boolean.valueOf(false));
          }
          else
          {
            useAnything = true;
            config.set("UseAnything", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "UseAnything set to : " + ChatColor.GRAY + useAnything);
        }
        else if (args[1].equalsIgnoreCase("MoreDamageToTools"))
        {
          if (moreDamageToTools)
          {
            moreDamageToTools = false;
            config.set("MoreDamageToTools", Boolean.valueOf(false));
          }
          else
          {
            moreDamageToTools = true;
            config.set("MoreDamageToTools", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "MoreDamageToTools set to : " + ChatColor.GRAY + moreDamageToTools);
        }
        else if (args[1].equalsIgnoreCase("InterruptIfToolBreaks"))
        {
          if (interruptIfToolBreaks)
          {
            interruptIfToolBreaks = false;
            config.set("InterruptIfToolBreaks", Boolean.valueOf(false));
          }
          else
          {
            interruptIfToolBreaks = true;
            config.set("InterruptIfToolBreaks", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "InterruptIfToolBreaks set to : " + ChatColor.GRAY + interruptIfToolBreaks);
        }
        else if (args[1].equalsIgnoreCase("LogsMoveDown"))
        {
          if (logsMoveDown)
          {
            logsMoveDown = false;
            config.set("LogsMoveDown", Boolean.valueOf(false));
          }
          else
          {
            logsMoveDown = true;
            config.set("LogsMoveDown", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "LogsMoveDown set to : " + ChatColor.GRAY + logsMoveDown);
        }
        else if (args[1].equalsIgnoreCase("OnlyTrees"))
        {
          if (onlyTrees)
          {
            onlyTrees = false;
            config.set("OnlyTrees", Boolean.valueOf(false));
          }
          else
          {
            onlyTrees = true;
            config.set("OnlyTrees", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "OnlyTrees set to : " + ChatColor.GRAY + onlyTrees);
        }
        else if (args[1].equalsIgnoreCase("PopLeaves"))
        {
          if (popLeaves)
          {
            popLeaves = false;
            config.set("PopLeaves", Boolean.valueOf(false));
          }
          else
          {
            popLeaves = true;
            config.set("PopLeaves", Boolean.valueOf(true));
          }
          sender.sendMessage(ChatColor.GOLD + "PopLeaves set to : " + ChatColor.GRAY + popLeaves);
        }
        else
        {
          sender.sendMessage(ChatColor.RED + "I can't find a setting called " + ChatColor.WHITE + args[1] + ChatColor.RED + "!");
          sender.sendMessage(ChatColor.GRAY + "(ActiveByDefault|UseAnything|MoreDamageToTools|InterruptIfToolBreaks|LogsMoveDown|OnlyTrees|PopLeaves)");
          return true;
        }
        saveConfig();
      }
    }
    else if ((commandLabel.equalsIgnoreCase("ToggleChop")) || (commandLabel.equalsIgnoreCase("tc")))
    {
      ChopTreePlayer ctPlayer = new ChopTreePlayer(this, sender.getName());
      ctPlayer.toggleActive();
      if (ctPlayer.isActive()) {
        sender.sendMessage(ChatColor.GOLD + "Chop Tree Activated!");
      } else {
        sender.sendMessage(ChatColor.GOLD + "Chop Tree Deactivated!");
      }
    }
    return true;
  }
  
  private String arrayToString(String[] array, String separator)
  {
    String outString = "";
    String[] arrayOfString;
    int j = (arrayOfString = array).length;
    for (int i = 0; i < j; i++)
    {
      String string = arrayOfString[i];
      if (!"".equals(outString)) {
        outString = outString + separator;
      }
      outString = outString + string;
    }
    return outString;
  }
  
  public void loadConfig()
  {
    reloadConfig();
    config = getConfig();
    defaultActive = config.getBoolean("ActiveByDefault", true);
    config.set("ActiveByDefault", Boolean.valueOf(defaultActive));
    useAnything = config.getBoolean("UseAnything", false);
    config.set("UseAnything", Boolean.valueOf(useAnything));
    allowedTools = config.getString("AllowedTools", "WOOD_AXE,STONE_AXE,IRON_AXE,GOLD_AXE,DIAMOND_AXE").split(",");
    config.set("AllowedTools", arrayToString(allowedTools, ","));
    moreDamageToTools = config.getBoolean("MoreDamageToTools", false);
    config.set("MoreDamageToTools", Boolean.valueOf(moreDamageToTools));
    interruptIfToolBreaks = config.getBoolean("InterruptIfToolBreaks", false);
    config.set("InterruptIfToolBreaks", Boolean.valueOf(interruptIfToolBreaks));
    logsMoveDown = config.getBoolean("LogsMoveDown", false);
    config.set("LogsMoveDown", Boolean.valueOf(logsMoveDown));
    onlyTrees = config.getBoolean("OnlyTrees", true);
    config.set("OnlyTrees", Boolean.valueOf(onlyTrees));
    popLeaves = config.getBoolean("PopLeaves", false);
    config.set("PopLeaves", Boolean.valueOf(popLeaves));
    leafRadius = config.getInt("LeafRadius", 3);
    config.set("LeafRadius", Integer.valueOf(leafRadius));
    saveConfig();
  }
  
  public void loadPlayers()
  {
    if (playerFile == null) {
      playerFile = new File(getDataFolder(), "players.yml");
    }
    playersDb = YamlConfiguration.loadConfiguration(playerFile);
  }
  
  public FileConfiguration getPlayers()
  {
    if (playersDb == null) {
      loadPlayers();
    }
    return playersDb;
  }
  
  public void savePlayers()
  {
    if ((playersDb == null) || (playerFile == null)) {
      return;
    }
    try
    {
      playersDb.save(playerFile);
    }
    catch (IOException ex)
    {
      String message = "Could not save " + playerFile;
      getLogger().severe(message);
    }
  }
}
