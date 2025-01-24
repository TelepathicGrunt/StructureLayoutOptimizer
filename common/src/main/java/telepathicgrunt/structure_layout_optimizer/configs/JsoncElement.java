package telepathicgrunt.structure_layout_optimizer.configs;

import java.util.Arrays;

//Source: https://github.com/Team-Resourceful/Resourceful-Config/tree/1.21.x/common/src/main/java/com/teamresourceful/resourcefulconfig/common/jsonc
public interface JsoncElement {

    int INDENT_SIZE = 4;
    String INDENT = ConfigUtils.repeat(" ", INDENT_SIZE);

    static void writeComment(StringBuilder builder, JsoncElement element, int indentation) {
        if (element.comments().length > 0 && !element.comment().trim().isEmpty()) {
            if (element.comments().length > 1) {
                builder.append(ConfigUtils.repeat(INDENT, indentation)).append("/*\n");
                for (String line : element.comments()) {
                    builder.append(ConfigUtils.repeat(INDENT, indentation)).append(" * ").append(line).append("\n");
                }
                builder.append(ConfigUtils.repeat(INDENT, indentation)).append(" */\n");
            } else {
                builder.append(ConfigUtils.repeat(INDENT, indentation)).append("// ").append(element.comment()).append("\n");
            }
        }
    }

    String toString(int indentation);

    void comment(String comment);

    String comment();

    default String[] comments() {
        return comment().split("\\R");
    }
}
