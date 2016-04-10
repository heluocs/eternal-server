package xyz.goome.eternal.loginserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xyz.goome.eternal.loginserver.core.LoginServer;

/**
 * Created by matrix on 16/3/29.
 */
public class LoginMain {

    private static final Logger logger = LoggerFactory.getLogger(LoginMain.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-*.xml");
        new LoginServer(8080).run();
    }

}
