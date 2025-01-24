package telepathicgrunt.structure_layout_optimizer.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

//Source: https://github.com/Team-Resourceful/Resourceful-Config/tree/1.21.x/common/src/main/java/com/teamresourceful/resourcefulconfig/common/jsonc
public class JsoncObject implements JsoncElement {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Pattern COMMENT_PATTERN = Pattern.compile("(^\s*//.*$)|(/\\*(\\*(?!/)|[^*])*\\*/)", Pattern.MULTILINE);

    private String comment = "";
    private final Map<String, JsoncElement> elements = new LinkedHashMap<>();

    public JsoncObject add(String key, JsoncElement element) {
        elements.put(key, element);
        return this;
    }

    public JsoncObject remove(String key) {
        elements.remove(key);
        return this;
    }

    @Override
    public String toString(int indentation) {
        if (elements.isEmpty()) return "{}";
        StringBuilder builder = new StringBuilder("{\n");
        for (Map.Entry<String, JsoncElement> entry : elements.entrySet()) {
            JsoncElement.writeComment(builder, entry.getValue(), indentation + 1);
            builder.append(ConfigUtils.repeat(INDENT, indentation + 1));
            builder.append("\"").append(entry.getKey()).append("\": ");
            builder.append(entry.getValue().toString(indentation + 1));
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append(ConfigUtils.repeat(INDENT, indentation)).append("}");
        return builder.toString();
    }

    @Override
    public void comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public String toString() {
        return toString(0);
    }

    public static JsonObject parse(String json) {
        json = COMMENT_PATTERN.matcher(json).replaceAll("");
        return GSON.fromJson(json, JsonObject.class);
    }
}