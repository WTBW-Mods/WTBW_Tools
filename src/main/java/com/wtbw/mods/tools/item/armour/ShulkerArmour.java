package com.wtbw.mods.tools.item.armour;

import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.tools.WTBWTools;
import com.wtbw.mods.tools.item.armour.util.ArmourFlightManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Sunekaer, Naxanria
*/
public class ShulkerArmour extends ArmorItem
{
  public ShulkerArmour(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder)
  {
    super(materialIn, slot, builder);
  }
  
  @Override
  public void onArmorTick(ItemStack stack, World world, PlayerEntity player)
  {
    // every 5 sec
    boolean apply = world.getGameTime() % 100L == 0;
  
    int duration = 6 * 20;
  
    switch (getEquipmentSlot())
    {
      case FEET:
        if (apply)
        {
          player.addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, duration, 1, true, false));
        }
        break;
    
      case LEGS:
        if (apply)
        {
          player.addPotionEffect(new EffectInstance(Effects.SPEED, duration, 1, true, false));
        }
        break;
    
      case CHEST:
        if (apply)
        {
          player.addPotionEffect(new EffectInstance(Effects.REGENERATION, duration, 1, true, false));
        }
        
        if (hasFullSet(player))
        {
          ArmourFlightManager.startFlight(player);
          applyResistance(player);
        }
        else
        {
          ArmourFlightManager.stopFlight(player);
        }
        break;
    
      case HEAD:
        if (apply)
        {
          player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, duration, 1, true, false));
        }
        break;
    }
  }
  
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    Effect effect = null;
    switch (getEquipmentSlot())
    {
      case FEET:
        effect = Effects.DOLPHINS_GRACE;
        break;
        
      case LEGS:
        effect = Effects.SPEED;
        break;
        
      case CHEST:
        effect = Effects.REGENERATION;
        break;
      
      case HEAD:
        effect = Effects.WATER_BREATHING;
        break;
    }
    if (effect != null)
    {
      tooltip.add(TextComponentBuilder.createTranslated(effect.getDisplayName().getUnformattedComponentText() + " II").blue().build());
    }
    if (getEquipmentSlot() == EquipmentSlotType.FEET)
    {
      tooltip.add(TextComponentBuilder.createTranslated(WTBWTools.MODID + ".tooltip.no_fall_damage").blue().build());
    }

    tooltip.add(TextComponentBuilder.createTranslated(WTBWTools.MODID + ".tooltip.shulker_setbonus").gold().build());
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  public static void applyResistance(PlayerEntity player)
  {
    if (player.world.getGameTime() % 100L == 0)
    {
      player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 6 * 20, 1, true, false));
    }
    
    ArmourFlightManager.startFlight(player);
  }

  public static boolean hasFullSet(PlayerEntity player)
  {
    return isShulkerArmour(player.getItemStackFromSlot(EquipmentSlotType.FEET)) &&
      isShulkerArmour(player.getItemStackFromSlot(EquipmentSlotType.CHEST)) &&
      isShulkerArmour(player.getItemStackFromSlot(EquipmentSlotType.LEGS)) &&
      isShulkerArmour(player.getItemStackFromSlot(EquipmentSlotType.HEAD));
  }

  public static boolean isShulkerArmour(ItemStack stack)
  {
    return  (!stack.isEmpty() && stack.getItem() instanceof ShulkerArmour);
  }
}
