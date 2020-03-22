package com.wtbw.mods.tools.event;

import com.wtbw.mods.tools.item.ModItems;
import com.wtbw.mods.tools.item.armour.FullAmourSetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.util.ArrayList;
import java.util.List;

/*
  @author: Naxanria
*/
public class ArmourEvents
{
  private static List<FullAmourSetHandler> fullSetHandlers = new ArrayList<>();
  
  public static void registerHandler(FullAmourSetHandler handler)
  {
    if (!fullSetHandlers.contains(handler))
    {
      fullSetHandlers.add(handler);
    }
  }
  
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
          float dmg = event.getAmount();
          event.setAmount(0);
          if (dmg > 5.0f && player instanceof ServerPlayerEntity)
          {
            boots.attemptDamageItem(1, player.world.rand, (ServerPlayerEntity) player);
          }
        }
      }
    }
  }
  
  public static void onTick(final TickEvent.PlayerTickEvent event)
  {
    if (event.side.isServer())
    {
      fullSetHandlers.forEach(handler -> handler.handle(event.player));
    }
  }
}
