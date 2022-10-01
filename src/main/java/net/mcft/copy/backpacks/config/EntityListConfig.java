package net.mcft.copy.backpacks.config;

import net.mcft.copy.backpacks.WearableBackpacks;
import net.mcft.copy.backpacks.api.BackpackRegistry;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class EntityListConfig {
    private static File configFolder = null;
    private static File entityListFile = null;
    private static File backpackListFile = null;

    private static final Map<String, BackpackRegistry.RenderOptions> entityList = new HashMap<>();
    private static final Map<String, ArrayList<String[]>> backpackList = new HashMap<>();

    @Nullable
    public static Map<String, BackpackRegistry.RenderOptions>  getEntityList() {
        if(entityListFile==null && !initEntityList()) return null;
        return entityList;
    }

    @Nullable
    public static Map<String, ArrayList<String[]>> getBackpackList() {
        if(backpackListFile==null && !initBackpackList()) return null;
        return backpackList;
    }

    private static boolean initDirectory() {
        configFolder = new File("config", WearableBackpacks.MOD_ID);
        if(!configFolder.exists() || !configFolder.isDirectory()) {
            if(!configFolder.mkdir()) {
                WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Could not create the folder for configuration.");
                return false;
            }
        }
        return true;
    }

    private static boolean initEntityList() {
        if(configFolder==null && !initDirectory()) return false;
        entityListFile = new File(configFolder, WearableBackpacks.MOD_ID + "entities.cfg");
        try {
            if(!entityListFile.exists()) {
                if(!entityListFile.createNewFile()) {
                    WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Failed to create new entity list file.");
                    return false;
                }
                Files.write(entityListFile.toPath(),
                        (       "//List of Entity entries and their backpack render options, one entry per line.\n" +
                                "//String entityName, double Y, double Z, double rotation, double scale\n" +
                                "//Defaults:\n" +
                                "minecraft:zombie,0.0,2.5,0.0,0.8\n" +
                                "minecraft:skeleton,0.0,2.5,0.0,0.8\n" +
                                "minecraft:zombie_pigman,0.0,2.5,0.0,0.8\n"
                        ).getBytes(StandardCharsets.UTF_8));
            }
            List<String> list = Files.lines(entityListFile.toPath())
                    .map(String::trim)
                    .filter(s -> !s.startsWith("//"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            for(String entry : list) {
                String[] entryArray = Arrays.stream(entry.split(",")).map(String::trim).toArray(String[]::new);
                entityList.put(entryArray[0], new BackpackRegistry.RenderOptions(
                                Double.parseDouble(entryArray[1]),
                                Double.parseDouble(entryArray[2]),
                                Double.parseDouble(entryArray[3]),
                                Double.parseDouble(entryArray[4])));
            }
            return true;
        }
        catch(Exception ex) {
            WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Failed to initialize entity list file: " + ex);
            return false;
        }
    }

    private static boolean initBackpackList() {
        if(configFolder==null && !initDirectory()) return false;
        backpackListFile = new File(configFolder, WearableBackpacks.MOD_ID + "entitybackpacks.cfg");
        try {
            if(!backpackListFile.exists()) {
                if(!backpackListFile.createNewFile()) {
                    WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Failed to create new entity backpack list file.");
                    return false;
                }
                Files.write(backpackListFile.toPath(),
                        (       "//List of Entities and their backpack entries, one entry per line.\n" +
                                "//Entities in this file must first be declared in wearablebackpacksentities.cfg.\n" +
                                "//Chance is 1 in X of appearing on entity.\n" +
                                "//String entityName, boolean colored, int chance, String loottable\n" +
                                "//Defaults:\n" +
                                "minecraft:zombie,false,800,wearablebackpacks:backpack/default\n" +
                                "minecraft:zombie,true,8000,wearablebackpacks:backpack/default\n" +
                                "minecraft:skeleton,false,1200,wearablebackpacks:backpack/default\n" +
                                "minecraft:skeleton,true,12000,wearablebackpacks:backpack/default\n" +
                                "minecraft:zombie_pigman,true,1000,wearablebackpacks:backpack/default\n"
                        ).getBytes(StandardCharsets.UTF_8));
            }
            List<String> list = Files.lines(backpackListFile.toPath())
                    .map(String::trim)
                    .filter(s -> !s.startsWith("//"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            for(String entry : list) {
                String[] entryArray = Arrays.stream(entry.split(",")).map(String::trim).toArray(String[]::new);
                if(backpackList.get(entryArray[0]) != null) {
                    backpackList.get(entryArray[0]).add(new String[]{entryArray[1],entryArray[2],entryArray[3]});
                }
                else {
                    ArrayList<String[]> array = new ArrayList<>();
                    array.add(new String[]{entryArray[1],entryArray[2],entryArray[3]});
                    backpackList.put(entryArray[0], array);
                }
            }
            return true;
        }
        catch(Exception ex) {
            WearableBackpacks.LOG.log(Level.ERROR, WearableBackpacks.MOD_ID + ": " + "Failed to initialize entity backpack list file: " + ex);
            return false;
        }
    }
}