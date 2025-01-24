package telepathicgrunt.structure_layout_optimizer.configs;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;

public class ConfigUtils {

    static File getConfigFile(Path configDir, String fileName) {
        File jsonFile = configDir.resolve(fileName + ".json").toFile();
        if (jsonFile.exists()) {
            return jsonFile;
        }
        return configDir.resolve(fileName + ".jsonc").toFile();
    }

    static String repeat(String input, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        if (count == 1) {
            return input;
        }
        final int len = input.length();
        if (len == 0 || count == 0) {
            return "";
        }
        if (Integer.MAX_VALUE / count < len) {
            throw new OutOfMemoryError("Required length exceeds implementation limit");
        }
        if (len == 1) {
            final byte[] single = new byte[count];
            Arrays.fill(single, input.getBytes()[0]);
            return new String(single);
        }
        final int limit = len * count;
        final byte[] multiple = new byte[limit];
        System.arraycopy(input.getBytes(), 0, multiple, 0, len);
        int copied = len;
        for (; copied < limit - copied; copied <<= 1) {
            System.arraycopy(multiple, 0, multiple, copied, copied);
        }
        System.arraycopy(multiple, 0, multiple, copied, limit - copied);
        return new String(multiple);
    }
}
