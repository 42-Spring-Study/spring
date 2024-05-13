package me.staek.itemservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.*;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

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
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Optional<Item> finded = repository.findById(itemId);
        model.addAttribute("item", finded.get());
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String item(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String save5(Item item, RedirectAttributes redirectAttributes, Model model) {

        /**
         * 검증로직 작성에 대한 문제점
         *
         * 1. 뷰 템플릿에서 중복 처리가 많다. (addForm.html)
         *
         * 2. 타입 오류 처리가 안된다. Item 의 price , quantity 같은 숫자 필드는 타입이 Integer 이므로 문자 타입
         * 으로 설정하는 것이 불가능하다. 숫자 타입에 문자가 들어오면 오류가 발생한다.
         * 이러한 오류는 컨트롤러 진입 전에 발생되어 400예외페이지가 띄워진다.
         *
         * 3. 2번에서 컨트롤러가 호출된다고 하더라도 다른타입을 저장할 공간이 없다. 사용자는 어떤값을 입력했었는지 알고 싶을 것이다.
         *
         */
        Map<String, String> errors = new HashMap<>();

        // 개별 필드 검증
        if (!StringUtils.hasText(item.getItemName()))
            errors.put("itemName", "상품이름은 필수입니다.");
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100_000)
            errors.put("price", "가격은 1000~100,000 범위가 합니다.");
        if (item.getQuantity() == null || item.getQuantity() > 9999)
            errors.put("quantity", "수량은 9,999까지 가능합니다.");

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                errors.put("globalError", "가격*수량 은 10,000 이상이어야 한다. 현재 값:" + retPrice);
        }

        // 검증 실패 시 입력폼으로 이동
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        Item saved = repository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model) {
        Optional<Item> finded = repository.findById(itemId);
        model.addAttribute("item", finded.get());
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String doEdit(@PathVariable Long itemId, ItemUpdateDto item) {
        repository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }
}
