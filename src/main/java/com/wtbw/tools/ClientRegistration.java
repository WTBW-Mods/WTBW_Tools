package com.wtbw.tools;

import com.wtbw.tools.blocks.ModBlocks;
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
    final RenderType translucent = RenderType.func_228645_f_();
    
    RenderTypeLookup.setRenderLayer(ModBlocks.GREENHOUSE_GLASS, translucent);
  }
}
