package com.varijon.tinies.firewand.magic;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class Fireball
{
	int duration;

	int maxDuration;
	Vec3d vec;
	Vec3d pos;
	boolean doneExisting;

	EntityPlayerMP player;
	
	public Fireball(int duration, Vec3d vec, Vec3d pos, EntityPlayerMP player) 
	{
		this.duration = 0;
		this.maxDuration = duration;
		this.vec = vec;
		//Adding speed this way doesn't work well with colliding, skips mobs
		//this.vec = new Vec3d(vec.xCoord*3, vec.yCoord*3, vec.zCoord*3);
		this.pos = pos;
		this.player = player;
		doneExisting = false;
	}
	
	public void doTick()
	{
		//check if homing, else use a different particle.. homing doesn't use vec and makes it look odd
		player.getServerWorld().spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 5, 0.05, 0.05, 0.05, 0, new int[]{});
		Vec3d morePart = new Vec3d(vec.x/10,vec.y/10,vec.z/10);
		Vec3d extraPos = pos.add(morePart);
		for(int x = 0; x <= 10; x++)
		{
			player.getServerWorld().spawnParticle(EnumParticleTypes.FLAME, extraPos.x, extraPos.y, extraPos.z, 5, 0.05, 0.05, 0.05, 0, new int[]{});
			extraPos = extraPos.add(morePart);
		}
		if(duration >= maxDuration)
		{
			explodeFireball();
			doneExisting = true;

			List<EntityLivingBase> listSplashLiving = getSplashEntityLivingBase(player.getServerWorld());
			if(listSplashLiving != null)
			{
				if(!listSplashLiving.isEmpty())
				{
					for(EntityLivingBase entityliving : listSplashLiving)
					{
						if(entityliving instanceof EntityPlayerMP)
						{
							EntityPlayerMP player = (EntityPlayerMP) entityliving;
							Vec3d direction = pos.subtract(player.getPositionVector());
							player.addVelocity(-direction.x, -direction.y, -direction.z);
							player.velocityChanged = true;
						}
					}
				}
			}
		}
		if(checkCollidedWithBlock(player.getServerWorld()))
		{			
			explodeFireball();
			doneExisting = true;

			List<EntityLivingBase> listSplashLiving = getSplashEntityLivingBase(player.getServerWorld());
			if(listSplashLiving != null)
			{
				if(!listSplashLiving.isEmpty())
				{
					for(EntityLivingBase entityliving : listSplashLiving)
					{
						if(entityliving instanceof EntityPlayerMP)
						{
							EntityPlayerMP player = (EntityPlayerMP) entityliving;
							Vec3d direction = pos.subtract(player.getPositionVector());
							player.addVelocity(-direction.x, -direction.y, -direction.z);
							player.velocityChanged = true;
						}
					}
				}
			}
		}
		List<EntityLivingBase> listLiving = checkCollidedWithEntityLiving(player.getServerWorld());
		if(listLiving != null)
		{
			boolean doExplode = false;
			if(!listLiving.isEmpty())
			{
				for(EntityLivingBase entityliving : listLiving)
				{
					if(entityliving instanceof EntityPlayerMP)
					{
						if(entityliving != player)
						{
							EntityPlayerMP player = (EntityPlayerMP) entityliving;
							Vec3d direction = pos.subtract(player.getPositionVector());
							player.addVelocity(-direction.x, -direction.y, -direction.z);
							player.velocityChanged = true;
							doExplode = true;
						}
					}
					else
					{
						entityliving.setHealth(0);
						doExplode = true;
					}
					//entityliving.setFire(5);
				}
				if(doExplode)
				{
					doneExisting = true;
					explodeFireball();					
				}
			}
		}
		duration++;
		pos = pos.add(vec);	
		
	}
	
	public void explodeFireball()
	{		
		player.getServerWorld().spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 50, 1, 1, 1, 0, new int[]{});
		doneExisting = true;

		player.getServerWorld().playSound(null, new BlockPos(pos.x, pos.y, pos.z), SoundEvents.ENTITY_FIREWORK_BLAST_FAR, SoundCategory.PLAYERS, 2f, 1f);	
	}
	
	public boolean checkCollidedWithBlock(WorldServer world)
	{
		//check each side for a block, then inverse the correct vector
		Vec3d checkPos = pos.add(vec);
		if(world.getBlockState(new BlockPos(checkPos.x, checkPos.y, checkPos.z)).getBlock() != Blocks.AIR && world.getBlockState(new BlockPos(checkPos.x, checkPos.y, checkPos.z)).isNormalCube())
		{
			return true;
		}
		return false;
	}
	
	public List<EntityLivingBase> checkCollidedWithEntityLiving(WorldServer world)
	{
		Vec3d checkPos = pos.add(vec);
		Vec3d minPos = checkPos.addVector(-0.2, -0.2, -0.2);
		Vec3d maxPos = checkPos.addVector(0.2, 0.2, 0.2);
		List<EntityLivingBase> listLiving = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z));
		return listLiving;
	}
	
	public List<EntityLivingBase> getSplashEntityLivingBase(WorldServer world)
	{
		Vec3d checkPos = pos.add(vec);
		Vec3d minPos = checkPos.addVector(-2.5, -2.5, -2.5);
		Vec3d maxPos = checkPos.addVector(2.5, 2.5, 2.5);
		List<EntityLivingBase> listLiving = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z));
		return listLiving;
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Vec3d getPos() {
		return pos;
	}

	public void setPos(Vec3d pos) {
		this.pos = pos;
	}


	public boolean isDoneExisting() {
		return doneExisting;
	}

	public void setDoneExisting(boolean doneExisting) {
		this.doneExisting = doneExisting;
	}
}
