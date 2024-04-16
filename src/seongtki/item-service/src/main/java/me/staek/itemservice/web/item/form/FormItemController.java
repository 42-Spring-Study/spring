package me.staek.itemservice.web.item.form;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

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
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "form/item";
    }

    /**
     * th:object 를 적용하기 위해 먼저 해당 오브젝트 정보를 넘겨준다.
     * 등록 폼이기 때문에 데이터가 비어있는 빈 오브젝트를 만들어서 뷰에 전달한다.
     */
    @GetMapping("/add")
    public String item(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    /**
     * 체크박스의 open는 언체크 시 requestBody 에 없다. (브라우저에서 안보냄)
     * 언체크 시 아래 로그에서는 확인가능함.(false)
     * 체크하면 requestBody에 있다.
     *
     * 스프링부트 3.0이전버전은 _open 태그가 있어야 언체크 시 null 이 출력되는데
     * 최신버전은 false로 잘 출력되마
     *
     */
    @PostMapping("/add")
    public String save5(Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.isOpen());
        Item saved = repository.save(item);
        redirectAttributes.addAttribute("itemId", saved.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, Model model) {
        Item finded = repository.findByItem(itemId);
        model.addAttribute("item", finded);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String doEdit(@PathVariable Long itemId, Item item) {
        repository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }
}
