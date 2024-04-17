package me.staek;

import me.staek.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class ItemServiceApplication {

    /**
     * 메세지소스 빈 등록 코드.
     * springboot에서는 application.properties에 spring.messages.basename=messages 와 같이 기입하면 작동한다.
     * @return
     */
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasenames("messages", "errors");
//        messageSource.setDefaultEncoding("utf-8");
//        return messageSource;
//    }

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }

}

/**
 * WebMvcConfigurer을 확장하여 getValidator()를 작성하면, 컨트롤러에 @InitBinder태그에 대한 validate 추가 로직이 없어도 모든 컨트롤러에 적용 가능하다.
 * 이 로직을 작성할 경우 Bean Validator기능을 사용할 수 없다.
 */
//@SpringBootApplication
//public class ItemServiceApplication implements WebMvcConfigurer {
//    public static void main(String[] args) {
//        SpringApplication.run(ItemServiceApplication.class, args);
//    }
//    @Override
//    public Validator getValidator() {
//        return new ItemValidator();
//    }
//}
