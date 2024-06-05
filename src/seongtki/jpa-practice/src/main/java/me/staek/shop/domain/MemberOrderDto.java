package me.staek.shop.domain;

public class MemberOrderDto {
    private Long memberId;
    private String teamName;
    private String memberName;
    private OrderStatus orderStatus;

    public MemberOrderDto(Long memberId, String teamName, String memberName, OrderStatus orderStatus) {
        this.memberId = memberId;
        this.teamName = teamName;
        this.memberName = memberName;
        this.orderStatus = orderStatus;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getMemberName() {
        return memberName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
