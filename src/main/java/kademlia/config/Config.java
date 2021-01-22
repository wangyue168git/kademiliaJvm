package kademlia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/23
 */
@Configuration
public class Config {

    @Value("${user}")
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
