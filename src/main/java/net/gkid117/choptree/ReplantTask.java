package net.gkid117.choptree;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ReplantTask
  implements Runnable
{
  public Block block;
  public int logtype = 0;
  
  public ReplantTask(Block log)
  {
    block = log;
    switch (log.getType()) {
    case BED_BLOCK: 
      logtype = (log.getData() % 4);
      break;
    case GHAST_TEAR: 
      logtype = (log.getData() % 4 + 4);
      break;
    }
    
  }
  


  public void run()
  {
    if (block.getType() == Material.AIR) {
      switch (block.getRelative(org.bukkit.block.BlockFace.DOWN).getType()) {
      case ACACIA_FENCE: 
      case ACACIA_FENCE_GATE: 
      case DIAMOND_HOE: 
        block.setType(Material.SAPLING);
        block.setData((byte)logtype);
        break;
      }
    }
  }
}
