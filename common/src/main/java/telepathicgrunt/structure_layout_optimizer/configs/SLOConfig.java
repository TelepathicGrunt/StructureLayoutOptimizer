package telepathicgrunt.structure_layout_optimizer.configs;

import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import telepathicgrunt.structure_layout_optimizer.StructureLayoutOptimizerMod;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class SLOConfig {
    public static boolean deduplicateShuffledTemplatePoolElementList = false;

    public static void configStartup(Path configFolderLocation) {
        File configFile = ConfigUtils.getConfigFile(configFolderLocation, StructureLayoutOptimizerMod.MODID);
        if (configFile.exists()) {
            loadFile(configFile);

            if (configFile.getName().endsWith(".json")) {
                if (!configFile.delete()) {
                    StructureLayoutOptimizerMod.LOGGER.warn("Failed to delete old config file " + StructureLayoutOptimizerMod.MODID + ".json");
                }
                else {
                    saveFile(configFile);
                }
            }
        }
        else {
            saveFile(configFile);
        }
    }

    private static void loadFile(File configFile) {
        try {
            String data = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            JsonObject json = JsoncObject.parse(data);
            loadConfig(json);
        }
        catch (Exception e) {
            StructureLayoutOptimizerMod.LOGGER.error("Failed to read config file " + StructureLayoutOptimizerMod.MODID + ".json | {}", e.getLocalizedMessage());
        }
    }

    private static void saveFile(File configFile) {
        JsoncObject json = save();
        try {
            FileUtils.write(configFile, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            StructureLayoutOptimizerMod.LOGGER.error("Failed to write config file " + StructureLayoutOptimizerMod.MODID + ".json | {}", e.getLocalizedMessage());
        }
    }

    public static JsoncObject save() {
        JsoncObject object = new JsoncObject();
        JsoncPrimitive config1 = new JsoncPrimitive(deduplicateShuffledTemplatePoolElementList);
        config1.comment(
                "Whether to use an alternative strategy to make structure layouts generate slightly even faster than the default optimization this mod has for template pool weights." +
                " This alternative strategy works by changing the list of pieces that structures collect from the template pool to not have duplicate entries." +
                "\n" +
                "This will not break the structure generation, but it will make the structure layout different than if this config was off (breaking vanilla seed parity)." +
                " The cost of speed may be worth it in large modpacks where many structure mods are using very high weight values in their template pools." +
                "\n" +
                "\n" +
                "Pros: Get a bit more performance from high weight Template Pool Structures." +
                "\n" +
                "Cons: Loses parity with vanilla seeds on the layout of the structure. (Structure layout is not broken, just different)");
        object.add("deduplicateShuffledTemplatePoolElementList", config1);
        return object;
    }

    public static void loadConfig(JsonObject json) {
        deduplicateShuffledTemplatePoolElementList = json.getAsJsonPrimitive("deduplicateShuffledTemplatePoolElementList").getAsBoolean();
    }
}
