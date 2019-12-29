package com.wtbw.tools;

import com.wtbw.lib.keybinds.LibKeyBinds;
import com.wtbw.tools.config.CommonConfig;
import com.wtbw.tools.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
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
  }
  
  public void clientSetup(final FMLClientSetupEvent event)
  {
    ClientRegistration.setupRenderLayers();
  }
}
