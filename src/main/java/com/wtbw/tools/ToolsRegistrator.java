package com.wtbw.tools;

import com.wtbw.lib.Registrator;
import com.wtbw.lib.block.BaseTileBlock;
import com.wtbw.lib.item.BaseBlockItem;
import com.wtbw.lib.util.TextComponentBuilder;
import com.wtbw.tools.blocks.GreenHouseGlassBlock;
import com.wtbw.tools.blocks.ModBlocks;
import com.wtbw.tools.config.CommonConfig;
import com.wtbw.tools.item.tools.*;
import com.wtbw.tools.tile.MagnetInhibitorTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;

/*
  @author: Naxanria
*/
public class ToolsRegistrator extends Registrator
{
  public ToolsRegistrator(ItemGroup group, String modid)
  {
    super(group, modid);
  }
  
  @Override
  protected void registerAllBlocks()
  {
    register(new BaseTileBlock<>(getBlockProperties(Material.IRON, 4), (world, state) -> new MagnetInhibitorTileEntity()), "magnet_inhibitor", false);
    register(new GreenHouseGlassBlock(getBlockProperties(Material.GLASS, 1).func_226896_b_()), "greenhouse_glass");
  }
  
  @Override
  protected void registerAllItems()
  {
    register(new BaseBlockItem(ModBlocks.MAGNET_INHIBITOR,
      getItemProperties().addTooltip(TextComponentBuilder.createTranslated(WTBWTools.MODID + ".tooltip.magnet_inhibitor", 3).aqua().build())),
      "magnet_inhibitor");
  
    int dMul = CommonConfig.instance().toolsDurabilityMultiplier.get();
    register(new HammerItem(ItemTier.STONE, 6, -3.6f, getItemProperties().maxDamage(Items.STONE_PICKAXE.getMaxDamage(null) * dMul)), "stone_hammer");
    register(new HammerItem(ItemTier.IRON, 8, -3.6f, getItemProperties().maxDamage(Items.IRON_PICKAXE.getMaxDamage(null) * dMul)), "iron_hammer");
    register(new HammerItem(ItemTier.GOLD, 6, -3.2f, getItemProperties().maxDamage(Items.GOLDEN_PICKAXE.getMaxDamage(null) * dMul)), "gold_hammer");
    register(new HammerItem(ItemTier.DIAMOND, 11, -3.6f, getItemProperties().maxDamage(Items.DIAMOND_PICKAXE.getMaxDamage(null) * dMul)), "diamond_hammer");
  
    register(new GreatAxeItem(ItemTier.STONE, 6, -2.8f, getItemProperties().maxDamage(Items.STONE_AXE.getMaxDamage(null) * dMul)), "stone_greataxe");
    register(new GreatAxeItem(ItemTier.IRON, 7, -2.8f, getItemProperties().maxDamage(Items.IRON_AXE.getMaxDamage(null) * dMul)), "iron_greataxe");
    register(new GreatAxeItem(ItemTier.GOLD, 7, -2.8f, getItemProperties().maxDamage(Items.GOLDEN_AXE.getMaxDamage(null) * dMul)), "gold_greataxe");
    register(new GreatAxeItem(ItemTier.DIAMOND, 8, -2.8f, getItemProperties().maxDamage(Items.DIAMOND_AXE.getMaxDamage(null) * dMul)), "diamond_greataxe");
  
    register(new ExcavatorItem(ItemTier.STONE, 4, -2.5f, getItemProperties().maxDamage(Items.STONE_SHOVEL.getMaxDamage(null) * dMul)), "stone_excavator");
    register(new ExcavatorItem(ItemTier.IRON, 5, -2.5f, getItemProperties().maxDamage(Items.IRON_SHOVEL.getMaxDamage(null) * dMul)), "iron_excavator");
    register(new ExcavatorItem(ItemTier.GOLD, 6, -2.5f, getItemProperties().maxDamage(Items.GOLDEN_SHOVEL.getMaxDamage(null) * dMul)), "gold_excavator");
    register(new ExcavatorItem(ItemTier.DIAMOND, 6, -2.5f, getItemProperties().maxDamage(Items.DIAMOND_SHOVEL.getMaxDamage(null) * dMul)), "diamond_excavator");
  
    register(new Trowel(getItemProperties()), "trowel");
  
    register(new SwapTool(ItemTier.STONE, getItemProperties()), "stone_swap_tool");
    register(new SwapTool(ItemTier.IRON, getItemProperties()), "iron_swap_tool");
    register(new SwapTool(ItemTier.DIAMOND, getItemProperties()), "diamond_swap_tool");
  
    register(new MagnetItem(getItemProperties().maxStackSize(1)), "magnet");
  
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.BASIC), "watering_can");
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.QUARTZ), "watering_can_quartz");
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.DIAMOND), "watering_can_diamond");
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.ENDER), "watering_can_ender");
  }
  
  @Override
  protected void registerAllTiles()
  {
    register(ModBlocks.MAGNET_INHIBITOR);
  }
  
  @Override
  protected void registerAllContainers()
  { }
}
