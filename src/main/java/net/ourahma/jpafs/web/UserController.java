package net.ourahma.jpafs.web;

import lombok.AllArgsConstructor;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
public class UserController {
    private UserService userService;

    // Consulter un utilisateur
    @GetMapping("/users")
    public User user(){

    }
}
