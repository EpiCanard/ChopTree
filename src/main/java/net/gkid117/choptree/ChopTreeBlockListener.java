package net.gkid117.choptree;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ChopTreeBlockListener implements Listener {
  public static Player pubplayer = null;
  public static ChopTree plugin;
  private int leafRadius;
  
  public ChopTreeBlockListener(ChopTree instance) {
    plugin = instance;
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }
    Block block = event.getBlock();
    if (BlockFaceEnum.CURRENT.isWood(block, plugin)){
      if (denyPermission(event.getPlayer())) {
        return;
      }
      if (denyActive(event.getPlayer())) {
        return;
      }
      if (denyItem(event.getPlayer())) {
        return;
      }
      event.setCancelled(true);
      if (chopTheTree(event.getBlock(), event.getPlayer(), event.getBlock().getWorld())) {
        if ((!plugin.moreDamageToTools) && 
          (breaksTool(event.getPlayer(), event.getPlayer().getItemInHand()))) {
          event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
        }
      } else {
        event.setCancelled(false);
      }
    }
  }
  
  public boolean chopTheTree(Block block, Player player, World world) {
    List<Block> blocks = new LinkedList<Block>();
    Block highest = getHighestLog(block);
    if (isTree(highest, player, block)) {
      getBlocksToChop(block, highest, blocks);
      if (plugin.logsMoveDown) {
        moveDownLogs(block, blocks, world, player);
      } else {
        popLogs(block, blocks, world, player);
      }
    } else {
      return false;
    }
    return true;
  }
  
  public void getBlocksToChop(Block block, Block highest, List<Block> blocks)
  {
    while (block.getY() <= highest.getY()) {
      if (!blocks.contains(block)) {
        blocks.add(block);
      }
      getBranches(block, blocks, block.getRelative(BlockFace.NORTH));
      getBranches(block, blocks, block.getRelative(BlockFace.NORTH_EAST));
      getBranches(block, blocks, block.getRelative(BlockFace.EAST));
      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_EAST));
      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH));
      getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_WEST));
      getBranches(block, blocks, block.getRelative(BlockFace.WEST));
      getBranches(block, blocks, block.getRelative(BlockFace.NORTH_WEST));
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
      }
      if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST))) {
        getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST));
      }
      if ((block.getData() == 3) || (block.getData() == 7) || (block.getData() == 11) || (block.getData() == 15)) {
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2));
        }
        if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2))) {
          getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2));
        }
      }
      if (blocks.contains(block.getRelative(BlockFace.UP)) || !BlockFaceEnum.UP.isWood(block, plugin)) {
        break;
      }
      block = block.getRelative(BlockFace.UP);
    }
  }
  
  public void getBranches(Block block, List<Block> blocks, Block other) {
    if (!blocks.contains(other) && BlockFaceEnum.CURRENT.isWood(other, plugin)) {
      getBlocksToChop(other, getHighestLog(other), blocks);
    }
  }
  
  public Block getHighestLog(Block block) {
    Block newBlock = null;
    boolean isLog = true;
    final List<BlockFaceEnum> enumsFaces = BlockFaceEnum.getValuesWithFirstFace(BlockFace.UP);

    while (isLog) {
      for (BlockFaceEnum face : enumsFaces) {
        if (face.isWood(block, plugin)) {
          newBlock = face.getBlock(block);
          break;
        }
      }
      if (newBlock == null) {
        isLog = false;
      } else {
        block = newBlock;
        newBlock = null;
      }
    }
    return block;
  }
  
  public boolean isTree(Block block, Player player, Block first) {
    if (!plugin.onlyTrees) {
      return true;
    }
    if (plugin.trees.containsKey(player))
    {
      Block[] blockarray = (Block[])plugin.trees.get(player);
      for (int counter = 0; counter < Array.getLength(blockarray); counter++)
      {
        if (blockarray[counter] == block) {
          return true;
        }
        if (blockarray[counter] == first) {
          return true;
        }
      }
    }
    int counter = 0;
    if (BlockFaceEnum.UP.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.UP.isLeaf(block.getRelative(BlockFace.UP), plugin))
      counter++;
    if (BlockFaceEnum.UP_N.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.UP_S.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.UP_E.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.UP_W.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.DOWN.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.N.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.E.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.S.isLeaf(block, plugin))
      counter++;
    if (BlockFaceEnum.W.isLeaf(block, plugin))
      counter++;

    if (counter >= 2) {
      return true;
    }
    if (block.getData() == 1) {
      block = block.getRelative(BlockFace.UP);
      if (BlockFaceEnum.UP.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.UP_N.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.UP_S.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.UP_E.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.UP_W.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.N.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.E.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.S.isLeaf(block, plugin))
        counter++;
      if (BlockFaceEnum.W.isLeaf(block, plugin))
        counter++;
      if (counter >= 2) {
        return true;
      }
    }
    return false;
  }
  
  public void popLogs(Block bl, List<Block> blocks, World world, Player player)
  {
    for (Block block : blocks) {
      block.breakNaturally();
      if (plugin.popLeaves) {
        popLeaves(block);
      }
      if ((plugin.moreDamageToTools) && 
        (breaksTool(player, player.getItemInHand())))
      {
        player.getInventory().clear(player.getInventory().getHeldItemSlot());
        if (plugin.interruptIfToolBreaks) {
          break;
        }
      }
    }
  }
  
  public void popLeaves(Block block)
  {
    for (int y = -plugin.leafRadius; y < plugin.leafRadius + 1; y++) {
      for (int x = -plugin.leafRadius; x < plugin.leafRadius + 1; x++) {
        for (int z = -plugin.leafRadius; z < plugin.leafRadius + 1; z++) {
          Block target = block.getRelative(x, y, z);
          if (BlockFaceEnum.CURRENT.isLeaf(target, plugin)) {
            target.breakNaturally();
          }
        }
      }
    }
  }
  
  public void moveDownLogs(Block block, List<Block> blocks, World world, Player player)
  {
    ItemStack item = new ItemStack(Material.STONE, 1, (short)0, null);
    item.setAmount(1);
    
    List<Block> downs = new LinkedList<Block>();
    for (int counter = 0; counter < blocks.size(); counter++)
    {
      block = (Block)blocks.get(counter);
      Block down = block.getRelative(BlockFace.DOWN);
      if ((down.getType() == Material.AIR) || BlockFaceEnum.CURRENT.isLeaf(down, plugin)) {
        down.setType(block.getType());
        block.setType(Material.AIR);
        downs.add(down);
      }
      else
      {
        item.setType(block.getType());
        item.setDurability((short)block.getData());
        block.setType(Material.AIR);
        world.dropItem(block.getLocation(), item);
        if ((plugin.moreDamageToTools) && 
          (breaksTool(player, player.getItemInHand()))) {
          player.getInventory().clear(player.getInventory().getHeldItemSlot());
        }
      }
    }
    for (int counter = 0; counter < downs.size(); counter++)
    {
      block = (Block)downs.get(counter);
      if (isLoneLog(block))
      {
        downs.remove(block);
        block.breakNaturally();
        if ((plugin.moreDamageToTools) && 
          (breaksTool(player, player.getItemInHand()))) {
          player.getInventory().clear(player.getInventory().getHeldItemSlot());
        }
      }
    }
    if (plugin.popLeaves) {
      moveLeavesDown(blocks);
    }
    if (plugin.trees.containsKey(player)) {
      plugin.trees.remove(player);
    }
    if (downs.isEmpty()) {
      return;
    }
    Block[] blockarray = new Block[downs.size()];
    for (int counter = 0; counter < downs.size(); counter++) {
      blockarray[counter] = ((Block)downs.get(counter));
    }
    plugin.trees.put(player, blockarray);
  }
  
  public void moveLeavesDown(List<Block> blocks)
  {
    ChopTreeBlockListener plugin = this;
    List<Block> leaves = new LinkedList<Block>();
    int y = 0;
    Iterator<Block> blockIterator = blocks.iterator();
    Block tempBlock;
    int z;
    while ((blockIterator.hasNext()) && (y < leafRadius + 1))
    {
      Block block = (Block)blockIterator.next();
      y = -leafRadius;
      for (int x = -leafRadius; x < leafRadius + 1; x++) {
        for (z = -leafRadius; z < leafRadius + 1; z++) {
          tempBlock = block.getRelative(x, y, z);
          if (BlockFaceEnum.CURRENT.isLeaf(tempBlock, ChopTreeBlockListener.plugin) && !leaves.contains(tempBlock)) {
            leaves.add(block.getRelative(x, y, z));
          }
        }
      }
      y++;
    }
    for (Block block : leaves) {
      Block downBlock = block.getRelative(BlockFace.DOWN);
      if ((BlockFaceEnum.CURRENT.is(downBlock, Material.AIR) || BlockFaceEnum.CURRENT.isLeaf(downBlock, ChopTreeBlockListener.plugin)) &&
          (BlockFaceEnum.DOWN.is(downBlock, Material.AIR) || BlockFaceEnum.DOWN.isLeafOrWood(downBlock, ChopTreeBlockListener.plugin)) &&
          (BlockFaceEnum.DOWN.is(downBlock.getRelative(BlockFace.DOWN), Material.AIR) || BlockFaceEnum.DOWN.isLeafOrWood(downBlock.getRelative(BlockFace.DOWN), ChopTreeBlockListener.plugin)))
      {
        block.getRelative(BlockFace.DOWN).setType(block.getType());
        block.setType(Material.AIR);
      }
      else
      {
        block.breakNaturally();
      }
    }
  }
  
  public boolean breaksTool(Player player, ItemStack item)
  {
    if ((item != null) && 
      (isTool(item.getType())))
    {
      short damage = item.getDurability();
      if (isAxe(item.getType())) {
        damage = (short)(damage + 1);
      } else {
        damage = (short)(damage + 2);
      }
      if (damage >= item.getType().getMaxDurability()) {
        return true;
      }
      item.setDurability(damage);
    }
    return false;
  }
  
  public boolean isTool(Material mat)
  {
    return plugin.tools.contains(mat.name());
  }
  
  public boolean isAxe(Material mat)
  {
    return plugin.axes.contains(mat.name());
  }
  
  public boolean isLoneLog(Block block)
  {
    if (BlockFaceEnum.UP.isWood(block, plugin)) {
      return false;
    }
    if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
      return false;
    }
    if (hasHorizontalCompany(block)) {
      return false;
    }
    if (hasHorizontalCompany(block.getRelative(BlockFace.UP))) {
      return false;
    }
    if (hasHorizontalCompany(block.getRelative(BlockFace.DOWN))) {
      return false;
    }
    return true;
  }
  
  public boolean hasHorizontalCompany(Block block)
  {
    if (BlockFaceEnum.N.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.NE.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.E.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.SE.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.S.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.SW.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.W.isWood(block, plugin))
      return true;
    if (BlockFaceEnum.NW.isWood(block, plugin))
      return true;
    return false;
  }
  
  public boolean denyPermission(Player player)
  {
    return (!player.hasPermission("choptree.chop"));
  }
  
  public boolean denyActive(Player player)
  {
    ChopTreePlayer ctPlayer = new ChopTreePlayer(plugin, player.getName());
    return (!ctPlayer.isActive());
  }
  
  public boolean denyItem(Player player)
  {
    if (!plugin.useAnything)
    {
      ItemStack item = player.getItemInHand();
      if (item != null)
      {
        String[] arrayOfString;
        int j = (arrayOfString = plugin.allowedTools).length;
        for (int i = 0; i < j; i++)
        {
          String tool = arrayOfString[i];
          if (tool.equalsIgnoreCase(item.getType().name())) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }
  
  public boolean interruptWhenBreak(Player player)
  {
    if (plugin.interruptIfToolBreaks) {
      return true;
    }
    return false;
  }
}
