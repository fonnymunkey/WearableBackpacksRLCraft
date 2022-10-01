package net.mcft.copy.backpacks;

import net.mcft.copy.backpacks.config.EntityListConfig;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.mcft.copy.backpacks.api.BackpackRegistry;
import net.mcft.copy.backpacks.api.BackpackRegistry.ColorRange;
import net.mcft.copy.backpacks.api.BackpackRegistry.RenderOptions;
import net.mcft.copy.backpacks.block.BlockBackpack;
import net.mcft.copy.backpacks.block.entity.TileEntityBackpack;
import net.mcft.copy.backpacks.item.ItemBackpack;
import net.mcft.copy.backpacks.item.recipe.RecipeDyeableItem;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Map;

public class BackpacksContent {
	
	public static ItemBackpack BACKPACK;
	
	public BackpacksContent() {

		BACKPACK = new ItemBackpack();
	}
	
	@SubscribeEvent
	public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockBackpack().setRegistryName(WearableBackpacks.MOD_ID, "backpack"));
		GameRegistry.registerTileEntity(TileEntityBackpack.class, new ResourceLocation(WearableBackpacks.MOD_ID, "backpack"));
	}
	
	@SubscribeEvent
	public void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(BACKPACK.setRegistryName(WearableBackpacks.MOD_ID, "backpack"));

		//May as well keep backpack entity registering in the same place
		String backpack  = BACKPACK.getRegistryName().toString();
		String idDefault = WearableBackpacks.MOD_ID + ":default";
		String idColored = WearableBackpacks.MOD_ID + ":colored";

		if(EntityListConfig.getEntityList() != null && EntityListConfig.getBackpackList() != null) {
			for(Map.Entry<String, RenderOptions> entry : EntityListConfig.getEntityList().entrySet()) {
				BackpackRegistry.registerEntity(entry.getKey(), entry.getValue());
			}
			for(Map.Entry<String, ArrayList<String[]>> entry : EntityListConfig.getBackpackList().entrySet()) {
				for(String[] subEntry : entry.getValue()) {
					try {
						boolean colored = Boolean.parseBoolean(subEntry[0]);
						int chance = Integer.parseInt(subEntry[1]);
						String loottable = subEntry[2];
						BackpackRegistry.registerBackpack(entry.getKey(), colored ? idColored : idDefault, backpack, chance, loottable, colored ? ColorRange.DEFAULT : null);
					}
					catch(Exception ex) {
						WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Failed to parse entity backpack list entry: " + ex);
						break;
					}
				}
			}
		}

		// TODO: Register all loot tables mentioned in config file?
		LootTableList.register(ItemBackpack.LOOT_TABLE);
	}
	
	public void registerRecipes() {
		ForgeRegistries.RECIPES.register(new RecipeDyeableItem()
			.setRegistryName(WearableBackpacks.MOD_ID, "dyeable_item"));
	}
	
}
