package com.event_management.eventmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventManagementApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner initUsers(UserService userService) {
        return args -> {
            UserDTO userDTO = new UserDTO();
            UUID uuid = UUID.randomUUID();
            userDTO.setUserEmail("user"+uuid.toString().substring(0,8)+"@techchina.com");
            userDTO.setUserPassword("password");
            userDTO.setUserFirstname("UserDemo");
            userDTO.setUserLastname("UserDemo");
            userService.saveUser(userDTO);

            System.out.println(userDTO);
        };
    }*/
}