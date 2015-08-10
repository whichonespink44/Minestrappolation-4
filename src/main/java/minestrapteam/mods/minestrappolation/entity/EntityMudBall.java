package minestrapteam.mods.minestrappolation.entity;

import java.util.Random;

import minestrapteam.mods.minestrappolation.lib.MItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityMudBall extends EntityThrowable
{
    public EntityMudBall(World worldIn)
    {
        super(worldIn);
    }

    public EntityMudBall(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityMudBall(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition pos)
    {
        if (pos.entityHit != null)
        {
            Random rand = new Random();
        	int dam = rand.nextInt(2);
        	pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)dam);
        }

        for (int i = 0; i < 8; ++i)
        {
        	this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(MItems.mud_ball)});
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}