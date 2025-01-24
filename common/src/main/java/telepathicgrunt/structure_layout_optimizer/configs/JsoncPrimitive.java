package telepathicgrunt.structure_layout_optimizer.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

//Source: https://github.com/Team-Resourceful/Resourceful-Config/tree/1.21.x/common/src/main/java/com/teamresourceful/resourcefulconfig/common/jsonc
public class JsoncPrimitive implements JsoncElement {
    private String comment = "";
    private final JsonElement element;

    public JsoncPrimitive(JsonElement element) {
        this.element = element;
    }

    public JsoncPrimitive(Number element) {
        this(new JsonPrimitive(element));
    }

    public JsoncPrimitive(boolean element) {
        this(new JsonPrimitive(element));
    }

    public JsoncPrimitive(String element) {
        this(new JsonPrimitive(element));
    }

    public void comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(int indentation) {
        return element.toString();
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
