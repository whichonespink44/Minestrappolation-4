package minestrapteam.mods.minestrappolation.item.blocks;

import minestrapteam.mods.minestrappolation.enumtypes.MStoneType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLampGlowstone extends ItemBlock
{

	public ItemBlockLampGlowstone(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damageValue)
	{
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack item)
	{
		return MStoneType.byMetadata(item.getItemDamage()) + "_lamp_glowstone";
	}
}
