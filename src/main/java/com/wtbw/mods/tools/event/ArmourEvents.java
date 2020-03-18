package com.wtbw.mods.tools.event;

import com.wtbw.mods.tools.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

/*
  @author: Naxanria
*/
public class ArmourEvents
{
  public static void onFallDamageTaken(final LivingDamageEvent event)
  {
    if (event.getSource() == DamageSource.FALL)
    {
      if (event.getEntityLiving() instanceof PlayerEntity)
      {
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        ItemStack boots = player.inventory.armorInventory.get(EquipmentSlotType.FEET.getIndex());
        if (boots.getItem() == ModItems.COBALT_BOOTS)
        {
          event.setAmount(0);
          if (player instanceof ServerPlayerEntity)
          {
            boots.attemptDamageItem(1, player.world.rand, (ServerPlayerEntity) player);
          }
        }
      }
    }
  }
}
