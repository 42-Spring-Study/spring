package me.staek.shop.domain;

public class MemberDto {
    private Long id;
    private String name;

    public MemberDto(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
