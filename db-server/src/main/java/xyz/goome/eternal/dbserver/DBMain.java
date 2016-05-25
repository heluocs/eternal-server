package xyz.goome.eternal.dbserver;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by matrix on 16/4/9.
 */
public class DBMain {

    public static void main(String[] args) {
        String[] config = {"classpath:spring-context.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
    }

}
