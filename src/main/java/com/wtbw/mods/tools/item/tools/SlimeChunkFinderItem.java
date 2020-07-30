package com.wtbw.mods.tools.item.tools;

import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;


/*
  @author: Naxanria
*/
public class SlimeChunkFinderItem extends Item
{
  public static final String BASE_KEY = "wtbw_tools.text.slime_chunk_finder.";
  
  public SlimeChunkFinderItem(Properties properties)
  {
    super(properties);
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
  {
    ItemStack stack = player.getHeldItem(hand);
    
    if (!world.isRemote)
    {
      //todo: check if dimension has slime chunks at all...
      
      if (world.func_234922_V_() != DimensionType.OVERWORLD)
      {
        
        player.sendMessage(TextComponentBuilder.createTranslated(BASE_KEY + "no_slime_dimension").red().build(), Util.DUMMY_UUID);
      }
      else
      {
        if (Utilities.isSlimeChunk(world, player.getPosition()))
        {
          player.sendMessage(TextComponentBuilder.createTranslated(BASE_KEY + "found").aqua().build(), Util.DUMMY_UUID);
        }
        else
        {
          int searchRadius = 20;
          int playerX = player.getPosition().getX();
          int playerZ = player.getPosition().getZ();
  
          BlockPos.Mutable mutable = new BlockPos.Mutable();
  
          ChunkPos found = null;
          int distance = Integer.MAX_VALUE;
          ChunkPos in = new ChunkPos(player.getPosition());
  
          for (int x = -searchRadius / 2; x < searchRadius / 2; x++)
          {
            for (int z = -searchRadius / 2; z < searchRadius / 2; z++)
            {
              // can skip current chunk as we already checked it
              if (x == 0 && z == 0)
              {
                continue;
              }
  
              mutable.setPos(playerX + x * 16, 0, playerZ + z * 16);
              boolean slimeChunk = Utilities.isSlimeChunk(world, mutable);
  
              if (slimeChunk)
              {
                ChunkPos chunkPos = new ChunkPos(mutable);
                if (found != null)
                {
                  int dist;
                  if ((dist = Utilities.distanceSqr(chunkPos, in)) < distance)
                  {
                    distance = dist;
                    found = chunkPos;
                  }
                }
                else
                {
                  found = chunkPos;
                  distance = Utilities.distanceSqr(chunkPos, in);
                }
              }
            }
          }
  
          if (found != null)
          {
            Direction direction = Direction.getFacingFromVector(found.x - in.x, 0, found.z - in.z);
            player.sendMessage(TextComponentBuilder.createTranslated(BASE_KEY + "direction", direction.getName2()).aqua().build(), Util.DUMMY_UUID);
          }
          else
          {
            player.sendMessage(TextComponentBuilder.createTranslated(BASE_KEY + "not_found").red().build(), Util.DUMMY_UUID);
          }
        }
  
        stack.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(hand));
      }
    }
  
    return ActionResult.resultSuccess(stack);
  }
}
