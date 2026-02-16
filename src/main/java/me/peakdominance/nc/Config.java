package me.peakdominance.nc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static final Path configFilePath = FabricLoader.getInstance().getGameDir().resolve(".ncreative");
    public JsonObject configObject = new JsonObject();

    public Config() {
        try {
            if (!Files.exists(configFilePath)) {
                this.configObject = generateDefaultConfig();
                Files.writeString(configFilePath, generateDefaultConfig().toString());
                NormalCreative.LOGGER.info("Created config file: {}", configFilePath);
            } else {
                String configContent = Files.readString(configFilePath);
                this.configObject = JsonParser.parseString(configContent).getAsJsonObject();
            }
        } catch (IOException e) {
            NormalCreative.LOGGER.info("Failed to create config file; an I/O error occurred or the parent directory does not exist");
        }
    }

    public boolean getSetting(String key) {
        return switch (key) {
            case "extraItems" -> configObject.get("extraItems").getAsBoolean();
            case "modernItems" -> configObject.get("modernItems").getAsBoolean();
            case "numIds" -> configObject.get("numIds").getAsBoolean();
            case "extraIds" -> configObject.get("extraIds").getAsBoolean();
            default -> throw new IllegalStateException("Unexpected value: " + key);
        };
    }

    public void setSetting(String key, Boolean value) {
        try {
            this.configObject.addProperty(key, value);
            Files.writeString(configFilePath, this.configObject.toString());
        } catch (IOException e) {
                NormalCreative.LOGGER.info("Failed to update config; an I/O error occurred or the parent directory does not exist");
        }
    }

    public static JsonObject generateDefaultConfig() {
        JsonObject newObject = new JsonObject();
        newObject.addProperty("extraItems", false);
        newObject.addProperty("modernItems", false);
        newObject.addProperty("numIds", true);
        newObject.addProperty("extraIds", true);
        return newObject;
    }
}
