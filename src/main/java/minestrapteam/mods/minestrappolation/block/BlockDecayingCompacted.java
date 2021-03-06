package minestrapteam.mods.minestrappolation.block;

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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

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

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		worldIn
			.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 0.5D, (double) pos.getX() + 2.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 2.5D);
		worldIn
			.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 0.5D, (double) pos.getX() + 2.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() - 1.5D);
		worldIn
			.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 0.5D, (double) pos.getX() - 1.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 2.5D);
		worldIn
			.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() + 0.5D, (double) pos.getX() - 1.5D, (double) pos.getY() + 0.5D,
			               (double) pos.getZ() - 1.5D);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		ItemStack stack;
		EntityItem item;
		int j = state.getValue(AGE).intValue();
		if (j < 15)
		{
			worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 2);
		}
		else
		{
			stack = (ItemStack) Chance.getRandomFromTable(this.dimension + "_items");
			int quantity = rand.nextInt(stack.stackSize);
			ItemStack newstack = new ItemStack(stack.getItem(), quantity + 1, stack.getItemDamage());
			item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), newstack.copy());
			worldIn.spawnEntityInWorld(item);
			worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 2);
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE).intValue();
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, AGE);
	}
}
