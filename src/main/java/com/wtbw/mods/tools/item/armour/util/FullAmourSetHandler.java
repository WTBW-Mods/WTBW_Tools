package com.wtbw.mods.tools.item.armour.util;

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
  public final Consumer<PlayerEntity> orElse;
  
  public FullAmourSetHandler(Predicate<PlayerEntity> isFullSet, Consumer<PlayerEntity> apply)
  {
    this(isFullSet, apply, (p) -> {});
  }
  
  public FullAmourSetHandler(Predicate<PlayerEntity> isFullSet, Consumer<PlayerEntity> apply, Consumer<PlayerEntity> orElse)
  {
    this.isFullSet = isFullSet;
    this.apply = apply;
  
    this.orElse = orElse;
  }
  
  public void handle(PlayerEntity player)
  {
    if (isFullSet.test(player))
    {
      apply.accept(player);
    }
    else
    {
      orElse.accept(player);
    }
  }
}
