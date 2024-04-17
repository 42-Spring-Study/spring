package me.staek.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * rejectValue() , reject() 는 내부에서 MessageCodesResolver 를 사용한다.=> 메시지 코드들 생성.
 * FieldError , ObjectError 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
 * => MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관한다.
 * BindingResult로그 : codes [range.item.price, range.price, range.java.lang.Integer, range]
 */
public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    /**
     * 1.: code + "." + object name
     * 2.: code
     */
    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    /**
     * 1.: code + "." + object name + "." + field
     * 2.: code + "." + field
     * 3.: code + "." + field type
     * 4.: code
     */
    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
