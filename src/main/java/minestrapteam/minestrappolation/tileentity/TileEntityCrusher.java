package minestrapteam.minestrappolation.tileentity;

import java.util.Random;

import minestrapteam.minestrappolation.lib.MBlocks;
import minestrapteam.minestrappolation.lib.MItems;
import minestrapteam.minestrappolation.util.AlloyRecipes;
import minestrapteam.minestrappolation.util.CrusherRecipes;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TileEntityCrusher extends TileEntityInventory implements ISidedInventory, IUpdatePlayerListBox
{
	public int					burnTime;
	public int					maxCrushTime;
	public int					crushTime;
	
	public TileEntityCrusher()
	{
		super(4);
	}
	
	public final int getMaxCrushTime()
	{
		return 150;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 4;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		this.burnTime = nbt.getShort("BurnTime");
		this.crushTime = nbt.getShort("CookTime");
		this.maxCrushTime = getItemBurnTime(this.itemStacks[1]);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setShort("BurnTime", (short) this.burnTime);
		nbt.setShort("CookTime", (short) this.crushTime);
	}
	
	public int getProgressScaled(int scalar)
	{
		return this.crushTime * scalar / this.getMaxCrushTime();
	}
	
	public int getBurnTimeRemainingScaled(int scalar)
	{
		if (this.maxCrushTime == 0)
		{
			this.maxCrushTime = 200;
		}
		
		return this.burnTime * scalar / this.maxCrushTime;
	}
	
	public boolean isBurning()
	{
		return this.burnTime > 0;
	}
	
	@Override
	public void update()
	{	
		boolean burning = this.burnTime > 0;
		
		if (burning)
		{
			this.burnTime -= 4;
		}
		
		if (this.worldObj.isRemote)
		{
			return;
		}
		
		if (this.canCrush())
		{
			if (this.burnTime == 0)
			{
				ItemStack fuel = this.itemStacks[1];
				this.maxCrushTime = this.burnTime = getItemBurnTime(fuel);
				if (this.burnTime > 0)
				{
					this.markDirty();
					if (fuel != null)
					{
						--fuel.stackSize;
						if (fuel.stackSize == 0)
						{
							this.itemStacks[1] = fuel.getItem().getContainerItem(fuel);
						}
					}
				}
			}
			else if (this.isBurning())
			{
				++this.crushTime;
				if (this.crushTime >= this.getMaxCrushTime())
				{
					this.crushTime = 0;
					this.crushItem();
					this.markDirty();
				}
			}
			else
			{
				this.crushTime = 0;
			}
		}
		else
		{
			this.crushTime = 0;
		}
		
		if (burning != this.burnTime > 0)
		{
			this.markDirty();
			this.validate();
		}
	}
	
	private boolean canCrush()
	{
		ItemStack input = this.itemStacks[0];
		if (input != null)
		{
			ItemStack output = CrusherRecipes.instance().getResult(input);
			if (output == null)
			{
				return false;
			}
			ItemStack outputSlot = this.itemStacks[2];
			if (outputSlot == null)
			{
				return true;
			}
			if (!outputSlot.isItemEqual(output))
			{
				return false;
			}
			int result = outputSlot.stackSize + output.stackSize;
			return result <= output.getMaxStackSize();
		}
		return false;
	}
	
	public void crushItem()
	{
		if (this.canCrush())
		{
			Random rand = new Random();
			int chance = rand.nextInt(15);
			
			
			ItemStack input = this.itemStacks[0];
			ItemStack output = CrusherRecipes.instance().getResult(input);
			ItemStack outputSlot = this.itemStacks[2];
			if (outputSlot == null)
			{
				this.itemStacks[2] = output.copy();
			}
			else if (outputSlot.isItemEqual(output))
			{
				outputSlot.stackSize += output.stackSize;
			}
			
			--input.stackSize;
			if (input.stackSize <= 0)
			{
				this.itemStacks[0] = null;
			}
			
			if(chance < 1 && this.itemStacks[3] == null)
			{
				this.itemStacks[3] = CrusherRecipes.instance().getExtra(input);
			}
			else if(chance < 1 && this.itemStacks[3] != null)
			{
				if(this.itemStacks[3].isItemEqual(CrusherRecipes.instance().getExtra(input)))
				{
					this.itemStacks[3].stackSize += 1;
				}
			}
		}
	}
	
	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}
	
	public static int getItemBurnTime(ItemStack stack)
	{
		if (stack == null)
		{
			return 0;
		}
		
		int i = TileEntityFurnace.getItemBurnTime(stack);
		if (i == 0)
		{
			Item item = stack.getItem();
		}
		
		return i;
	}
	
	@Override
	public boolean canInsertItem(int slotID, ItemStack stack, EnumFacing side)
	{
		return this.isItemValidForSlot(slotID, stack);
	}
	
	@Override
	public boolean canExtractItem(int slotID, ItemStack stack, EnumFacing side)
	{
		return slotID != 1 || stack.getItem() == Items.bucket;
	}	

	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		return null;
	}

}