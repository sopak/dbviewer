package cz.jcode.dbviewer.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DbViewerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbViewerServerApplication.class, args);
    }

}
