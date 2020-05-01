package com.wtbw.mods.tools.item.tools;

import com.wtbw.mods.lib.util.PlayEvent;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.lib.util.nbt.NBTHelper;
import com.wtbw.mods.lib.util.rand.RandomUtil;
import com.wtbw.mods.tools.WTBWTools;
import com.wtbw.mods.tools.config.CommonConfig;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
  @author: Naxanria
*/
public class WateringCan extends Item
{
  public enum Tier
  {
    BASIC,
    COPPER,
    QUARTZ,
    DIAMOND,
    ENDER;
  }
  
  public static class WateringCanData
  {
    public int radius;
    public int maxWater;
    public int waterUse;
    public int chance;
    
    public boolean useWater = true;
    public boolean autoHarvest = false;
    
    public WateringCanData(int radius, int maxWater, int waterUse, int chance)
    {
      this.radius = radius;
      this.maxWater = maxWater;
      this.waterUse = waterUse;
      this.chance = chance;
    }
    
    public WateringCanData noWater()
    {
      useWater = false;
      return this;
    }
    
    public WateringCanData autoHarvest()
    {
      autoHarvest = true;
      return this;
    }
    
  }
  
  private static Map<Tier, WateringCanData> tierMap = new HashMap<Tier, WateringCanData>()
  {{
    put(Tier.BASIC, new WateringCanData(3, 1000, 5, 10));
    put(Tier.COPPER, new WateringCanData(3, 2000, 5, 15));
    put(Tier.QUARTZ, new WateringCanData(3, 5000, 5, 25));
    put(Tier.DIAMOND, new WateringCanData(5, 10000, 5, 35));
    put(Tier.ENDER, new WateringCanData(7, 10000, 5, 50).noWater().autoHarvest());
  }};
  
  public static WateringCanData getData(Tier tier)
  {
    return tierMap.get(tier);
  }
  
  protected WateringCanData wateringCanData;
  protected Tier tier;
  
  public WateringCan(Properties properties, Tier tier)
  {
    super(properties.rarity(tier == Tier.ENDER ? Rarity.EPIC : tier == Tier.DIAMOND ? Rarity.RARE : Rarity.COMMON));
    
    this.tier = tier;
    wateringCanData = tierMap.getOrDefault(tier, tierMap.get(Tier.BASIC));
  }
  
  @Override
  public int getUseDuration(ItemStack stack)
  {
    return 100000000;
  }
  
  private int getWater(ItemStack stack)
  {
    CompoundNBT data = stack.getOrCreateChildTag("data");
    return NBTHelper.getInt(data, "water");
  }
  
  private void setWater(ItemStack stack, int amount)
  {
    amount = MathHelper.clamp(amount, 0, wateringCanData.maxWater);
    stack.getOrCreateChildTag("data").putInt("water", amount);
  }
  
  private int changeWater(ItemStack stack, int delta)
  {
    CompoundNBT data = stack.getOrCreateChildTag("data");
    int water = NBTHelper.getInt(data, "water");
    data.putInt("water", water = MathHelper.clamp(water + delta, 0, wateringCanData.maxWater));
    return water;
  }
  
  @Override
  public boolean showDurabilityBar(ItemStack stack)
  {
    return wateringCanData.useWater && getWater(stack) < wateringCanData.maxWater;
  }
  
  @Override
  public double getDurabilityForDisplay(ItemStack stack)
  {
    return 1.0 - (getWater(stack) / (double) wateringCanData.maxWater);
  }
  
  @Override
  public ActionResultType onItemUse(ItemUseContext context)
  {
    return super.onItemUse(context);
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
  {
    playerIn.setActiveHand(handIn);
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }
  
  @Override
  public void onUsingTick(ItemStack stack, LivingEntity player, int count)
  {
    if (!(player instanceof PlayerEntity))
    {
      return;
    }
  
    CommonConfig config = CommonConfig.instance();
  
    World world = player.world;
    Random rand = world.rand;
    BlockRayTraceResult lookingAt = Utilities.getLookingAt((PlayerEntity) player, 5, RayTraceContext.FluidMode.SOURCE_ONLY);
    if (Utilities.isMiss(lookingAt))
    {
      return;
    }
  
    BlockState lookingState = world.getBlockState(lookingAt.getPos());
    if (wateringCanData.useWater)
    {
      if (lookingState.getFluidState().getFluid() == Fluids.WATER)
      {
        changeWater(stack, config.wateringCanRefillRate.get());
    
        return;
      }
  
      // enough maxWater
      if (!((PlayerEntity) player).isCreative() && changeWater(stack, -wateringCanData.waterUse) == 0)
      {
        return;
      }
    }
  
    boolean spreadGrass = config.wateringCanSpreadGrass.get();
    boolean moisturize = config.wateringCanMoisterize.get();
  
    // get in radius
    List<BlockPos> blocks = Utilities.getBlocks(lookingAt.getPos(), Direction.DOWN, wateringCanData.radius);
    for (BlockPos pos : blocks)
    {
      if (world.isRemote && RandomUtil.chance(rand, 0.3f))
      {
        // todo: proper rain/water particles
        double x = pos.getX() + rand.nextDouble();
        double y = pos.getY() + rand.nextDouble() + .7;
        double z = pos.getZ() + rand.nextDouble();
  
        world.addParticle(ParticleTypes.RAIN, x, y, z, 0, 0, 0);
      }
      if (world.isRemote)
      {
        continue;
      }
  
      // Chance: P/20 -> 1/20th of a chance per tick, naively make it be ~P/sec
      if (!RandomUtil.chance(rand, (wateringCanData.chance / 100f / 20f)))
      {
        continue;
      }
  
  
      BlockState state = world.getBlockState(pos);
      Block block = state.getBlock();
      CropsBlock crop = null;
      SaplingBlock sapling = null;
  
      if (spreadGrass && block instanceof SpreadableSnowyDirtBlock)
      {
        if (!world.isRemote)
        {
          ((SpreadableSnowyDirtBlock) block).randomTick(state, (ServerWorld) world, pos, rand);
        }
        else
        {
          BoneMealItem.spawnBonemealParticles(world, pos.up(), RandomUtil.range(rand, 3, 6));
        }
  
        continue;
      }
  
      if (block == Blocks.SUGAR_CANE)
      {
        sugarcane(world, pos, state);
        continue;
      }
      
      if (block == Blocks.CACTUS)
      {
        cactus(world, pos, state);
        continue;
      }
      
      if (block == Blocks.NETHER_WART)
      {
        netherWart(world, pos, state);
        continue;
      }
  
      if (block instanceof CropsBlock)
      {
        crop = (CropsBlock) block;
      }
      else if (block instanceof SaplingBlock)
      {
        sapling = (SaplingBlock) block;
      }
  
      if (crop == null && sapling == null)
      {
    
        if (moisturize && block instanceof FarmlandBlock)
        {
          int moisture = state.get(FarmlandBlock.MOISTURE);
          if (moisture < 7)
          {
            BlockState newState = state.cycle(FarmlandBlock.MOISTURE);
            world.setBlockState(pos, newState, 3);
          }
        }
    
        pos = pos.up();
        state = world.getBlockState(pos);
        block = state.getBlock();
    
        if (block instanceof CropsBlock)
        {
          crop = (CropsBlock) block;
        }
        else if (block instanceof SaplingBlock)
        {
          sapling = (SaplingBlock) block;
        }
      }
  
      if (crop == null && sapling == null)
      {
        continue;
      }
  
      if (crop != null)
      {
        if (!crop.isMaxAge(state) && crop.canGrow(world, pos, state, world.isRemote))
        {
          if (!world.isRemote)
          {
            crop.grow(world, pos, state);
            bonemeal(world, pos);
          }
        }
        
        else if (wateringCanData.autoHarvest && crop.isMaxAge(state))
        {
          Utilities.dropItems(world, Block.getDrops(state, (ServerWorld) world, pos, null), pos);
          world.setBlockState(pos, crop.withAge(0), Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
      }
      else
      {
        if (sapling.canGrow(world, pos, state, world.isRemote))
        {
          if (!world.isRemote)
          {
            sapling.func_226942_a_((ServerWorld) world, pos, state, rand);
            bonemeal(world, pos);
          }
        }
      }
    }
  }
  
  private void netherWart(World world, BlockPos pos, BlockState state)
  {
    int age = state.get(NetherWartBlock.AGE);
    if (age < 3)
    {
      age++;
      world.setBlockState(pos, state.with(NetherWartBlock.AGE, age), Constants.BlockFlags.DEFAULT_AND_RERENDER);
      bonemeal(world, pos);
    }
    else if (age == 3 && wateringCanData.autoHarvest)
    {
      Utilities.dropItems(world, Block.getDrops(state, (ServerWorld) world, pos, null), pos);
      world.setBlockState(pos, state.with(NetherWartBlock.AGE, 0), Constants.BlockFlags.DEFAULT_AND_RERENDER);
    }
  }
  
  private void cactus(World world, BlockPos pos, BlockState state)
  {
    int length;
    for (length = 1; world.getBlockState(pos.down(length)).getBlock() == Blocks.CACTUS; length++)
    { }
    if (length < 3)
    {
      int up;
      for (up = 1; world.getBlockState(pos.up(up)).getBlock() == Blocks.CACTUS; up++)
      {
        length++;
      }
      if (length < 3)
      {
        if (up > 1)
        {
          pos = pos.up(up);
        }
  
        if (world.isAirBlock(pos.up()))
        {
          int age = state.get(SugarCaneBlock.AGE);
          if (ForgeHooks.onCropsGrowPre(world, pos, state, true))
          {
            if (age == 15)
            {
              world.setBlockState(pos.up(), Blocks.CACTUS.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
              world.setBlockState(pos, state.with(CactusBlock.AGE, 0), Constants.BlockFlags.DEFAULT_AND_RERENDER);
              state.neighborChanged(world, pos.up(), Blocks.CACTUS, pos, false);
//                  world.notifyBlockUpdate(pos.up(), Blocks.AIR.getDefaultState(), Blocks.SUGAR_CANE.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            else
            {
              world.setBlockState(pos, state.with(CactusBlock.AGE, age + 1), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
          }
          bonemeal(world, pos);
          ForgeHooks.onCropsGrowPost(world, pos, state);
        }
      }
    }
  }
  
  private void sugarcane(World world, BlockPos pos, BlockState state)
  {
    int length;
    for (length = 1; world.getBlockState(pos.down(length)).getBlock() == Blocks.SUGAR_CANE; length++)
    { }
    if (length < 3)
    {
      int up;
      for (up = 1; world.getBlockState(pos.up(up)).getBlock() == Blocks.SUGAR_CANE; up++)
      {
        length++;
      }
      if (length < 3)
      {
        if (up > 1)
        {
          pos = pos.up(up);
        }

        if (world.isAirBlock(pos.up()))
        {
          int age = state.get(SugarCaneBlock.AGE);
          if (ForgeHooks.onCropsGrowPre(world, pos, state, true))
          {
            if (age == 15)
            {
              world.setBlockState(pos.up(), Blocks.SUGAR_CANE.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
              world.setBlockState(pos, state.with(SugarCaneBlock.AGE, 0), Constants.BlockFlags.DEFAULT_AND_RERENDER);
//                  world.notifyBlockUpdate(pos.up(), Blocks.AIR.getDefaultState(), Blocks.SUGAR_CANE.getDefaultState(), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            else
            {
              world.setBlockState(pos, state.with(SugarCaneBlock.AGE, age + 1), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
          }
          bonemeal(world, pos);
          ForgeHooks.onCropsGrowPost(world, pos, state);

        }
      }
    }
  }
  
  private void bonemeal(World world, BlockPos pos)
  {
    PlayEvent.boneMeal(world, pos, RandomUtil.range(world.rand, 3, 6));
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    int water = getWater(stack);
    if (wateringCanData.useWater)
    {
      tooltip.add(new TranslationTextComponent(WTBWTools.MODID + ".tooltip.water", water, wateringCanData.maxWater));
    }
    
    if (wateringCanData.autoHarvest)
    {
      tooltip.add(TextComponentBuilder.createTranslated(WTBWTools.MODID + ".tooltip.harvest").gold().build());
    }
    
    tooltip.add(new TranslationTextComponent(WTBWTools.MODID + ".tooltip.radius", wateringCanData.radius));
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  @Override
  public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
  {
    return true;
  }
  
  @Override
  public UseAction getUseAction(ItemStack stack)
  {
    return UseAction.BLOCK;
  }
  
  @Override
  public boolean hasEffect(ItemStack stack)
  {
    return tier == Tier.ENDER;
  }
}