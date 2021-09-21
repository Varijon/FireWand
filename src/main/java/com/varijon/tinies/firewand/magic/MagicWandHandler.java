package com.varijon.tinies.firewand.magic;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class MagicWandHandler 
{
	MinecraftServer server;
	
	public MagicWandHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	@SubscribeEvent
	public void onRightClick (PlayerInteractEvent.RightClickItem event)
	{
		ItemStack wand = null;
		ItemStack homeWand = null;
		if(event.getHand() != EnumHand.OFF_HAND)
		{
			wand = getMagicWand(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND));
			homeWand = getHomingMagicWand(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND));			
		}
		else
		{
			wand = getMagicWand(event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND));
			homeWand = getHomingMagicWand(event.getEntityPlayer().getHeldItem(EnumHand.OFF_HAND));						
		}
		if(wand != null)
		{
			float yaw = event.getEntityPlayer().rotationYaw;
			float pitch = event.getEntityPlayer().rotationPitch;
			double motionX = (double)(-MathHelper.sin(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI));
			double motionZ = (double)(MathHelper.cos(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI));
			double motionY = (double)(-MathHelper.sin((pitch) / 180.0F * (float)Math.PI));
			Vec3d vec = new Vec3d(motionX, motionY, motionZ);
			Vec3d pos = event.getEntityPlayer().getPositionVector().addVector(0, 1, 0);
			FireballProjectileHandler.createFireball(50, vec, pos, (EntityPlayerMP) event.getEntityPlayer());
			event.getWorld().playSound(null, new BlockPos(pos.x, pos.y, pos.z), SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.PLAYERS, 1f, 1f);
		}
		if(homeWand != null)
		{
			float yaw = event.getEntityPlayer().rotationYaw;
			float pitch = event.getEntityPlayer().rotationPitch;
			double motionX = (double)(-MathHelper.sin(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI));
			double motionZ = (double)(MathHelper.cos(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI));
			double motionY = (double)(-MathHelper.sin((pitch) / 180.0F * (float)Math.PI));
			Vec3d vec = new Vec3d(motionX, motionY, motionZ);
			Vec3d pos = event.getEntityPlayer().getPositionVector().addVector(0, 1, 0);
			HomingBoltHandler.createFireball(15000, vec, pos, (EntityPlayerMP) event.getEntityPlayer());
			event.getWorld().playSound(null, new BlockPos(pos.x, pos.y, pos.z), SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
		}
	}
	
	public ItemStack getMagicWand(ItemStack item)
	{
		if(item != null)
		{
			if(item.hasTagCompound())
			{
				NBTTagCompound nbt = item.getTagCompound();
				if(nbt.hasKey("isFireWand"))
				{
					return item;
				}
			}
		}
		return null;
	}
	public ItemStack getHomingMagicWand(ItemStack item)
	{
		if(item != null)
		{
			if(item.hasTagCompound())
			{
				NBTTagCompound nbt = item.getTagCompound();
				if(nbt.hasKey("isHomingMagicWand"))
				{
					return item;
				}
			}
		}
		return null;
	}
}
