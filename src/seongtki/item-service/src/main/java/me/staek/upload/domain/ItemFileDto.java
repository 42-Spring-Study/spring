package me.staek.upload.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@Data
public class ItemFileDto {
    private Long itemId;
    private String itemName;
    /**
     * 이미지를 다중 업로드 하기 위해 MultipartFile 를 사용했다
     */
    private List<MultipartFile> imageFiles;
    private MultipartFile attachFile;
}
