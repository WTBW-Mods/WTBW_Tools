package com.wtbw.mods.tools.client;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/*
  @author: Naxanria
*/
public abstract class Renderer
{
  public abstract void render(final RenderWorldLastEvent event, ItemStack stack);
}
