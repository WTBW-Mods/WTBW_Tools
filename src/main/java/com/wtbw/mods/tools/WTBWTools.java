package com.wtbw.mods.tools;

import com.wtbw.mods.lib.ClientSetup;
import com.wtbw.mods.lib.keybinds.LibKeyBinds;
import com.wtbw.mods.tools.client.RenderManager;
import com.wtbw.mods.tools.config.CommonConfig;
import com.wtbw.mods.tools.event.ArmourEvents;
import com.wtbw.mods.tools.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
  @author: Sunekaer
*/
@Mod(WTBWTools.MODID)
public class WTBWTools
{
  public static final String MODID = "wtbw_tools";
  
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  
  public static final ItemGroup GROUP = new ItemGroup(MODID)
  {
    @Override
    public ItemStack createIcon()
    {
      return new ItemStack(ModItems.DIAMOND_HAMMER);
    }
  };
  
  public WTBWTools()
  {
    CommonConfig.init();
    new ToolsRegistrator(GROUP, MODID);
  
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> LibKeyBinds.DEFAULT.registerToolRadiusKeyBinds());
  
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    MinecraftForge.EVENT_BUS.addListener(ArmourEvents::onFallDamageTaken);
  }
  
  public void clientSetup(final FMLClientSetupEvent event)
  {
    ClientRegistration.init();
//    MinecraftForge.EVENT_BUS.addListener(RenderManager::onDrawEnd);
  }
}
