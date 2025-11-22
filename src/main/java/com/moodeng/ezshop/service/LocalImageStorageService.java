package com.moodeng.ezshop.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalImageStorageService implements ImageStorageService {

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return "test_url_save";
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null) {
        }
    }

    @Override
    public String updateFile(String oldFileUrl, MultipartFile newFile) {
        String newFileName = newFile.getOriginalFilename();
        return "test_url_update";
    }

}
