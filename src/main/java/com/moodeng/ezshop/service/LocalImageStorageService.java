package com.moodeng.ezshop.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalImageStorageService implements ImageStorageService {

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return "";
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null) {
            return;
        }
    }

    @Override
    public String updateFile(String oldFileUrl, MultipartFile newFile) {
        String newFileName = newFile.getOriginalFilename();
        return "";
    }

    @Override
    public Resource loadFileAsResource(String fileUrl) {
        return null;
    }
}
