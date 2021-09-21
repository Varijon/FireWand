package com.varijon.tinies.firewand.magic;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class HomingBoltHandler 
{
	int counter = 0;
	MinecraftServer server;
	
	static ArrayList<HomingFireball> listFireballs;
	
	public HomingBoltHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
		listFireballs = new ArrayList<HomingFireball>();
	}

	@SubscribeEvent
	public void onWorldTick (WorldTickEvent event)
	{
//		if(counter == 1)
//		{
			ArrayList<HomingFireball> listRemoveFireball = new ArrayList<HomingFireball>();
			for(HomingFireball fireball : listFireballs)
			{
				//This does seem to speed it up, while collision works, do split off particles
				fireball.doTick();
				fireball.doSound();
				if(fireball.doneExisting)
				{
					listRemoveFireball.add(fireball);
				}
			}
			for(HomingFireball fireball : listRemoveFireball)
			{
				listFireballs.remove(fireball);
			}
			counter = 0;
			return;
//		}
//		counter++;
	}
	
	public static void createFireball(int duration, Vec3d vec, Vec3d pos, EntityPlayerMP player)
	{
		listFireballs.add(new HomingFireball(duration, vec, pos, player));
	}
}
