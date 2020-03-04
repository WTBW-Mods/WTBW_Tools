package com.wtbw.mods.tools.item.armour;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class CobaltArmour extends ArmorItem
{
  public CobaltArmour(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder)
  {
    super(materialIn, slot, builder);
  }
  
  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
  {
    // every 5 sec
    if (world.getGameTime() % 100L == 0)
    {
      int duration = 6 * 20;
      if (getEquipmentSlot().getIndex() == itemSlot)
      {
        if (entity instanceof PlayerEntity)
        {
          PlayerEntity player = (PlayerEntity) entity;
          if (player.inventory.armorInventory.get(itemSlot).equals(stack, false))
          {
            switch (getEquipmentSlot())
            {
              case FEET:
                player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, duration, 0, true, false));
                break;
          
              case LEGS:
                player.addPotionEffect(new EffectInstance(Effects.SPEED, duration, 0, true, false));
                break;
          
              case CHEST:
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, duration, 0, true, false));
                break;
          
              case HEAD:
                player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, duration, 0, true, false));
                break;
            }
          }
        }
      }
    }
  }
}
