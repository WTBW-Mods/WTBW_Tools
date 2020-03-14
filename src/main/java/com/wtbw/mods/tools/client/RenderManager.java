package com.wtbw.mods.tools.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  @author: Naxanria
*/
public class RenderManager
{
  private static Map<Item, Renderer> rendererMap = new HashMap<>();
  
  public static void register(Item item, Renderer renderer)
  {
    rendererMap.put(item, renderer);
  }
  
  public static void onDrawEnd(final RenderWorldLastEvent event)
  {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player != null)
    {
      ItemStack heldItemMainHand = player.getHeldItemMainhand();
      if (!heldItemMainHand.isEmpty() && rendererMap.containsKey(heldItemMainHand.getItem()))
      {
        Renderer renderer = rendererMap.get(heldItemMainHand.getItem());
        renderer.render(event, heldItemMainHand);
      }
    }
  }
}
