package com.varijon.tinies.firewand.magic;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class FireballProjectileHandler 
{
	int counter = 0;
	MinecraftServer server;
	
	static ArrayList<Fireball> listFireballs;
	
	public FireballProjectileHandler()
	{
		server = FMLCommonHandler.instance().getMinecraftServerInstance();
		listFireballs = new ArrayList<Fireball>();
	}

	@SubscribeEvent
	public void onWorldTick (WorldTickEvent event)
	{
//		if(counter == 1)
//		{
			ArrayList<Fireball> listRemoveFireball = new ArrayList<Fireball>();
			for(Fireball fireball : listFireballs)
			{
				//This does seem to speed it up, while collision works, do split off particles
				fireball.doTick();
				fireball.doTick();
				fireball.doTick();
				fireball.doTick();
				if(fireball.doneExisting)
				{
					listRemoveFireball.add(fireball);
				}
			}
			for(Fireball fireball : listRemoveFireball)
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
		listFireballs.add(new Fireball(duration, vec, pos, player));
	}
}
