package com.wtbw.mods.tools.item.armour;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;
import java.util.function.Predicate;

/*
  @author: Naxanria
*/
public class FullAmourSetHandler
{
  public final Predicate<PlayerEntity> isFullSet;
  public final Consumer<PlayerEntity> apply;
  
  public FullAmourSetHandler(Predicate<PlayerEntity> isFullSet, Consumer<PlayerEntity> apply)
  {
    this.isFullSet = isFullSet;
    this.apply = apply;
  }
  
  public void handle(PlayerEntity player)
  {
    if (isFullSet.test(player))
    {
      apply.accept(player);
    }
  }
}
