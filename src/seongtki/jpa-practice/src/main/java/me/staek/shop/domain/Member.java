package me.staek.shop.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Member extends BaseEntity {


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Address address;


    /**
     * ElementCollection : 지연로딩
     *
     */
    @ElementCollection
    @CollectionTable(name = "FAVOLITE_FOODS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name="FOOD_NAME")
    private Set<String> favoliteFoods = new HashSet<>();

    /**
     * Collection Value Type
     * - 식별자가 필요하거나 변경이 필요하여 Entity 생명주기에 종속적이지 않게 되는경우 사용하지 않는 게 좋다.
     */
//    @ElementCollection
//    @CollectionTable(name = "ADDRESS")
//    private List<Address> addressHistory = new ArrayList<>();
//    public List<Address> getAddressHistory() {
//        return addressHistory;
//    }

    /**
     * Collection Value Type 를 Entity로 추출하여 아래처럼 작성할 수 있다.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<EntityAddress> addressHistory = new ArrayList<>();

    public List<EntityAddress> getAddressHistory() {
        return addressHistory;
    }

    public Set<String> getFavoliteFoods() {
        return favoliteFoods;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Member member = (Member) object;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getName(), member.getName()) && Objects.equals(getAddress(), member.getAddress()) && Objects.equals(getFavoliteFoods(), member.getFavoliteFoods()) && Objects.equals(getAddressHistory(), member.getAddressHistory()) && Objects.equals(workAddress, member.workAddress) && Objects.equals(getPeriod(), member.getPeriod()) && Objects.equals(getTeam(), member.getTeam()) && Objects.equals(orders, member.orders) && Objects.equals(getLocker(), member.getLocker()) && Objects.equals(getProductMembers(), member.getProductMembers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress(), getFavoliteFoods(), getAddressHistory(), workAddress, getPeriod(), getTeam(), orders, getLocker(), getProductMembers());
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", favoliteFoods=" + favoliteFoods +
                ", addressHistory=" + addressHistory +
                ", workAddress=" + workAddress +
                ", period=" + period +
                ", team=" + team +
                ", orders=" + orders +
                ", locker=" + locker +
                ", productMembers=" + productMembers +
                '}';
    }

    /**
     * 값 객체의 변수 이름을 실제 테이블 컬럼으로 변경세팅할 수 있다.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET"))
    })
    private Address workAddress;

    /**
     * Embedded Type 객체변수 정의
     */
    @Embedded
    private Period period;


    /**
     * M쪽 연관관계주인 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


    /**
     * M쪽 연관관계 매핑
     * - insert, update 를 모두 False 해주어야 갱신,추가 권한이 없어져 문제가 없다.
     */
//    @ManyToOne
//    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
//    private Team team;

    /**
     * 1쪽 연관관계 매핑
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();


    /**
     * 주테이블 Member (1) : 타겟테이블 Locker (1)
     * - 주 테이블에 연관관계 매핑
     * - Member 테이블에 외래키 생성.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;


    /**
     * N:M 설정
     * 양방향 참조객체 설정
     */
//    @ManyToMany(mappedBy = "members")
//    private List<Product> products = new ArrayList<>();

//    public List<Product> getProducts() {
//        return products;
//    }

    /**
     * 1쪽 참조객체
     * - N:M 매핑테이블의 member를 참조
     */
    @OneToMany(mappedBy = "member")
    private List<ProductMember> productMembers = new ArrayList<>();

    public List<ProductMember> getProductMembers() {
        return productMembers;
    }

    public Locker getLocker() {
        return locker;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
