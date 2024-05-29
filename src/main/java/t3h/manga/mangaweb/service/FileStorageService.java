package t3h.manga.mangaweb.service;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads";

    public String store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uploadDir = "uploads"; // Thư mục lưu trữ ảnh

        // Tạo thư mục uploads nếu nó chưa tồn tại
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        // Lưu file vào thư mục uploads
        Path uploadPathWithFileName = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), uploadPathWithFileName, StandardCopyOption.REPLACE_EXISTING);

        // Thay thế dấu gạch ngược "\" bằng "/"
        String uploadPathString = uploadPathWithFileName.toString().replace("\\", "/");

        // Trả về đường dẫn tới file vừa lưu
        return uploadPathString;
    }
}
