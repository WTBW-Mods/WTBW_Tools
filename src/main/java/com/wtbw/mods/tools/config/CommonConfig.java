package com.wtbw.mods.tools.config;

import com.wtbw.mods.lib.config.BaseConfig;
import com.wtbw.mods.lib.config.SubConfig;
import com.wtbw.mods.tools.WTBWTools;
import com.wtbw.mods.tools.item.tools.WateringCan;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

/*
  @author: Naxanria
*/
public class CommonConfig extends BaseConfig
{
  private static CommonConfig instance;
  public static CommonConfig instance()
  {
    return instance;
  }
  
  public static void init()
  {
    final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    instance = specPair.getLeft();
    
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
  }
  
  public ForgeConfigSpec.BooleanValue wateringCanSpreadGrass;
  public ForgeConfigSpec.IntValue wateringCanRefillRate;
  public ForgeConfigSpec.BooleanValue wateringCanMoisterize;
  
  public WateringCanConfig basicWateringCan;
  public WateringCanConfig copperWateringCan;
  public WateringCanConfig quartzWateringCan;
  public WateringCanConfig diamondWateringCan;
  public WateringCanConfig enderWateringCan;
  
  public ForgeConfigSpec.DoubleValue areaToolsDurabilityMultiplier;
  public ForgeConfigSpec.DoubleValue areaToolsSpeedModifier;
  
  public ForgeConfigSpec.ConfigValue<String> swapperBlackList;
  public ForgeConfigSpec.DoubleValue swapperMaxHardness;
  
  public ForgeConfigSpec.BooleanValue magnetCheckCanPickUp;
  public ForgeConfigSpec.IntValue magnetTickRate;
  public ForgeConfigSpec.IntValue magnetRadius;
  
  
  
  public CommonConfig(ForgeConfigSpec.Builder builder)
  {
    super(WTBWTools.MODID, builder);
    
    instance = this;
  
    push("tools");
  
    hammers();
  
    swapping();
  
    magnet();
  
    wateringCans();
  
    pop();
  }
  
  private void wateringCans()
  {
    push("wateringCans");
    
    wateringCanSpreadGrass = builder
      .comment("Can the watering cans spread grass faster")
      .translation(key("tools.wateringcan.spread_grass"))
      .define("canSpreadGrass", true);
    
    wateringCanRefillRate = builder
      .comment("The refill rate of water/tick")
      .translation(key("tools.wateringcan.refill_rate"))
      .defineInRange("refillRate", 30, 1, 100000);
    
    wateringCanMoisterize = builder
      .comment("Moisterize farm land")
      .translation(key("tools.wateringcan.moisterize"))
      .define("moisterize", true);
    
    basicWateringCan = new WateringCanConfig(WateringCan.Tier.BASIC, "basic", builder);
    copperWateringCan = new WateringCanConfig(WateringCan.Tier.COPPER, "copper", builder);
    quartzWateringCan = new WateringCanConfig(WateringCan.Tier.QUARTZ, "quartz", builder);
    diamondWateringCan = new WateringCanConfig(WateringCan.Tier.DIAMOND, "diamond", builder);
    enderWateringCan = new WateringCanConfig(WateringCan.Tier.ENDER, "ender", builder);
    
    pop();
  }
  
  private void hammers()
  {
    push("3x3");
    areaToolsDurabilityMultiplier = builder
      .comment
        (
          "The multiplier for the area tools durability.",
          "The durability is calculated by the equivalant tools durability",
          "multiplied by this multiplier",
          "Default: 5.0"
        )
      .translation(key("tools.hammer.multiplier"))
      .defineInRange("multiplier", 5.0, 1.0, 15.0);
    
    areaToolsSpeedModifier = builder
      .comment
        (
          "The modifier for the speed of area tools",
          "The speed gets divided by this number, so the higher the number, the slower the tool",
          "Default: 4"
        )
      .translation(key("tools.area.speed_modifier"))
      .defineInRange("speed_modifier", 3.5, 1.0, 10.0);
    
    pop();
  }
  
  private void swapping()
  {
    push("swapping");
    
    swapperBlackList = builder
      .comment("The blacklisted blocks")
      .translation(key("tools.swapping.blacklist"))
      .define("blacklist", "minecraft:bedrock");
    
    swapperMaxHardness = builder
      .comment("Maximum hardness that can be swapped")
      .translation(key("tools.swapping.hardness"))
      .defineInRange("max_hardness", 49, 0, Double.MAX_VALUE);
    
    pop();
  }
  
  
  private void magnet()
  {
    push("magnet");
    
    magnetTickRate = builder
      .comment("How often the magnet needs to check to magnetize, in ticks", "default: 1")
      .translation(key("tools.magnet.tick_rate"))
      .defineInRange("tick_rate", 1, 1, 60);
    
    magnetRadius = builder
      .comment("The radius (cube) that items can be magnetized", "default: 6")
      .translation(key("tools.magnet.radius"))
      .defineInRange("radius", 6, 1, 16);
    
    magnetCheckCanPickUp = builder
      .comment("Should the magnet only pickup items that fit into the inventory", "default: true")
      .translation(key("tools.magnet.pick_up"))
      .define("pick_up", true);
    
    pop();
  }
  
  public static class WateringCanConfig extends SubConfig
  {
    public final WateringCan.Tier tier;
    public final String name;
    
    public ForgeConfigSpec.IntValue radius;
    public ForgeConfigSpec.IntValue maxWater;
    public ForgeConfigSpec.IntValue drainRate;
    public ForgeConfigSpec.IntValue chance;
    
    public ForgeConfigSpec.BooleanValue useWater;
    public ForgeConfigSpec.BooleanValue autoHarvest;
    
    public WateringCanConfig(WateringCan.Tier tier, String name, ForgeConfigSpec.Builder builder)
    {
      super(builder);
      this.tier = tier;
      this.name = name;
      init();
    }
    
    @Override
    protected void init()
    {
      push(name);
      
      WateringCan.WateringCanData data = WateringCan.getData(tier);
      String baseKey = "tools.wateringcan." + name + ".";
      CommonConfig parent = CommonConfig.instance();
      
      radius = builder
        .comment("The radius of effect for the watering can", "Number MUST be uneven")
        .translation(parent.key(baseKey + "radius"))
        .defineInRange("radius", data.radius, 1, 15);
      
      useWater = builder
        .comment("Use water at all, or have it be infinite use", "Default: " + data.useWater)
        .translation(parent.key(baseKey + "use_water"))
        .define("useWater", data.useWater);
      
      autoHarvest = builder
        .comment("Auto harvest fully grown crops", "Default: " + data.autoHarvest)
        .translation(parent.key(baseKey + "auto_harvest"))
        .define("autoHarvest", data.autoHarvest);
      
      maxWater = builder
        .comment("The maximum amount of water the watering can can hold")
        .translation(parent.key(baseKey + "max_water"))
        .defineInRange("maxWater", data.maxWater, 100, 100000);
      
      drainRate = builder
        .comment("The amount of water consumed per tick")
        .translation(parent.key(baseKey + "drain_rate"))
        .defineInRange("drainRate", data.waterUse, 1, 10000);
      
      chance = builder
        .comment("The chance to further a growth stage")
        .translation(parent.key(baseKey + "chance"))
        .defineInRange("chance", data.chance, 1, 100);
      
      pop();
    }
  
    @Override
    protected void reload()
    {
      WateringCan.WateringCanData data = WateringCan.getData(tier);
      data.radius = radius.get();
      data.maxWater = maxWater.get();
      data.waterUse = drainRate.get();
      data.chance = chance.get();
    }
  }
}
