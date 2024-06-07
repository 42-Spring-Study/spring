package me.staek.shop.domain;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/**
 * Embeddable : 값 객체 선언 애노테이션.
 * - Entity에서 사용하면 생명주기가 종속된다.
 * - setMethod 함수는 사용 안해야 하지만 예제작성이 필요해서 임시로 작성함.
 */
@Embeddable
public class Address {
    private String city;
    private String zipcode;
    private String street;

    public Address() {}

    public Address(String city, String zipcode, String street) {
        this.city = city;
        this.zipcode = zipcode;
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getStreet() {
        return street;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getZipcode(), address.getZipcode()) && Objects.equals(getStreet(), address.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getZipcode(), getStreet());
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
