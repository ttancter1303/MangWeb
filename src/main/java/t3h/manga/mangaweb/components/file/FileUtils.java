package t3h.manga.mangaweb.components.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    public static boolean isImageFile(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }

        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return false; // Không có phần mở rộng
        }

        String fileExtension = fileName.substring(dotIndex + 1).toLowerCase();
        return IMAGE_EXTENSIONS.contains(fileExtension);
    }
}
