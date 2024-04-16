package me.staek.itemservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.DeliveryCode;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

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
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String item(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @PostMapping("/add")
    public String save5(Item item, BindingResult br, RedirectAttributes redirectAttributes, Model model) {

        /**
         *
         */
        Map<String, String> errors = new HashMap<>();

        // 개별 필드 검증
        if (!StringUtils.hasText(item.getItemName()))
            br.addError(new FieldError("item", "itemName", "상품이름은 필수입니다."));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100_000)
            br.addError(new FieldError("item","price","가격은 1000~100,000 범위가 합니다."));
        if (item.getQuantity() == null || item.getQuantity() > 9999)
            br.addError(new FieldError("item", "quantity", "수량은 9,999까지 가능합니다."));

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.addError(new ObjectError("item", "가격*수량 은 10,000 이상이어야 한다. 현재 값:" + retPrice));
        }

        // 검증 실패 시 입력폼으로 이동
        if (br.hasErrors()) {
            log.info("errors={}", br);
            return "validation/v2/addForm";
        }

        Item saved = repository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String doEdit(@PathVariable Long itemId, Item item) {
        repository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}
