package com.varijon.tinies.firewand;

import com.varijon.tinies.firewand.magic.FireballProjectileHandler;
import com.varijon.tinies.firewand.magic.HomingBoltHandler;
import com.varijon.tinies.firewand.magic.MagicWandHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid="firewand", version="1.0.0", acceptableRemoteVersions="*")
public class FireWand
{
	public static String MODID = "modid";
	public static String VERSION = "version";

		
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{

	}
	
	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		MinecraftForge.EVENT_BUS.register(new MagicWandHandler());
		MinecraftForge.EVENT_BUS.register(new FireballProjectileHandler());
		MinecraftForge.EVENT_BUS.register(new HomingBoltHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
	}

	 @EventHandler
	 public void serverLoad(FMLServerStartingEvent event)
	 {	 
	 }
}