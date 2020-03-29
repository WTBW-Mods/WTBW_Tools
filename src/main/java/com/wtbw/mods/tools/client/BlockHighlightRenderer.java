package com.wtbw.mods.tools.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.wtbw.mods.lib.util.ClientHelper;
import com.wtbw.mods.lib.util.Utilities;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
  @author: Naxanria
*/
public class BlockHighlightRenderer extends Renderer
{
  public static final BlockHighlightRenderer HARVESTABLE_BLOCKS_3x3 = new BlockHighlightRenderer(stack -> 3, (stack, blockState) -> stack.getItem().canHarvestBlock(blockState));
    
  private float distance = 6;
  private Function<ItemStack, Integer> rangeSupplier;
  private BiFunction<ItemStack, BlockState, Boolean> filterSupplier;
  private Color color = Color.GRAY;
  
  public BlockHighlightRenderer(Function<ItemStack, Integer> rangeSupplier, BiFunction<ItemStack, BlockState, Boolean> filterSupplier)
  {
    this.rangeSupplier = rangeSupplier;
    this.filterSupplier = filterSupplier;
  }
  
  public Color getColor()
  {
    return color;
  }
  
  public BlockHighlightRenderer setColor(Color color)
  {
    this.color = color;
    return this;
  }
  
  @Override
  public void render(RenderWorldLastEvent event, ItemStack stack)
  {
    if (ClientHelper.getPlayer().isCrouching())
    {
      return;
    }
    
    int range = this.rangeSupplier.apply(stack);
    BlockRayTraceResult lookingAt = Utilities.getLookingAt(ClientHelper.getPlayer(), distance);
    Direction direction = lookingAt.getFace();
    List<BlockPos> blocks = Utilities.getBlocks(lookingAt.getPos(), direction, range);
    World world = ClientHelper.getWorld();
    
    List<BlockPos> collected = blocks.stream().filter(pos ->
    {
      BlockState blockState = world.getBlockState(pos);
      return filterSupplier.apply(stack, blockState);
    }).collect(Collectors.toList());
    
    if (collected.size() > 0)
    {
      MatrixStack matrixStack = event.getMatrixStack();
      Entity renderEntity = minecraft.renderViewEntity;
      if (renderEntity == null)
      {
        return;
      }
      
      BlockPos entityPos = renderEntity.getPosition();
      
      ActiveRenderInfo info = minecraft.gameRenderer.getActiveRenderInfo();
      Vec3d view = info.getProjectedView();
      
      for (BlockPos pos : collected)
      {
//        pos = pos.offset(direction.getOpposite());
        drawShape(matrixStack, minecraft.getRenderTypeBuffers().getBufferSource().getBuffer(RenderType.getLines()), VoxelShapes.fullCube(),
          pos.getX() - view.x, pos.getY() - view.y, pos.getZ() - view.z,
          0, 0, 0, 0.4f
          );
      }
    }
  }
  
  private static void drawShape(MatrixStack matrixStackIn, IVertexBuilder bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
  {
    Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
    shapeIn.forEachEdge((p_230013_12_, p_230013_14_, p_230013_16_, p_230013_18_, p_230013_20_, p_230013_22_) ->
    {
      bufferIn.pos(matrix4f, (float) (p_230013_12_ + xIn), (float) (p_230013_14_ + yIn), (float) (p_230013_16_ + zIn)).color(red, green, blue, alpha).endVertex();
      bufferIn.pos(matrix4f, (float) (p_230013_18_ + xIn), (float) (p_230013_20_ + yIn), (float) (p_230013_22_ + zIn)).color(red, green, blue, alpha).endVertex();
    });
  }
}
