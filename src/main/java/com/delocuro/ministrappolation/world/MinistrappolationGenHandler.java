package com.delocuro.ministrappolation.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.delocuro.ministrappolation.init.MinestrappolationBlocks;
import com.google.common.base.Predicate;

public class MinistrappolationGenHandler implements IWorldGenerator{
	
	public static void load()
	{
		GameRegistry.registerWorldGenerator(new MinistrappolationGenHandler(), 0);
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		//chunkX <<= 4;
		//chunkZ <<= 4;
		BlockPos pos = new BlockPos(chunkX, 1, chunkZ);
		if(chunkGenerator instanceof ChunkProviderGenerate)
		{
			this.generateOverworld(world, random, chunkX, chunkZ);
			this.genBiomeStone(world, chunkX, chunkZ, random);
		}
		else if(chunkGenerator instanceof ChunkProviderHell)
		{
			this.generateNether(world, random, chunkX, chunkZ);
		}
		else if(chunkGenerator instanceof ChunkProviderEnd)
		{
			this.generateEnd(world, random, chunkX, chunkZ);
		}
	}
	
	public void generateEnd(World world, Random rand, int x, int z)
	{
		
	}
	
	public void generateOverworld(World world, Random rand, int x, int z)
	{
		int x1 = x;
		int y1 = 0;
		int z1 = z;
		generateOre(MinestrappolationBlocks.copper_ore, world, rand, x1, z1, 3, 12, 30, 15, 100, BlockHelper.forBlock(Blocks.stone));
		generateOre(MinestrappolationBlocks.tin_ore, world, rand, x1, z1, 3, 12, 30, 15, 100, BlockHelper.forBlock(Blocks.stone));
		generateOre(MinestrappolationBlocks.sunstone_ore, world, rand, x1, z1, 2, 4, 30, 1, 200, BlockHelper.forBlock(Blocks.stone));
		generateBoulder(world, rand, x1, z1);
	}
	
	public void generateNether(World world, Random rand, int x, int z)
	{
		
	}
	
	private void generateOre(Block block, World world, Random rand, int chunkX, int chunkZ, int minVienSize, int maxVienSize, int chance, int minY, int maxY, Predicate blockType)
	{
		int vienSize = minVienSize + rand.nextInt(maxVienSize - minVienSize);
		int heightRange = maxY - minY;
		WorldGenMinable gen = new WorldGenMinable(block.getDefaultState(), vienSize, blockType);
		for(int i = 0; i < chance; i++)
		{
			int xRand = chunkX * 16 + rand.nextInt(16);
			int yRand = rand.nextInt(heightRange) + minY;
			int zRand = chunkZ * 16 + rand.nextInt(16);
			BlockPos position = new BlockPos(xRand, yRand, zRand);
			gen.generate(world, rand, position);
		}
	}
	
	
	
	private void generateBoulder(World world, Random rand, int chunkX, int chunkZ)
	{
		WorldGenBoulder gen = new WorldGenBoulder();
		for(int i = 0; i < 1; i++)
		{
			int xRand = chunkX * 16 + rand.nextInt(16);
			int yRand = rand.nextInt(100);
			int zRand = chunkZ * 16 + rand.nextInt(16);
			BlockPos position = new BlockPos(xRand, yRand, zRand);
			gen.generate(world, rand, position);
		}
	}
	
	public void genBiomeStone(World world, int chunkX, int chunkZ, Random random)
	{
		BlockPos pos = new BlockPos(chunkX, 0, chunkZ);
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		WorldChunkManager chunkManager = world.getWorldChunkManager();

		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				BlockPos subpos = new BlockPos(x,0,z);
				BiomeGenBase biome = chunk.getBiome(subpos, chunkManager);
				Block stoneBlock = Blocks.cobblestone;
				Block deepStoneBlock = Blocks.mossy_cobblestone;
				int oreMetadata = 0;
				int deepOreMetadata = 0;
				int deepStoneDepth = 0;
				
				if (biome.temperature < 0.2F)
				{
					stoneBlock = Blocks.snow;
					deepStoneBlock = Blocks.packed_ice;
					deepStoneDepth = random.nextInt(5) + 30;
					oreMetadata = 10;
					deepOreMetadata = 11;
				}
				else if (biome.temperature <0.4F)
				{
					stoneBlock = Blocks.lapis_block;
					deepStoneBlock = Blocks.diamond_block;
					deepStoneDepth = random.nextInt(5) + 35;
					oreMetadata = 8;
					deepOreMetadata = 9;
				}
				else if (biome.getTempCategory() == TempCategory.OCEAN)
				{
					stoneBlock = Blocks.sandstone;
					deepStoneBlock = Blocks.red_sandstone;
					deepStoneDepth = random.nextInt(5) + 20;
					oreMetadata = 12;
					deepOreMetadata = 13;
				}
				else if (biome.temperature >= 1.0F)
				{
					stoneBlock = Blocks.netherrack;
					deepStoneBlock = Blocks.nether_brick;
					deepStoneDepth = random.nextInt(5) + 35;
					oreMetadata = 6;
					deepOreMetadata = 7;
				}
				else
				{
					deepStoneBlock = Blocks.mossy_cobblestone;
					deepStoneDepth = random.nextInt(5) + 35;
					deepOreMetadata = 5;
				}
				
				for (int y = 128; y >= 0; y--)
				{
					BlockPos subpos2 = new BlockPos(x,y,z);
					Block block = chunk.getBlock(x, y, z);
					IBlockState biomeStoneBlock = stoneBlock.getDefaultState();
			
					if (block == Blocks.stone)
					{
						System.out.println("Replacable Stone Detected");
						if (y < deepStoneDepth)
						{
							chunk.setBlockState(subpos2, deepStoneBlock.getDefaultState());
							System.out.println("Placed DeepStone Block at " + subpos);
						}
						else
						{
							chunk.setBlockState(subpos2, stoneBlock.getDefaultState());
							System.out.println("Placed Stone Block at " + subpos2.getX() + ", " + subpos2.getY() + ", " + subpos2.getZ() + ".");
						}
					}
				}
			}
		}
	}
	
	
}
