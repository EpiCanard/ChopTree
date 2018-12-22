package net.gkid117.choptree;

import org.bukkit.configuration.file.FileConfiguration;

public class ChopTreePlayer
{
  private static ChopTree plugin;
  private boolean active;
  private String playerName;
  
  public ChopTreePlayer(ChopTree instance, String playerName)
  {
    plugin = instance;
    this.playerName = playerName;
    active = getSetting("active");
    if (plugin.playersDb.get(playerName + ".active") == null)
    {
      addPlayer();
      active = plugin.defaultActive;
    }
  }
  
  public boolean isActive()
  {
    return active;
  }
  
  public void setActive(boolean setting)
  {
    active = setting;
    plugin.playersDb.set(playerName + ".active", Boolean.valueOf(setting));
    plugin.savePlayers();
  }
  
  public void toggleActive()
  {
    if (active) {
      active = false;
    } else {
      active = true;
    }
    plugin.playersDb.set(playerName + ".active", Boolean.valueOf(active));
    plugin.savePlayers();
  }
  
  private boolean getSetting(String setting)
  {
    boolean value = false;
    if (setting.equalsIgnoreCase("active")) {
      value = plugin.playersDb.getBoolean(playerName + "." + setting, plugin.defaultActive);
    }
    return value;
  }
  
  private void addPlayer()
  {
    plugin.playersDb.set(playerName + ".active", Boolean.valueOf(plugin.defaultActive));
    plugin.savePlayers();
  }
}
