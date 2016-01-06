package minestrapteam.mods.minestrappolation.block;

import java.util.Random;

import minestrapteam.mods.minestrappolation.Config;
import minestrapteam.mods.minestrappolation.lib.MBlocks;
import minestrapteam.mods.minestrappolation.util.Chance;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDecayingCompacted extends Block
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
	public String dimension;
	
	public BlockDecayingCompacted(Material materialIn, String dimension)
	{
		super(materialIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.dimension = dimension;
	}
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }
    
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
    	worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    	ItemStack stack;
    	EntityItem item;
    	int j = ((Integer)state.getValue(AGE)).intValue();
        if(j < 15)
        {
        	worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 2);
        }
        else
        {
        	stack = (ItemStack) Chance.getRandomFromTable(dimension + "_items");
        	item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
        	worldIn.spawnEntityInWorld(item);
        	worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 2);
        }
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE});
    }
}
