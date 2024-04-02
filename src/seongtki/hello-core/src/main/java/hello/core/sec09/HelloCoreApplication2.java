package hello.core.sec09;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//@SpringBootApplication
public class HelloCoreApplication2 {
    public static void main(String[] args) {
//        SpringApplication.run(HelloCoreApplication2.class, args);

//
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(beanDefinitionNames.length);
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = context.getBeanDefinition(beanDefinitionName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println(beanDefinitionName + " " + beanDefinition.getBeanClassName());
//                Object bean = context.getBean(beanDefinitionName);
//                System.out.println("name=" + beanDefinitionName + " object=" + bean);
            }
        }
    }
}
