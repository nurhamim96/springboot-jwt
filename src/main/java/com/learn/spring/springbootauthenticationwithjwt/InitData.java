package com.learn.spring.springbootauthenticationwithjwt;

import com.learn.spring.springbootauthenticationwithjwt.dto.request.UserRequest;
import com.learn.spring.springbootauthenticationwithjwt.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InitData implements CommandLineRunner {
    private final UserService userService;

    public InitData(UserService userService) { this.userService = userService; }

    @Override
    public void run(String... args) {
        log.info("inject user default");
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName("User Default");
        userRequest.setUsername("user");
        userRequest.setPassword("password");
        userService.register(userRequest);
    }
}
