package me.staek.itemservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.web.validation.dto.ItemSaveDto;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 * HttpMessageConverter ( @RequestBody ) 테스트
 *
 *
 * POST  http://localhost:8080/validation/api/items/add
 * Body > raw > json
 *
 * 1) {"itemName":"hello", "price":1000, "quantity": 10} => 성공
 * 2) {"itemName":"hello", "price":ㅁ, "quantity": 10}
 * => 컨트롤러진입 실패 (HttpMessageConverter에서 json객체를 만들기 실패하였고, 예외가 발생하여 이후 단계실행이 안됨.)
 * 3) 1) {"itemName":"hello", "price":1000, "quantity": 10000}
 * => 검증오류 (BindingResult에 관련내용이 생기고 response에 관련 내용확인 가능)
 *
 *
 */
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
@RequiredArgsConstructor
public class ValidationItemControllerAPI {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveDto form, BindingResult br) {

        log.info("API 컨트롤러 호출");

        if (br.hasErrors()) {
            log.info("검증 오류 발생 errors={}", br);
            return br.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
