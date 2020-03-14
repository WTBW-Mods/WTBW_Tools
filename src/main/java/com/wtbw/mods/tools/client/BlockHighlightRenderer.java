package com.wtbw.mods.tools.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.wtbw.mods.lib.ClientSetup;
import com.wtbw.mods.lib.util.ColorUtil;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.tools.item.tools.ExcavatorItem;
import com.wtbw.mods.tools.item.tools.GreatAxeItem;
import com.wtbw.mods.tools.item.tools.HammerItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
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
    int range = this.rangeSupplier.apply(stack);
    BlockRayTraceResult lookingAt = Utilities.getLookingAt(ClientSetup.getPlayer(), distance);
    Direction direction = lookingAt.getFace();
    List<BlockPos> blocks = Utilities.getBlocks(lookingAt.getPos(), direction, range);
    World world = ClientSetup.getWorld();
    
    List<BlockPos> collected = blocks.stream().filter(pos ->
    {
      BlockState blockState = world.getBlockState(pos);
      return filterSupplier.apply(stack, blockState);
    }).collect(Collectors.toList());
  
    if (collected.size() > 0)
    {
      Vec3d view = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
  
      MatrixStack matrix = event.getMatrixStack();
      matrix.push();
      matrix.translate(-view.x, -view.y, -view.y);
  
      IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
      IVertexBuilder builder = buffer.getBuffer(RenderType.lines());
      
      collected.forEach(pos ->
      {
        matrix.push();;
        matrix.translate(pos.getX(), pos.getY(), pos.getZ());
        final float off = -0.0005f;
        matrix.translate(off, off, off);
        matrix.scale(1, 1, 1);
        matrix.rotate(Vector3f.YP.rotationDegrees(-90f));
  
        Matrix4f posMatrix = matrix.getLast().getPositionMatrix();
  
        float[] cols = ColorUtil.getRGBAf(color.getRGB());
        renderLines(posMatrix, builder, cols);
        matrix.pop();
      });
      
      matrix.pop();
      RenderSystem.disableDepthTest();
      buffer.finish(RenderType.lines());
    }
  }
  
  private void renderLines(Matrix4f matrix, IVertexBuilder builder, float[] cols)
  {
    float a = cols[3];
    float r = cols[0];
    float g = cols[1];
    float b = cols[2];
  
    //A
    builder.pos(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 0, 0).color(r, g, b, a).endVertex();
  
    //B
    builder.pos(matrix, 1, 0, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 0, -1).color(r, g, b, a).endVertex();
  
    //C
    builder.pos(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 0, 0, -1).color(r, g, b, a).endVertex();
  
    //D
    builder.pos(matrix, 0, 0, -1).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 0, -1).color(r, g, b, a).endVertex();
  
    //E
    builder.pos(matrix, 0, 0, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 0, 1, 0).color(r, g, b, a).endVertex();
  
    //F
    builder.pos(matrix, 1, 0, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 1, 0).color(r, g, b, a).endVertex();
  
    //G
    builder.pos(matrix, 1, 0, -1).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 1, -1).color(r, g, b, a).endVertex();
  
    //H
    builder.pos(matrix, 0, 0, -1).color(r, g, b, a).endVertex();
    builder.pos(matrix, 0, 1, -1).color(r, g, b, a).endVertex();
  
    //I
    builder.pos(matrix, 0, 1, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 0, 1, -1).color(r, g, b, a).endVertex();
  
    //J
    builder.pos(matrix, 0, 1, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 1, 0).color(r, g, b, a).endVertex();
  
    //K
    builder.pos(matrix, 1, 1, 0).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 1, -1).color(r, g, b, a).endVertex();
  
    //L
    builder.pos(matrix, 0, 1, -1).color(r, g, b, a).endVertex();
    builder.pos(matrix, 1, 1, -1).color(r, g, b, a).endVertex();
  }
}
