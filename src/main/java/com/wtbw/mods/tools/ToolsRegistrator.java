package com.wtbw.mods.tools;

import com.wtbw.mods.lib.Registrator;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.item.BaseBlockItem;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.tools.blocks.GreenHouseGlassBlock;
import com.wtbw.mods.tools.blocks.ModBlocks;
import com.wtbw.mods.tools.config.CommonConfig;
import com.wtbw.mods.tools.item.armour.CobaltArmour;
import com.wtbw.mods.tools.item.armour.ShulkerArmour;
import com.wtbw.mods.tools.item.tools.*;
import com.wtbw.mods.tools.tile.MagnetInhibitorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
    register(new GreenHouseGlassBlock(getBlockProperties(Material.GLASS, 1).notSolid().setSuffocates((state, world, pos) -> false)), "greenhouse_glass");
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
  
    ArmorMaterialExt shulkerArmorMaterial = (ArmorMaterialExt) ArmorMaterialExt.get(ItemTierExt.SHULKER);
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.HEAD, getItemProperties()), "shulker_helmet");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.CHEST, getItemProperties()), "shulker_chestplate");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.LEGS, getItemProperties()), "shulker_leggings");
    register(new ShulkerArmour(shulkerArmorMaterial, EquipmentSlotType.FEET, getItemProperties()), "shulker_boots");

    double dMul = CommonConfig.instance().areaToolsDurabilityMultiplier.get();
    
    int durabilityStone = (int) (ItemTier.STONE.getMaxUses() * dMul);
    int durabilityIron = (int) (ItemTier.IRON.getMaxUses() * dMul);
    int durabilityGold = (int) (ItemTier.GOLD.getMaxUses() * dMul);
    int durabilityDiamond = (int) (ItemTier.DIAMOND.getMaxUses() * dMul);
    int durabilityCopper = (int) (ItemTierExt.COPPER.getMaxUses() * dMul);
    int durabilityCobalt = (int) (ItemTierExt.COBALT.getMaxUses() * dMul);
    
    register(new HammerItem(ItemTier.STONE, 6, -3.6f, getItemProperties().maxDamage(durabilityStone)), "stone_hammer");
    register(new HammerItem(ItemTier.IRON, 8, -3.6f, getItemProperties().maxDamage(durabilityIron)), "iron_hammer");
    register(new HammerItem(ItemTier.GOLD, 6, -3.2f, getItemProperties().maxDamage(durabilityGold)), "gold_hammer");
    register(new HammerItem(ItemTier.DIAMOND, 11, -3.6f, getItemProperties().maxDamage(durabilityDiamond)), "diamond_hammer");
    register(new HammerItem(ItemTierExt.COPPER, 6, -3.6f, getItemProperties().maxDamage(durabilityCopper)), "copper_hammer");
    register(new HammerItem(ItemTierExt.COBALT, 13, -3.3f, getItemProperties().maxDamage(durabilityCobalt)), "cobalt_hammer");
    
    register(new GreatAxeItem(ItemTier.STONE, 6, -2.8f, getItemProperties().maxDamage(durabilityStone)), "stone_greataxe");
    register(new GreatAxeItem(ItemTier.IRON, 7, -2.8f, getItemProperties().maxDamage(durabilityIron)), "iron_greataxe");
    register(new GreatAxeItem(ItemTier.GOLD, 7, -2.4f, getItemProperties().maxDamage(durabilityGold)), "gold_greataxe");
    register(new GreatAxeItem(ItemTier.DIAMOND, 9, -2.8f, getItemProperties().maxDamage(durabilityDiamond)), "diamond_greataxe");
    register(new GreatAxeItem(ItemTierExt.COPPER, 6, -2.8f, getItemProperties().maxDamage(durabilityCopper)), "copper_greataxe");
    register(new GreatAxeItem(ItemTierExt.COBALT, 11, -2.6f, getItemProperties().maxDamage(durabilityCobalt)), "cobalt_greataxe");
  
    register(new ExcavatorItem(ItemTier.STONE, 4, -2.5f, getItemProperties().maxDamage(durabilityStone)), "stone_excavator");
    register(new ExcavatorItem(ItemTier.IRON, 5, -2.5f, getItemProperties().maxDamage(durabilityIron)), "iron_excavator");
    register(new ExcavatorItem(ItemTier.GOLD, 6, -2.3f, getItemProperties().maxDamage(durabilityGold)), "gold_excavator");
    register(new ExcavatorItem(ItemTier.DIAMOND, 7, -2.5f, getItemProperties().maxDamage(durabilityDiamond)), "diamond_excavator");
    register(new ExcavatorItem(ItemTierExt.COPPER, 4, -2.5f, getItemProperties().maxDamage(durabilityCopper)), "copper_excavator");
    register(new ExcavatorItem(ItemTierExt.COBALT, 9, -2.4f, getItemProperties().maxDamage(durabilityCobalt)), "cobalt_excavator");
  
    register(new Trowel(getItemProperties()), "trowel");
  
    register(new SwapTool(ItemTier.STONE, getItemProperties()), "stone_swap_tool");
    register(new SwapTool(ItemTier.IRON, getItemProperties()), "iron_swap_tool");
    register(new SwapTool(ItemTier.DIAMOND, getItemProperties()), "diamond_swap_tool");
  
    register(new MagnetItem(getItemProperties().maxStackSize(1)), "magnet");
  
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.BASIC), "watering_can");
    register(new WateringCan(getItemProperties().maxStackSize(1), WateringCan.Tier.COPPER), "watering_can_copper");
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
  protected void registerAllContainers()
  { }
}
