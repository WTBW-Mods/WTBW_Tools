package com.wtbw.mods.tools.item.armour.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;

/*
  @author: Naxanria
*/
public class ArmourFlightManager
{
  private static Set<PlayerEntity> enabledFlightSet = new HashSet<>();
  
  public static boolean isEnabledArmourFlight(PlayerEntity player)
  {
    return enabledFlightSet.contains(player);
  }
  
  public static void addEnabledArmourFlight(PlayerEntity player)
  {
    enabledFlightSet.add(player);
  }
  
  public static void removeEnabledArmourFlight(PlayerEntity player)
  {
    enabledFlightSet.remove(player);
  }
  
  public static boolean canFly(PlayerEntity player)
  {
    return player.abilities.allowFlying;
  }
  
  public static void startFlight(PlayerEntity player)
  {
    if (!player.isCreative() && !player.isSpectator() && !canFly(player))
    {
      player.abilities.allowFlying = true;
      player.sendPlayerAbilities();
      addEnabledArmourFlight(player);
    }
  }
  
  public static void stopFlight(PlayerEntity player)
  {
    if (!player.isCreative() && !player.isSpectator() && canFly(player) && isEnabledArmourFlight(player))
    {
      player.abilities.allowFlying = false;
      player.abilities.isFlying = false;
      player.sendPlayerAbilities();
      removeEnabledArmourFlight(player);
    }
  }
}
