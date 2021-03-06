package minestrapteam.mods.minestrappolation.block;

import minestrapteam.mods.minestrappolation.Config;
import minestrapteam.mods.minestrappolation.lib.MItems;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockMooshroom extends BlockFlesh
{
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

	public BlockMooshroom(Material materialIn, MapColor mapColorIn)
	{
		super(materialIn, mapColorIn);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTickRandomly(true);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int j = state.getValue(AGE).intValue();
		if (j < 5)
		{
			int chance = rand.nextInt(Config.bushGrowChance);
			if (chance == 1)
			{
				worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 2);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		if (playerIn.getCurrentEquippedItem().getItem() == Items.bowl)
		{
			if (playerIn.getCurrentEquippedItem().stackSize > 1)
			{
				playerIn.getCurrentEquippedItem().stackSize--;
				EntityItem ei = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
				                               new ItemStack(Items.mushroom_stew, 1));
				worldIn.spawnEntityInWorld(ei);
				if (playerIn instanceof FakePlayer)
					ei.onCollideWithPlayer(playerIn);
				return true;
			}
			else
			{
				playerIn.destroyCurrentEquippedItem();
				EntityItem ei = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
				                               new ItemStack(Items.mushroom_stew, 1));
				worldIn.spawnEntityInWorld(ei);
				if (playerIn instanceof FakePlayer)
					ei.onCollideWithPlayer(playerIn);
				return true;
			}
		}
		if (playerIn.getCurrentEquippedItem().getItem() == MItems.bread_bowl)
		{
			if (playerIn.getCurrentEquippedItem().stackSize > 1)
			{
				playerIn.getCurrentEquippedItem().stackSize--;
				EntityItem ei = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
				                               new ItemStack(MItems.bread_mushroom_stew, 1));
				worldIn.spawnEntityInWorld(ei);
				if (playerIn instanceof FakePlayer)
					ei.onCollideWithPlayer(playerIn);
				return true;
			}
			else
			{
				playerIn.destroyCurrentEquippedItem();
				EntityItem ei = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
				                               new ItemStack(MItems.bread_mushroom_stew, 1));
				worldIn.spawnEntityInWorld(ei);
				if (playerIn instanceof FakePlayer)
					ei.onCollideWithPlayer(playerIn);
				return true;
			}
		}
		if (state.getValue(AGE) == 5 && playerIn.getCurrentEquippedItem().getItem() == Items.shears)
		{
			if (playerIn.getCurrentEquippedItem().getItemDamage() < playerIn.getCurrentEquippedItem().getMaxDamage())
			{
				playerIn.getCurrentEquippedItem().damageItem(1, playerIn);
			}
			else
			{
				playerIn.destroyCurrentEquippedItem();
			}
			worldIn.setBlockState(pos, this.getDefaultState(), 2);
			EntityItem ei = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ,
			                               new ItemStack(Blocks.red_mushroom, 1));
			worldIn.spawnEntityInWorld(ei);
			if (playerIn instanceof FakePlayer)
				ei.onCollideWithPlayer(playerIn);
			return true;
		}
		return false;
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
