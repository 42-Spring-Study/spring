package me.staek.itemservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository repository;

    /**
     * 컨트롤러 클래스가 호출될 때마다 자동호출된다 (리팩토링 필요)
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    /**
     * 컨트롤러 클래스가 호출될 때마다 자동호출된다 (리팩토링 필요)
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    /**
     * 컨트롤러 클래스가 호출될 때마다 자동호출된다 (리팩토링 필요)
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = repository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String item(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    /**
     * @InitBinder 를 태그한 ItemValidator 를 추가하는 메서드를 삭제한다.
     * 일전에 추가한 spring-boot-starter-validation 라이브러리가 자동으로 Bean Validator를 인지하고 스프링에 통합된다.
     * LocalValidatorFactoryBean을 글로벌 validator에 등록하여 사용자는 @Validated를 사용하기만 하면
     * 타겟 오브젝트의 @NotNull 등을 검사한다.
     *
     *
     * **검증순서
     * @ModelAttribute 각각의 필드에 타입 변환 시도
     * 1. 바인딩에 성공하면 다음로직으로이동 (실패하면 typeMismatch 로 FieldError 추가)
     * 2. BeanValidation 적용
     *
     *
     * 예시)
     * 1. itemName 에 문자 "A" 입력 타입 변환 성공 itemName 필드에 BeanValidation 적용
     * 2. price 에 문자 "A" 입력 "A"를 숫자 타입 변환 시도 실패 typeMismatch FieldError 추가 price 필드는 BeanValidation 적용 X
     *
     *
     * BeanValidation 메시지 찾는 순서
     * 1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
     * 2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
     * 3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.
     *
     * ObjectError 검증로직
     * Domain에 @ScriptAssert를사용할 수 도 있지만, 여러 오브젝트를 결합하여 검증을 요구할 수도 있기에
     * 유연한 개발을 위해 자바코드로 작성하면 좋다.
     *
     *
     */
//    @PostMapping("/add")
    public String addItem1(@Validated Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.reject("totalPriceMin", new Object[]{10000, retPrice}, null);
        }

        // 검증 실패 시 입력폼으로 이동
        if (br.hasErrors()) {
            log.info("errors={}", br);
            return "validation/v3/addForm";
        }

        Item saved = repository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    /**
     * groups 추가
     * 같은 도메인(Item.class)의 필드를 추가,수정할때 검증내용이 다를 수 있다.
     * 이를 해결하기 위해 group에 해당하는 인터페이스를작성하고, Item에 정의하고, 컨트롤러에 사용할 그룹을 작성한다.
     *
     * 단점
     * 복잡하다.
     * @Valid는 groups를 적용할 수 있는 기능이 없다.
     */
    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class)  Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.reject("totalPriceMin", new Object[]{10000, retPrice}, null);
        }

        // 검증 실패 시 입력폼으로 이동
        if (br.hasErrors()) {
            log.info("errors={}", br);
            return "validation/v3/addForm";
        }

        Item saved = repository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "validation/v3/editForm";
    }

//    @PostMapping("/{itemId}/edit")
    public String doEdit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult br) {

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.reject("totalPriceMin", new Object[]{10000, retPrice}, null);
        }

        // 검증 실패 시 입력폼으로 이동
        if (br.hasErrors()) {
            log.info("errors={}", br);
            return "validation/v3/editForm";
        }


        repository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    @PostMapping("/{itemId}/edit")
    public String doEdit2(@PathVariable Long itemId, @Validated(UpdateCheck.class)  @ModelAttribute Item item, BindingResult br) {

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.reject("totalPriceMin", new Object[]{10000, retPrice}, null);
        }

        // 검증 실패 시 입력폼으로 이동
        if (br.hasErrors()) {
            log.info("errors={}", br);
            return "validation/v3/editForm";
        }


        repository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}
