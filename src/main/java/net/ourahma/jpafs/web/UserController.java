package net.ourahma.jpafs.web;

import lombok.AllArgsConstructor;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Consulter un utilisateur
    @GetMapping("/users/{username}")
    public User user(@PathVariable String username){
        User u = userService.findUserByUserName(username);
        return u;
    }
}
