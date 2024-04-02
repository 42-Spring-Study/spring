package hello.core;

import hello.core.sec06.AutoAppConfig;
import hello.core.sec09.HelloCoreApplication2;
import hello.core.step3.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

@SpringBootApplication
public class HelloCoreApplication {

    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//             GenericXmlApplicationContext context = new GenericXmlApplicationContext("appConfig.xml");




        SpringApplication.run(HelloCoreApplication.class, args);
    }

}
