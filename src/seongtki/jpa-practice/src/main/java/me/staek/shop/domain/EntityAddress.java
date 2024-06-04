package me.staek.shop.domain;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

/**
 * Collection value type 을 Entity로 추출하여 작성함.
 */
@Entity
@Table(name = "ADDRESS")
public class EntityAddress {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ADRESS_ID")
    private Long id;

    public EntityAddress() {

    }

    public EntityAddress(Address address) {
        this.address = address;
    }

    private Address address;

    public EntityAddress(String city, String zipcode, String street) {
        this.address = new Address(city, zipcode, street);
    }

    @Override
    public String toString() {
        return "EntityAddress{" +
                "id=" + id +
                ", address=" + address +
                '}';
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        EntityAddress that = (EntityAddress) object;
        return Objects.equals(id, that.id) && Objects.equals(address, that.address);
    }

    /**
     * Entity작성 시 proxy 를 사용한다면 아래처럼 사용해야 한다.
     */
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        EntityAddress address = (EntityAddress) o;
//        return Objects.equals(this.id, address.id) &&
//                Objects.equals(this.address, address.address);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}
