package me.staek.itemservice.web.item.basic;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository repository;

    /**
     * 테스트데이터
     */
    @PostConstruct
    public void init() {
        repository.save(new Item("testA", 10000, 10));
        repository.save(new Item("testB", 20000, 20));
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = repository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "basic/item";
    }

    @GetMapping("/add")
    public String item() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String save(@RequestParam String itemName
            , @RequestParam Integer price
            , @RequestParam Integer quantity
            , Model model) {
        model.addAttribute("item", repository.save(new Item(itemName, price, quantity)));
        return "basic/item";
    }

    /**
     * 인자로 ModelAttribute를 받으면, Model에 Item객체를 추가하지 않아도 유지된다.
     */
//    @PostMapping("/add")
    public String save2(@ModelAttribute("item") Item item) {
        repository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute를 생략해도 사용자 객체라면 자동 적용된다.
     * (alias는 정할 수 없다 - 인자가그대로 키로 적용됨 (맨 앞글자만 소문자로 변경)
     */
    @PostMapping("/add")
    public String save3(Item item) {
        repository.save(item);
        return "basic/item";
    }
}
