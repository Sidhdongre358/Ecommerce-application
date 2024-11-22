package com.ecommerce.project.services.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileServices {
    String uploadImage(String path, MultipartFile file) throws IOException;
}
