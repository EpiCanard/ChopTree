package net.gkid117.choptree;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public enum BlockFaceEnum {

  CURRENT(null, null),

  N(BlockFace.NORTH, null),
  E(BlockFace.EAST, null),
  S(BlockFace.SOUTH, null),
  W(BlockFace.WEST, null),
  NE(BlockFace.NORTH_EAST, null),
  NW(BlockFace.NORTH_WEST, null),
  SE(BlockFace.SOUTH_EAST, null),
  SW(BlockFace.SOUTH_WEST, null),

  UP(BlockFace.UP, null),

  UP_N(BlockFace.UP, BlockFace.NORTH),
  UP_E(BlockFace.UP, BlockFace.EAST),
  UP_S(BlockFace.UP, BlockFace.SOUTH),
  UP_W(BlockFace.UP, BlockFace.WEST),
  UP_NE(BlockFace.UP, BlockFace.NORTH_EAST),
  UP_NW(BlockFace.UP, BlockFace.NORTH_WEST),
  UP_SE(BlockFace.UP, BlockFace.SOUTH_EAST),
  UP_SW(BlockFace.UP, BlockFace.SOUTH_WEST),

  DOWN(BlockFace.DOWN, null),
  ;

  private BlockFace faceA;
  private BlockFace faceB;

  BlockFaceEnum(BlockFace firstFace, BlockFace secondFace) {
    this.faceA = firstFace;
    this.faceB = secondFace;
  }

  /**
   * Get the block at the specific position from the block
   * If ENUM value is UP it will get the block of the block above the block sent in parameter
   * 
   * @param Block Start block position
   * @return Block at the specific position from the block
   */
  public Block getBlock(Block block) {
    if (faceA != null)
      block = block.getRelative(this.faceA);
    if (faceB != null)
      block = block.getRelative(this.faceB);
    return block;
  }

  /**
   * Get the material at the specific position from the block
   * If ENUM value is UP it will get the material of the block above the block sent in parameter
   * 
   * @param Block Start block position
   * @return Material at the specific position from the block
   */
  public Material getMaterial(Block block) {
    Block bl = this.getBlock(block);
    return bl.getType();
  }

  /**
   * Define if the block at the specified face is a wood log block
   * If ENUM value is UP it will define if the block above the block sent in parameter is a wodd log
   * 
   * @param Block start block position
   * @return Boolean to define if it's a wood log
   */
  public Boolean isWood(Block block, ChopTree plugin) {
    return plugin.allowedWoodBlocks.contains(this.getMaterial(block).name());
  }

  /**
   * Define if the block at the specified face is a leaf
   * If ENUM value is UP it will define if the block above the block sent in parameter is a leaf
   * 
   * @param Block start block position
   * @return Boolean to define if it's a leaf
   */
  public Boolean isLeaf(Block block, ChopTree plugin) {
    return plugin.leavesForDecay.contains(this.getMaterial(block).name());
  }

 /**
   * Define if the block at the specified face is a leaf or a wood
   * If ENUM value is UP it will define if the block above the block sent in parameter is a leaf or wood
   * 
   * @param Block start block position
   * @return Boolean to define if it's a leaf or wood
   */
  public Boolean isLeafOrWood(Block block, ChopTree plugin) {
    return this.isLeaf(block, plugin) || this.isWood(block, plugin);
  }

  /**
   * Define if the block at the specified face is the same as material in parameter
   * If ENUM value is UP it will define if the block above the block sent in parameter
   * is the same as material in parameter
   * 
   * @param Block start block position
   * @return Boolean to define if it's the same as in parameter
   */
  public Boolean is(Block block, Material material) {
    return this.getMaterial(block).equals(material);
  }

  /**
   * Get all the BlockFaceEnum value with first face (faceA) is equal to the param
   * 
   * @param BlockFace value of faceA to compare
   * @return List of BlockFaceEnum that has faceA == face
   */
  public static List<BlockFaceEnum> getValuesWithFirstFace(BlockFace face) {
    final List<BlockFaceEnum> blockFaces = Arrays.asList(BlockFaceEnum.values());
    return blockFaces.stream().filter(value -> value.faceA == face).collect(Collectors.toList());    
  }
}