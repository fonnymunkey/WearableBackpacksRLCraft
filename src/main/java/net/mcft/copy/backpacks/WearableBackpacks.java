package net.mcft.copy.backpacks;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.SidedProxy;

import net.mcft.copy.backpacks.network.BackpacksChannel;

// TODO: Add achievement(s)! <3

@Mod(modid = WearableBackpacks.MOD_ID, name = WearableBackpacks.MOD_NAME,
     version = WearableBackpacks.VERSION, dependencies = "required-after:forge@[14.23.5.2855,);required-after:baubles@[1.5.2,)")
public class WearableBackpacks {
	
	public static final String MOD_ID   = "wearablebackpacks";
	public static final String MOD_NAME = "Wearable Backpacks RLCraft";
	public static final String VERSION  = "3.2.1";
	
	@Instance
	public static WearableBackpacks INSTANCE;
	
	@SidedProxy(serverSide = "net.mcft.copy.backpacks.ProxyCommon",
	            clientSide = "net.mcft.copy.backpacks.ProxyClient")
	public static ProxyCommon PROXY;
	
	public static Logger LOG;
	public static BackpacksChannel CHANNEL;
	public static BackpacksContent CONTENT;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOG = event.getModLog();
		CHANNEL = new BackpacksChannel();

		//CONFIG.load();
		//CONFIG.save();
		
		CONTENT = new BackpacksContent();
		CONTENT.registerRecipes();
		
		PROXY.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//CONFIG.init();
		WearableBackpacks.PROXY.initBackpackLayers();
		PROXY.init();
	}
	
}
