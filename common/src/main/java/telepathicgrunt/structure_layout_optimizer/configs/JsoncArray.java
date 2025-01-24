package telepathicgrunt.structure_layout_optimizer.configs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Source: https://github.com/Team-Resourceful/Resourceful-Config/tree/1.21.x/common/src/main/java/com/teamresourceful/resourcefulconfig/common/jsonc
public class JsoncArray implements JsoncElement, Iterable<JsoncElement> {

    private final List<JsoncElement> elements = new ArrayList<>();
    private String comment = "";

    @Override
    public void comment(String comment) {
        this.comment = comment;
    }

    @Override
    public String comment() {
        return comment;
    }

    public JsoncArray add(JsoncElement element) {
        elements.add(element);
        return this;
    }

    public JsoncArray remove(JsoncElement element) {
        elements.remove(element);
        return this;
    }

    public JsoncArray remove(int index) {
        elements.remove(index);
        return this;
    }

    @Override
    public String toString(int indentation) {
        if (elements.isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[\n");
        for (JsoncElement element : elements) {
            JsoncElement.writeComment(builder, element, indentation + 1);
            builder.append(ConfigUtils.repeat(INDENT, indentation + 1));
            builder.append(element.toString(indentation + 1));
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append(ConfigUtils.repeat(INDENT, indentation)).append("]");
        return builder.toString();
    }

    @Override
    public Iterator<JsoncElement> iterator() {
        return elements.iterator();
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
