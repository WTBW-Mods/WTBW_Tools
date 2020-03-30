package com.wtbw.mods.tools;

import com.wtbw.mods.lib.keybinds.LibKeyBinds;
import com.wtbw.mods.tools.config.CommonConfig;
import com.wtbw.mods.tools.event.ArmourEvents;
import com.wtbw.mods.tools.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

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
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEvent);
    IEventBus eventBus = MinecraftForge.EVENT_BUS;
    eventBus.addListener(ArmourEvents::onFallDamageTaken);
    eventBus.addListener(ArmourEvents::onTick);
  }
  
  public void clientSetup(final FMLClientSetupEvent event)
  {
    ClientRegistration.init();
//    MinecraftForge.EVENT_BUS.addListener(RenderManager::onDrawEnd);
  }

  public void imcEvent(final InterModEnqueueEvent event)
  {
    InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage("ring"));
  }
}
