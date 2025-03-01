package net.mcft.copy.backpacks.config;

import net.mcft.copy.backpacks.WearableBackpacks;
import net.mcft.copy.backpacks.misc.BackpackSize;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

@Config(modid = WearableBackpacks.MOD_ID)
public class ModConfig {
    @Config.Comment("Server Config")
    @Config.Name("Server")
    public static final ServerConfig server = new ServerConfig();

    @Config.Comment("Client Config")
    @Config.Name("Client")
    public static final ClientConfig client = new ClientConfig();

    public static class ServerConfig {
        private BackpackSize backpackSize;
        private Potion backpackEffect;

        @Config.Comment("Whether or not the backpack should be equipped as a Bauble.")
        @Config.Name("Equip Backpack as Bauble")
        @Config.LangKey("config.wearablebackpacks.server.equipAsBauble")
        public boolean equipAsBauble = true;

        @Config.Comment("If enabled, allows equipped backpacks to be opened by other players by right clicking the target's back.")
        @Config.Name("Enable Equipped Interaction")
        @Config.LangKey("config.wearablebackpacks.general.enableEquippedInteraction")
        public boolean enableEquippedInteraction = true;

        @Config.Comment("If enabled, allows players to open their own equipped backpack without requiring it to be placed first using a keybind.")
        @Config.Name("Enable Self Interaction")
        @Config.LangKey("config.wearablebackpacks.general.enableSelfInteraction")
        public boolean enableSelfInteraction = false;

        @Config.Comment("If enabled, allows machines to interact with placed backpacks.")
        @Config.Name("Enable Machine Interaction")
        @Config.LangKey("config.wearablebackpacks.general.enableMachineInteraction")
        public boolean enableMachineInteraction = false;

        @Config.Comment("If enabled, places equipped backpacks as a block on death, instead of scattering the items all around.")
        @Config.Name("Drop as Block on Death")
        @Config.LangKey("config.wearablebackpacks.general.dropAsBlockOnDeath")
        public boolean dropAsBlockOnDeath = true;

        @Config.Comment("Should backpack placement and breaking attempt to ignore event cancellation? (Patches Lycanite's boss arena voiding backpacks)")
        @Config.Name("Backpack Ignore Cancellation")
        @Config.LangKey("config.wearablebackpacks.general.ignoreCancellation")
        public boolean ignoreCancellation = false;
        @Config.Comment("Column size of a normal backpack. Does not affect placed or equipped backpacks until turned back into an item.")
        @Config.Name("Backpack Size Column")
        @Config.LangKey("config.wearablebackpacks.size.sizeColumn")
        @Config.RangeInt(min = 1, max = 17)
        public int backpackSizeColumn = 9;

        @Config.Comment("Row size of a normal backpack. Does not affect placed or equipped backpacks until turned back into an item.")
        @Config.Name("Backpack Size Row")
        @Config.LangKey("config.wearablebackpacks.size.sizeRow")
        @Config.RangeInt(min = 1, max = 6)
        public int backpackSizeRow = 4;

        @Config.Comment("Controls whether mobs spawned naturally can randomly be wearing backpacks.")
        @Config.Name("Natural Mobs Spawn With Backpacks")
        @Config.LangKey("config.wearablebackpacks.entity.spawnNaturally")
        public boolean spawnNaturally = true;

        @Config.Comment("Controls whether mobs spawned from spawners can randomly be wearing backpacks.")
        @Config.Name("Spawned Mobs Spawn With Backpacks")
        @Config.LangKey("config.wearablebackpacks.entity.spawnFromSpawners")
        public boolean spawnFromSpawners = false;

        @Config.Comment("Whether or not to apply a potion effect to the player while wearing a backpack.")
        @Config.Name("Apply Potion Effect")
        @Config.LangKey("config.wearablebackpacks.effect.doEffect")
        public boolean doPotionEffect = false;

        @Config.Comment("Amplifier of the potion effect to be applied to the player while wearing a backpack.")
        @Config.Name("Potion Effect Amplifier")
        @Config.LangKey("config.wearablebackpacks.effect.effectAmplifier")
        @Config.RangeInt(min = 0, max = 10)
        public int potionEffectAmplifier = 0;

        @Config.Comment("The Resourcelocation of the potion effect to be applied on the player while wearing a backpack. (Ex. minecraft:weakness)")
        @Config.Name("Potion Effect Name")
        @Config.LangKey("config.wearablebackpacks.effect.effectName")
        public String potionEffectName = "";

        public void resetCache() {
            backpackSize=null;
            backpackEffect=null;
        }

        public BackpackSize getBackpackSize() {
            if(backpackSize==null) {
                backpackSize = new BackpackSize(backpackSizeColumn, backpackSizeRow);
            }
            return backpackSize;
        }

        public Potion getBackpackEffect() {
            if(backpackEffect==null) {
                backpackEffect = Potion.getPotionFromResourceLocation(potionEffectName);
                if(backpackEffect==null) {
                    WearableBackpacks.LOG.log(Level.WARN, "WearableBackpacksRLCraft: Invalid potion effect in config, returning weakness.");
                    backpackEffect = MobEffects.WEAKNESS;
                }
            }
            return backpackEffect;
        }
    }

    public static class ClientConfig {
        @Config.Comment("Controls the opacity / visibility of the enchantment effect on equipped and placed backpacks, if present. Default: 80%.")
        @Config.Name("Enchant Effect Opacity")
        @Config.LangKey("config.wearablebackpacks.cosmetic.enchantEffectOpacity")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double enchantEffectOpacity = 0.80;
    }

    @Mod.EventBusSubscriber(modid = WearableBackpacks.MOD_ID)
    private static class EventHandler{
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(WearableBackpacks.MOD_ID)) {
                ModConfig.server.resetCache();
                ConfigManager.sync(WearableBackpacks.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
