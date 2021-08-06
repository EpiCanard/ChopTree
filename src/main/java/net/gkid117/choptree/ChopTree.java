package net.gkid117.choptree;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
  protected List<String> allowedWoodBlocks;
  protected List<String> leavesForDecay;
  private File playerFile;
  protected FileConfiguration playersDb;
  // Contants.yml
  protected List<String> tools;
  protected List<String> axes;
  
  
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
        sender.sendMessage(ChatColor.GOLD + "ChopTree v" + getDescription().getVersion() + " : By Gkid117 and EpiCanard");
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
        sender.sendMessage(ChatColor.GOLD + "AllowedWoodBlocks : " + ChatColor.GRAY + StringUtils.join(allowedWoodBlocks, ","));
        sender.sendMessage(ChatColor.GOLD + "LeavesForDecay : " + ChatColor.GRAY + StringUtils.join(leavesForDecay, ","));
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
        if (args[1].equalsIgnoreCase("ActiveByDefault")){
          defaultActive = !defaultActive;
          config.set("ActiveByDefault", defaultActive);
          sender.sendMessage(ChatColor.GOLD + "ActiveByDefault set to : " + ChatColor.GRAY + defaultActive);
        }
        else if (args[1].equalsIgnoreCase("UseAnything"))
        {
          useAnything = !useAnything;
          config.set("useAnything", useAnything);
          sender.sendMessage(ChatColor.GOLD + "UseAnything set to : " + ChatColor.GRAY + useAnything);
        }
        else if (args[1].equalsIgnoreCase("MoreDamageToTools"))
        {
          moreDamageToTools = !moreDamageToTools;
          config.set("MoreDamageToTools", moreDamageToTools);
          sender.sendMessage(ChatColor.GOLD + "MoreDamageToTools set to : " + ChatColor.GRAY + moreDamageToTools);
        }
        else if (args[1].equalsIgnoreCase("InterruptIfToolBreaks"))
        {
          interruptIfToolBreaks = !interruptIfToolBreaks;
          config.set("InterruptIfToolBreaks", interruptIfToolBreaks);
          sender.sendMessage(ChatColor.GOLD + "InterruptIfToolBreaks set to : " + ChatColor.GRAY + interruptIfToolBreaks);
        }
        else if (args[1].equalsIgnoreCase("LogsMoveDown"))
        {
          logsMoveDown = !logsMoveDown;
          config.set("LogsMoveDown", logsMoveDown);
          sender.sendMessage(ChatColor.GOLD + "LogsMoveDown set to : " + ChatColor.GRAY + logsMoveDown);
        }
        else if (args[1].equalsIgnoreCase("OnlyTrees"))
        {
          onlyTrees = !onlyTrees;
          config.set("OnlyTrees", onlyTrees);
          sender.sendMessage(ChatColor.GOLD + "OnlyTrees set to : " + ChatColor.GRAY + onlyTrees);
        }
        else if (args[1].equalsIgnoreCase("PopLeaves"))
        {
          popLeaves = !popLeaves;
          config.set("PopLeaves", popLeaves);
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

    // LOAD CONSTANTS
    YamlConfiguration constants = YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("constants.yml")));
    tools = constants.getStringList("Tools");
    axes = constants.getStringList("Axes");

    reloadConfig();
    config = getConfig();
    defaultActive = config.getBoolean("ActiveByDefault", true);
    config.set("ActiveByDefault", defaultActive);
    useAnything = config.getBoolean("UseAnything", false);
    config.set("UseAnything", useAnything);
    allowedTools = config.getStringList("AllowedTools").toArray(new String[0]);
    config.set("AllowedTools", arrayToString(allowedTools, ","));
    moreDamageToTools = config.getBoolean("MoreDamageToTools", false);
    config.set("MoreDamageToTools", moreDamageToTools);
    interruptIfToolBreaks = config.getBoolean("InterruptIfToolBreaks", false);
    config.set("InterruptIfToolBreaks", interruptIfToolBreaks);
    logsMoveDown = config.getBoolean("LogsMoveDown", false);
    config.set("LogsMoveDown", logsMoveDown);
    onlyTrees = config.getBoolean("OnlyTrees", true);
    config.set("OnlyTrees", onlyTrees);
    popLeaves = config.getBoolean("PopLeaves", false);
    config.set("PopLeaves", popLeaves);
    leafRadius = config.getInt("LeafRadius", 3);
    config.set("LeafRadius", leafRadius);

    allowedWoodBlocks = filterConfigParams("AllowedWoodBlocks", Arrays.asList("LOG", "STEM"));
    leavesForDecay = filterConfigParams("LeavesForDecay", Arrays.asList("LEAVES", "WART_BLOCK"));

    saveConfig();
  }

  private List<String> filterConfigParams(String configVar, List<String> contain) {
    final List<String> input = config.getStringList(configVar);
    final List<String> notAllowed = new ArrayList<>();
    final List<String> output = input.stream().filter(block -> {
      final boolean ret = contain.stream().anyMatch(block::contains);
      if (!ret)
        notAllowed.add(block);
      return ret;
    }).collect(Collectors.toList());
    
    if(!notAllowed.isEmpty()) {
      getLogger().warning("[CONFIG] Those following " + configVar + " are not " + contain + " blocks : " + StringUtils.join(notAllowed, ','));
      getLogger().warning("[CONFIG] Config has been rewritten with only those blocks : " + configVar + " = " + StringUtils.join(output, ","));      
    }

    config.set(configVar, output);
    
    return output;
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
