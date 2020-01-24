package com.wtbw.mods.tools.blocks;

import com.wtbw.mods.lib.util.PlayEvent;
import com.wtbw.mods.lib.util.rand.RandomUtil;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.tools.WTBWTools;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/*
  @author: Naxanria
*/
public class GreenHouseGlassBlock extends AbstractGlassBlock implements IBeaconBeamColorProvider
{
  public GreenHouseGlassBlock(Properties properties)
  {
    super(properties.tickRandomly());
  }
  
//  @Override
//  public BlockRenderLayer getRenderLayer()
//  {
//    return BlockRenderLayer.TRANSLUCENT;
//  }
  
//  @OnlyIn(Dist.CLIENT)
//  @Override
//  public float getAmbientOcclusionLightValue(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_)
//  {
//    return 1.0F;
//  }
  
  @Override
  public boolean causesSuffocation(BlockState p_229869_1_, IBlockReader p_229869_2_, BlockPos p_229869_3_) {
    return false;
  }

  @Override
  public DyeColor getColor()
  {
    return DyeColor.GREEN;
  }
  
  @Override
  public boolean ticksRandomly(BlockState p_149653_1_)
  {
    return true;
  }
  
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
  {
    int maxRange = 25;
    int chance = 40;
    
    if (!world.isRemote)
    {
      if (!world.isDaytime())
      {
        return;
      }
      
      if (RandomUtil.chance(rand, chance))
      {
        if (world.canBlockSeeSky(pos.up()))
        {
          for (int i = 1; i < maxRange; i++)
          {
            BlockPos check = pos.down(i);
            BlockState foundState = world.getBlockState(check);
            Block block = foundState.getBlock();
            if (block instanceof CropsBlock)
            {
              if (!((CropsBlock) block).isMaxAge(foundState))
              {
                PlayEvent.boneMeal(world, check, rand.nextInt(7) + 5);
                ((CropsBlock) block).grow(world, check, foundState);
              }
              
              break;
            }
            if (block instanceof SaplingBlock)
            {
              PlayEvent.boneMeal(world, check, rand.nextInt(7) + 5);
              ((SaplingBlock) block).func_226942_a_(world, check, foundState, rand);

              break;
            }
          }
        }
      }
    }
  }
  
  
  
  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    String baseKey = WTBWTools.MODID + ".tooltip.greenhouse_glass";
    tooltip.add(TextComponentBuilder.createTranslated(baseKey).aqua().build());
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
}
