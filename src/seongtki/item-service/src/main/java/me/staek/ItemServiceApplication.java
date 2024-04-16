package me.staek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class ItemServiceApplication {

    /**
     * 메세지소스 빈 등록 코드.
     * springboot에서는 application.properties에 spring.messages.basename=messages 와 같이 기입하면 작동한다.
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "errors");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }

}
