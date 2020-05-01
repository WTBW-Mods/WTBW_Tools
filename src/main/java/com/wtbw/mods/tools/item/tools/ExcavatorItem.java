package com.wtbw.mods.tools.item.tools;

import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.tools.config.CommonConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.List;

/*
  @author: Naxanria
*/
public class ExcavatorItem extends ShovelItem
{
  public ExcavatorItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder)
  {
    super(tier, attackDamageIn, attackSpeedIn, builder);
  }
  
  @Override
  public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entity)
  {
    if (!world.isRemote)
    {
      if (state.getBlockHardness(world, pos) == 0)
      {
        return true;
      }
      
      if (entity instanceof PlayerEntity)
      {
        PlayerEntity player = (PlayerEntity) entity;
        if (!player.isCrouching())
        {
          breakNeighbours(world, pos, (ServerPlayerEntity) player, true, stack);
          return true;
        }
        else
        {
//          stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) player);
          stack.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        }
      }
      
      
    }
    
    return true;
  }
  
  @Override
  public boolean canHarvestBlock(BlockState state)
  {
    if (state.getBlock() == Blocks.BEDROCK)
    {
      return false;
    }
    /*|| state.isIn(BlockTags.field_219750_S DIRT_LIKE)*/
    return state.isIn(BlockTags.SAND) || state.isIn(Tags.Blocks.DIRT) || state.isIn(Tags.Blocks.GRAVEL) || state.getBlock() == Blocks.SOUL_SAND;
  }
  
  private void breakNeighbours(World world, BlockPos pos, ServerPlayerEntity player, boolean damageItem, ItemStack stack)
  {
    BlockRayTraceResult rayTraceResult = Utilities.getLookingAt(player, 6);
    Direction facing = rayTraceResult.getFace();
    
    List<BlockPos> brokenBlocks = Utilities.getBlocks(pos, facing);
    for (BlockPos blockPos : brokenBlocks)
    {
      BlockState blockState = world.getBlockState(blockPos);
      if (!canHarvestBlock(blockState))
      {
        continue;
      }
      
      if (blockState.getBlock().hasTileEntity(blockState))
      {
        continue;
      }
      
      world.destroyBlock(blockPos, false);
      
      if (!player.isCreative())
      {
        if (damageItem)
        {
          player.getHeldItemMainhand().damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(EquipmentSlotType.MAINHAND));//attemptDamageItem(1, world.rand, player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player : null);
        }
        
        Utilities.dropItems(world, Block.getDrops(blockState, (ServerWorld) world, blockPos, null, player, player.getHeldItemMainhand()), blockPos);
        Utilities.spawnExp(world, blockPos, blockState, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack), EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack));
        blockState.spawnAdditionalDrops(world, blockPos, player.getHeldItemMainhand());
      }
    }
  }
  
  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state)
  {
    return super.getDestroySpeed(stack, state) / CommonConfig.instance().areaToolsSpeedModifier.get().floatValue();
  }
}
