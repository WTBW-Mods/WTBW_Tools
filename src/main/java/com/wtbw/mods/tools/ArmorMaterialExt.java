package com.wtbw.mods.tools;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.HashMap;
import java.util.Map;

/*
  @author: Naxanria
*/
public class ArmorMaterialExt implements IArmorMaterial
{
  private static final Map<IItemTier, IArmorMaterial> materialMap = new HashMap<>();
  
  public static IArmorMaterial get(IItemTier tier)
  {
    return materialMap.getOrDefault(tier, ArmorMaterial.LEATHER);
  }
  
  public static final ArmorMaterialExt COPPER = new ArmorMaterialExt("copper", ItemTierExt.COPPER, 12, new int[]{2, 5, 5, 2}, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0, 0);
  public static final ArmorMaterialExt COBALT = new ArmorMaterialExt("cobalt", ItemTierExt.COBALT, 37, new int[]{4, 7, 9, 4}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4, 3.5f);
  public static final ArmorMaterialExt SHULKER = new ArmorMaterialExt("shulker", ItemTierExt.SHULKER, 69, new int[]{6, 9, 11, 6}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 8, 5f);

  // mirrored from vanilla
  private static final int[] DURABILITY = new int[]{13, 15, 16, 11};
  
  private final IItemTier tier;
  private final int[] reduction;
  private final SoundEvent soundEvent;
  
  private final String fullname;
  
  private final String name;
  
  private final float toughness;
  
  private final int durabilityFactor;
  
  private final float knockbackResistance;
  
  public ArmorMaterialExt(String name, IItemTier tier, int durabilityFactor, int[] reduction, SoundEvent soundEvent, float toughness, float knockbackResistance)
  {
    this.tier = tier;
    this.durabilityFactor = durabilityFactor;
    this.reduction = reduction;
    this.soundEvent = soundEvent;
    this.name = name;
    this.fullname = WTBWTools.MODID + ":" + name;
    this.toughness = toughness;
    this.knockbackResistance = knockbackResistance;
  
    materialMap.put(tier, this);
  }
  
  public IItemTier getTier()
  {
    return tier;
  }
  
  @Override
  public int getDurability(EquipmentSlotType slot)
  {
    return durabilityFactor * DURABILITY[slot.getIndex()];
  }
  
  @Override
  public int getDamageReductionAmount(EquipmentSlotType slot)
  {
    return reduction[slot.getIndex()];
  }
  
  @Override
  public int getEnchantability()
  {
    return tier.getEnchantability();
  }
  
  @Override
  public SoundEvent getSoundEvent()
  {
    return soundEvent;
  }
  
  @Override
  public Ingredient getRepairMaterial()
  {
    return tier.getRepairMaterial();
  }
  
  @Override
  public String getName()
  {
    return fullname;
  }
  
  public String getMaterialName()
  {
    return name;
  }
  
  @Override
  public float getToughness()
  {
    return toughness;
  }
  
  @Override
  public float getKnockbackResistance()
  {
    return knockbackResistance;
  }
}
