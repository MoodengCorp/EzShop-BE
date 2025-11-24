package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.exception.BusinessLogicException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 파일 업로드
    @Override
    public String saveFile(MultipartFile file) {
        // 파일이 없으면 null 반환
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // 파일명 중복 방지 (파일명 앞에 UUID추가)
            String originalFileName = file.getOriginalFilename();
            String storeFileName = UUID.randomUUID() + "_" + originalFileName;

            // S3에 업로드
            s3Template.upload(bucketName, storeFileName, file.getInputStream());

            // 이미지 URL 반환 (DB 저장용)
            return String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucketName, storeFileName);

        } catch (IOException e) {
            throw new BusinessLogicException(ResponseCode.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");
        }
    }

    // 이미지 파일 삭제
    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return;

        try {
            // URL에서 파일명(Key)만 추출 (ex. https://.../uuid_image.jpg -> uuid_image.jpg)
            String splitStr = ".com/"; // '.com/'이후로 이미지 이름
            int index = fileUrl.lastIndexOf(splitStr);
            if (index != -1) {
                String fileName = fileUrl.substring(index + splitStr.length());
                // S3에서 삭제
                s3Template.deleteObject(bucketName, fileName);
            }
        } catch (Exception e) {
            throw new BusinessLogicException(ResponseCode.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다.");
        }
    }

    // 이미지 파일 교체
    @Override
    public String updateFile(String oldFileUrl, MultipartFile newFile) {
        // 기존 파일 삭제 후 새 파일 업로드함
        deleteFile(oldFileUrl);
        return saveFile(newFile);
    }
}