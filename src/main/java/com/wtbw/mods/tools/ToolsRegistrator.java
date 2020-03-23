package com.wtbw.mods.tools;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.item.BaseBlockItem;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.tools.blocks.GreenHouseGlassBlock;
import com.wtbw.mods.tools.blocks.ModBlocks;
import com.wtbw.mods.tools.config.CommonConfig;
import com.wtbw.mods.tools.event.ArmourEvents;
import com.wtbw.mods.tools.item.armour.CobaltArmour;
import com.wtbw.mods.tools.item.armour.util.ArmourFlightManager;
import com.wtbw.mods.tools.item.armour.util.FullAmourSetHandler;
import com.wtbw.mods.tools.item.armour.ShulkerArmour;
import com.wtbw.mods.tools.item.tools.*;
import com.wtbw.mods.tools.tile.MagnetInhibitorTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;

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
    register(new GreenHouseGlassBlock(getBlockProperties(Material.GLASS, 1).variableOpacity()), "greenhouse_glass");
  }
  
  @Override
  protected void registerAllItems()
  {
    register(new BaseBlockItem(ModBlocks.MAGNET_INHIBITOR,
      getItemProperties().addTooltip(TextComponentBuilder.createTranslated(WTBWTools.MODID + ".tooltip.magnet_inhibitor", 3).aqua().build())),
      "magnet_inhibitor");
    
    // copper tools/armour
    registerArmourSet(ItemTierExt.COPPER);
    registerTools(ItemTierExt.COPPER, "copper", -2.4f, 3, 1);
    
    // cobalt armour/tools
    ArmorMaterialExt cobaltArmorMaterial = (ArmorMaterialExt) ArmorMaterialExt.get(ItemTierExt.COBALT);
    register(new CobaltArmour(cobaltArmorMaterial, EquipmentSlotType.HEAD, getItemProperties()), "cobalt_helmet");
    register(new CobaltArmour(cobaltArmorMaterial, EquipmentSlotType.CHEST, getItemProperties()), "cobalt_chestplate");
    register(new CobaltArmour(cobaltArmorMaterial, EquipmentSlotType.LEGS, getItemProperties()), "cobalt_leggings");
    register(new CobaltArmour(cobaltArmorMaterial, EquipmentSlotType.FEET, getItemProperties()), "cobalt_boots");
    registerTools(ItemTierExt.COBALT, "cobalt", -1.8f, 5, 3);
  
    ArmourEvents.registerHandler(new FullAmourSetHandler(CobaltArmour::hasFullSet, CobaltArmour::applyEffect));
    
    ArmorMaterialExt shulkerArmorMaterial = (ArmorMaterialExt) ArmorMaterialExt.get(ItemTierExt.SHULKER);
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.HEAD, getItemProperties()), "shulker_helmet");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.CHEST, getItemProperties()), "shulker_chestplate");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.LEGS, getItemProperties()), "shulker_leggings");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.FEET, getItemProperties()), "shulker_boots");

    ArmourEvents.registerHandler(new FullAmourSetHandler(ShulkerArmour::hasFullSet, ShulkerArmour::applyEffect, ArmourFlightManager::stopFlight));
  
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
    
    register(new SlimeChunkFinderItem(getItemProperties().maxStackSize(1).maxDamage(25)), "slime_chunk_finder");
  }
  
  private void registerTools(ItemTierExt tier, String name, float attackSpeed, float bonusAxeAttack, float bonusSwordAttack)
  {
    register(new PickaxeItem(tier, 1, attackSpeed, getItemProperties()), name + "_pickaxe");
    register(new ShovelItem(tier, 1, attackSpeed, getItemProperties()), name + "_shovel");
    register(new AxeItem(tier, tier.getAttackDamage() + bonusAxeAttack, attackSpeed, getItemProperties()), name + "_axe");
    register(new SwordItem(tier, (int) (tier.getAttackDamage() + bonusSwordAttack), attackSpeed, getItemProperties()), name + "_sword");
  }
  
  private void registerArmourSet(IItemTier tier)
  {
    ArmorMaterialExt material = (ArmorMaterialExt) ArmorMaterialExt.get(tier);
    String name = material.getMaterialName();
    
    register(new ArmorItem(material, EquipmentSlotType.HEAD, getItemProperties()), name + "_helmet");
    register(new ArmorItem(material, EquipmentSlotType.CHEST, getItemProperties()), name + "_chestplate");
    register(new ArmorItem(material, EquipmentSlotType.LEGS, getItemProperties()), name + "_leggings");
    register(new ArmorItem(material, EquipmentSlotType.FEET, getItemProperties()), name + "_boots");
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
