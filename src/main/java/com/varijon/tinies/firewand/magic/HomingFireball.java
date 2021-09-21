package com.varijon.tinies.firewand.magic;

import java.util.List;

import net.minecraft.entity.EntityLiving;
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

public class HomingFireball
{
	int duration;

	int maxDuration;
	Vec3d vec;
	Vec3d pos;
	boolean doneExisting;
	EntityLivingBase target;
	int soundCount;

	EntityPlayerMP player;
	
	//Make a specific mob kill it
	
	public HomingFireball(int duration, Vec3d vec, Vec3d pos, EntityPlayerMP player) 
	{
		this.duration = 0;
		this.maxDuration = duration;
		this.vec = vec;
		//Adding speed this way doesn't work well with colliding, skips mobs
		this.vec = new Vec3d(vec.x/3, vec.y/3, vec.z/3);
		this.pos = pos;
		this.player = player;
		doneExisting = false;
		target = null;
		soundCount = 0;
	}
	
	public void doTick()
	{
		target = null;
		player.getServerWorld().spawnParticle(EnumParticleTypes.END_ROD, pos.x, pos.y, pos.z, 5, 0.1, 0.1, 0.1, 0, new int[]{});
		if(target == null)
		{
			Vec3d checkPos = pos.add(vec);
			Vec3d minPos = checkPos.addVector(-20, -20, -20);
			Vec3d maxPos = checkPos.addVector(20, 20, 20);
			
			List<EntityLivingBase> listLiving = player.getServerWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z));
			
			double closest = 50000;
			for(EntityLivingBase living : listLiving)
			{
				if(living.getPositionVector().distanceTo(checkPos) < closest && living != player)
				{
					closest = living.getPositionVector().distanceTo(checkPos);
					target = living;
				}
			}
		}
		if(duration >= maxDuration)
		{
			explodeFireball();
			doneExisting = true;

		}
		if(checkCollidedWithBlock(player.getServerWorld()))
		{
			// lol this makes it bounce straight back
			
			explodeFireball();
			doneExisting = true;

		}
		List<EntityLivingBase> listLiving = checkCollidedWithEntityLiving(player.getServerWorld());
		if(listLiving != null)
		{
			if(!listLiving.isEmpty())
			{
				for(EntityLivingBase entityliving : listLiving)
				{
					if(entityliving != player)
					{
						target = null;
						entityliving.setHealth(0);
						entityliving.setDead();
						//entityliving.setFire(5);
						explodeFireball();
					}
				}

			}
		}
		duration++;
		if(target != null)
		{
			Vec3d higher = target.getPositionVector();
			higher.addVector(0, 1, 0);
			Vec3d temp = pos.subtract(higher).normalize();
			Vec3d temp2 = new Vec3d(temp.x/25, temp.y/25, temp.z/25);
			pos = pos.subtract(temp2);
			
		}
//		else
//		{
//			pos = pos.add(vec);			
//		}
		
	}

	public void doSound()
	{
		if(soundCount == 60)
		{
			player.getServerWorld().playSound(null, new BlockPos(pos.x, pos.y, pos.z), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.PLAYERS, 0.5f, 1f);	
			soundCount = 0;
		}
		soundCount++;
	}
	public void explodeFireball()
	{		
		player.getServerWorld().spawnParticle(EnumParticleTypes.END_ROD, pos.x, pos.y, pos.z, 50, 1, 1, 1, 0, new int[]{});

		player.getServerWorld().playSound(null, new BlockPos(pos.x, pos.y, pos.z), SoundEvents.ENTITY_FIREWORK_BLAST_FAR, SoundCategory.PLAYERS, 2f, 1f);	
	}
	
	public boolean checkCollidedWithBlock(WorldServer world)
	{
		//check each side for a block, then inverse the correct vector
		Vec3d checkPos = pos.add(vec);
		if(world.getBlockState(new BlockPos(checkPos.x, checkPos.y, checkPos.z)).getBlock() == Blocks.BEDROCK)
		{
			return true;
		}
		return false;
	}
	
	public List<EntityLivingBase> checkCollidedWithEntityLiving(WorldServer world)
	{
		Vec3d checkPos = pos.add(vec);
		Vec3d minPos = checkPos.addVector(-0.3, -0.3, -0.3);
		Vec3d maxPos = checkPos.addVector(0.3, 0.3, 0.3);
		List<EntityLivingBase> listLiving = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z));
		return listLiving;
	}
	
	public List<EntityLiving> getSplashEntityLiving(WorldServer world)
	{
		Vec3d checkPos = pos.add(vec);
		Vec3d minPos = checkPos.addVector(-1.5, -1.5, -1.5);
		Vec3d maxPos = checkPos.addVector(1.5, 1.5, 1.5);
		List<EntityLiving> listLiving = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(minPos.x, minPos.y, minPos.z, maxPos.x, maxPos.y, maxPos.z));
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
