package kademlia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.UnknownHostException;



/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/22
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,
        scanBasePackages="kademlia")
@EnableConfigurationProperties

public class App {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(App.class, args);
    }

}
