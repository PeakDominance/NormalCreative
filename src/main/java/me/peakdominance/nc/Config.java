package me.peakdominance.nc;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fabricmc.loader.api.FabricLoader;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Config {
    public static final Path configFilePath = FabricLoader.getInstance().getGameDir().resolve(".ncreative");
    public JSONObject configObject = new JSONObject();

    public Config() {
        try {
            if (!Files.exists(configFilePath)) {
                this.configObject = generateDefaultConfig();
                Files.writeString(configFilePath, generateDefaultConfig().toString());
                NormalCreative.LOGGER.info("Created config file: {}", configFilePath);
                System.out.println(configObject);
            } else {
                String configContent = Files.readString(configFilePath);
                this.configObject = new JSONObject(new ObjectMapper().readValue(configContent, HashMap.class));
            }
        } catch (IOException e) {
            NormalCreative.LOGGER.info("Failed to create config file; an I/O error occurred or the parent directory does not exist");
        }
    }

    public boolean getSetting(String key) {
        return switch (key) {
            case "extraItems" -> (Boolean) configObject.getOrDefault("extraItems", false);
            case "modernItems" -> (Boolean) configObject.getOrDefault("modernItems", false);
            case "numIds" -> (Boolean) configObject.getOrDefault("numIds", true);
            case "extraIds" -> (Boolean) configObject.getOrDefault("extraIds", true);
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }

    public void setSetting(String key, Boolean value) {
        try {
            this.configObject.appendField(key, value);
            Files.writeString(configFilePath, this.configObject.toString());
        } catch (IOException e) {
                NormalCreative.LOGGER.info("Failed to update config; an I/O error occurred or the parent directory does not exist");
        }
    }

    public static JSONObject generateDefaultConfig() {
        return new JSONObject()
                .appendField("extraItems", false)
                .appendField("modernItems", false)
                .appendField("numIds", true)
                .appendField("extraIds", true);
    }
}
