package t3h.manga.mangaweb.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl {
    @Autowired
    private Cloudinary cloudinary;

    public Map uploadImageFromUrl(String imageUrl) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(imageUrl, ObjectUtils.emptyMap());
        return uploadResult;
    }
}
