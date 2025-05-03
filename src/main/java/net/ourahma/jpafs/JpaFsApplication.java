package net.ourahma.jpafs;

import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class JpaFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaFsApplication.class, args);
    }
    // tester les methodes de ServiceInmpl

    // une méthode @Bean ce que il s'éxecute au démarrage de l'application
    @Bean
    CommandLineRunner start(UserService userService) {
        // si comme on retoure un new CommandLineRunner avec la methode run
        return args ->{
            User u = new User();
            u.setUsername("user1");
            u.setPassword("123456");
            userService.addNewUser(u);

            User u2 = new User();
            u2.setUsername("admin");
            u2.setPassword("123456");
            userService.addNewUser(u2);

            Stream.of("STUDENT", "USER", "ADMIN").forEach(r->{
                Role role1 = new Role();
                role1.setRoleName(r);
                role1.setDesc("desc");
                userService.addNewRole(role1);
            });

            // affecter les roles au users
            userService.addRoleToUser("user1","STUDENT");
            userService.addRoleToUser("user1","USER");
            userService.addRoleToUser("admin","USER");
            userService.addRoleToUser("admin","ADMIN");
            // consulter une utilisateur

            try {
                User user = userService.authenticate("user1","123456");
                System.out.println(user.getUserId());
                System.out.println(user.getUsername());
                System.out.println("Roles => ");
                user.getRoles().forEach(role ->{
                    System.out.println("Role =>"+role);
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
