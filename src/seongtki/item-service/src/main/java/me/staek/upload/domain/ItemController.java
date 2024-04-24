package me.staek.upload.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.upload.file.FileStore;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemFileRepository itemRepository;
    private final FileStore fileStore;

    /**
     * 등록 폼 조회
     */
    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemFileDto form) {
        return "item-form";
    }

    /**
     * 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트 한다.
     */
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemFileDto form, RedirectAttributes
            redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // 저장
        FileItem item = new FileItem();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    /**
     * 상품 조회
     */
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        FileItem item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    /**
     * <img> 태그로 이미지를 조회할 때 사용한다.
     * UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로 이미지 바이너리를 반환한다.
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws
            MalformedURLException {
        log.error("file:" + fileStore.getFullPath(filename));
        log.error("file:" + filename);
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    /**
     * 파일을 다운로드 할 때 실행한다.
     * 파일 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다.
     * 이때는 Content-Disposition 해더에 attachment; filename="업로드 파일명" 값을 주면 된다.
     */
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
            throws MalformedURLException {
        FileItem item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
