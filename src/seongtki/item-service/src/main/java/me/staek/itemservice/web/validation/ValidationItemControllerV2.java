package me.staek.itemservice.web.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.*;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
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

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository repository;
    private final ItemValidator validator;

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
        Optional<Item> finded = repository.findById(itemId);
        model.addAttribute("item", finded.get());
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String item(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItem1(Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * FieldError - 타입오류 발생시 400오류화면이 아닌 등록폼 화면을 유지하고 에러를 출력한다. (출력메세지는 스프링에서 제공)
         */
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

//    @PostMapping("/add")
    public String addItem2(Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * FieldError - 오류 발생시 사용자 입력 값을 저장하는 기능을 제공하는 생성자 사용 (rejectedValue)
         * - th:field="*{price}" 는 정상상황일 때 전달된 객체를 사용하고, 오류발생 시 FieldError의 rejectedValue를 표출함.
         *
         * - 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다. 그리고 해당
         *   오류를 BindingResult 에 담아서 컨트롤러를 호출한다.
         *
         */
        // 개별 필드 검증
        if (!StringUtils.hasText(item.getItemName()))
            br.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수입니다."));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100_000)
            br.addError(new FieldError("item","price", item.getPrice(), false, null, null, "가격은 1000~100,000 범위가 합니다."));
        if (item.getQuantity() == null || item.getQuantity() > 9999)
            br.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9,999까지 가능합니다."));

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.addError(new ObjectError("item", null, null, "가격*수량 은 10,000 이상이어야 한다. 현재 값:" + retPrice));
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

//    @PostMapping("/add")
    public String addItem3(Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * 아래는 FieldError의 생성자 인자이다.
         *  objectName : 오류가 발생한 객체 이름
         *  field : 오류 필드
         *  rejectedValue : 사용자가 입력한 값(거절된 값)
         *  bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
         *  codes : 메시지 코드
         *  arguments : 메시지에서 사용하는 인자
         *  defaultMessage : 기본 오류 메시지
         *  -> codes와 arguments를 추가해서 메세지를 errors.properties가 관리하도록 한다.
         */
        // 개별 필드 검증
        if (!StringUtils.hasText(item.getItemName()))
            br.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, "상품이름은 필수입니다."));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100_000)
            br.addError(new FieldError("item","price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000,100000}, "가격은 1000~100,000 범위가 합니다."));
        if (item.getQuantity() == null || item.getQuantity() > 9999)
            br.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, "수량은 9,999까지 가능합니다."));

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, retPrice}, "가격*수량 은 10,000 이상이어야 한다. 현재 값:" + retPrice));
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


//    @PostMapping("/add")
    public String addItem4(Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * rejectValue(), reject() 메서드
         *  field : 오류 필드명
         *  errorCode : 오류 코드(messageResolver 를 위한 코드)
         *  errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
         *  defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
         *
         * 검증순서 정리
         * 1. rejectValue() 호출
         * 2. MessageCodesResolver 를 사용해서 검증 오류 코드로 메시지 코드들을 생성
         * 3. new FieldError() 를 생성하면서 메시지 코드들을 보관
         * 4. th:erros 에서 메시지 코드들로 메시지를 순서대로 메시지에서 찾고, 표출
         */

        log.info("objectName={}", br.getObjectName()); //objectName=item
        // target=Item(id=null, itemName=awef, price=2000, quantity=22222, open=false, regions=[], itemType=null, deliveryCode=)
        log.info("target={}", br.getTarget());

        // 개별 필드 검증
        if (!StringUtils.hasText(item.getItemName()))
            br.rejectValue("itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 100_000)
            br.rejectValue("price", "range", new Object[]{1000,100000}, null);
        if (item.getQuantity() == null || item.getQuantity() > 9999)
            br.rejectValue("quantity", "max", new Object[]{9999}, null);

        // 복합 필드 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int retPrice = item.getPrice() * item.getQuantity();
            if (retPrice < 10000)
                br.reject("totalPriceMin", new Object[]{10000, retPrice}, null);
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

//    @PostMapping("/add")
    public String addItem5(Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * Valicator를 확장한 빈의 메서드에 검증로직을 담고 호출한다.
         */
        validator.validate(item, br);

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

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(validator);
    }

    @PostMapping("/add")
    public String addItem6(@Validated Item item, BindingResult br, RedirectAttributes redirectAttributes) {

        /**
         * @Validated 태그를 target object에 추가하면 WebDataBinder에 추가되어 있는 validator가 동작한다.
         * ItemValidator는 support()에 의해 타겟을 검사하므로 안전하다.
         */

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
        Optional<Item> finded = repository.findById(itemId);
        model.addAttribute("item", finded.get());
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String doEdit(@PathVariable Long itemId, ItemUpdateDto item) {
        repository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}
