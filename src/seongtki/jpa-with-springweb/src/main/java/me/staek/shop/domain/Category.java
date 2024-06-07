package me.staek.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import me.staek.shop.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "CATETORY_ID")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "CATETORY_ITEM",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}
