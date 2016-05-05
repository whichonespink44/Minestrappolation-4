package minestrapteam.mods.minestrappolation.block;

import minestrapteam.mods.minestrappolation.lib.MBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockVirtianGrass extends BlockGrass
{

	public BlockVirtianGrass()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
		this.setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return this.getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return 8494438;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) < 4
				    && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2)
			{
				worldIn.setBlockState(pos, MBlocks.virtian_soil.getDefaultState());
			}
			else
			{
				if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

						if (iblockstate1 == MBlocks.virtian_soil.getDefaultState()
							    && worldIn.getLightFromNeighbors(blockpos1.up()) >= 4
							    && block.getLightOpacity(worldIn, blockpos1.up()) <= 2)
						{
							worldIn.setBlockState(blockpos1, MBlocks.virtian_grass.getDefaultState());
						}
					}
				}
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return MBlocks.virtian_soil.getItemDropped(MBlocks.virtian_soil.getDefaultState(), rand, fortune);
	}
}