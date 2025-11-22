package com.moodeng.ezshop.service;


import org.springframework.web.multipart.MultipartFile;

// 이미지 관리용 인터페이스
public interface ImageStorageService {

    String saveFile(MultipartFile file);

    void deleteFile(String fileUrl);

    String updateFile(String oldFileUrl, MultipartFile newFile);

}
