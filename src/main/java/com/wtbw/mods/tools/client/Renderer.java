package com.wtbw.mods.tools.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/*
  @author: Naxanria
*/
public abstract class Renderer
{
  protected final Minecraft minecraft = Minecraft.getInstance();
  
  public abstract void render(final RenderWorldLastEvent event, ItemStack stack);
}
