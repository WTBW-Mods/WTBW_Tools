package com.wtbw.mods.tools;

import com.wtbw.mods.tools.blocks.ModBlocks;
import com.wtbw.mods.tools.client.BlockHighlightRenderer;
import com.wtbw.mods.tools.client.RenderManager;
import com.wtbw.mods.tools.item.ModItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

/*
  @author: Naxanria
*/
@SuppressWarnings("ConstantConditions")
public class ClientRegistration
{
  public static void init()
  {
    setupRenderLayers();
  
//    RenderManager.register(ModItems.DIAMOND_HAMMER, BlockHighlightRenderer.HARVESTABLE_BLOCKS_3x3);
  }
  
  public static void setupRenderLayers()
  {
    final RenderType translucent = RenderType.translucent();
    
    RenderTypeLookup.setRenderLayer(ModBlocks.GREENHOUSE_GLASS, translucent);
  }
  
  
}
