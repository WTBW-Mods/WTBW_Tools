package com.wtbw.mods.tools;

import com.wtbw.mods.tools.blocks.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class ClientRegistration
{
  public static void setupRenderLayers()
  {
    final RenderType translucent = RenderType.translucent();
    
    RenderTypeLookup.setRenderLayer(ModBlocks.GREENHOUSE_GLASS, translucent);
  }
}
