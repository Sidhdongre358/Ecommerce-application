package com.ecommerce.project.services.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServicesImpl implements FileServices {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalImageName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String extension = originalImageName.substring(originalImageName.lastIndexOf("."));
        String fileName = randomId.concat(extension);
        String filePath = path + File.separator + fileName;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
