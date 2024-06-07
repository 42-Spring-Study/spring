package me.staek;

import me.staek.shop.domain.Address;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class _05_Embedded_Equal {

    @Test
    void test() {
        Address address1 = new Address("city", "zipcode", "street");
        Address address2 = new Address("city", "zipcode", "street");

        Assertions.assertThat(address1).isEqualTo(address2);
    }

}
