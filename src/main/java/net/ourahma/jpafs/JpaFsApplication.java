package net.ourahma.jpafs;

import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.repositories.RoleRepository;
import net.ourahma.jpafs.repositories.UserRepository;
import net.ourahma.jpafs.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class JpaFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaFsApplication.class, args);
    }
    // tester les methodes de ServiceInmpl

    // une méthode @Bean ce que il s'éxecute au démarrage de l'application
    @Bean
    CommandLineRunner start(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        // si comme on retoure un new CommandLineRunner avec la methode run
        return args ->{

            // créer un utilisateur
            User u = new User();
            u.setUsername("user1");
            u.setPassword("123456");
            userService.addNewUser(u);

            User u2 = new User();
            u2.setUsername("admin");
            u2.setPassword("123456");
            userService.addNewUser(u2);
            System.out.println("Récupération des utilisateurs après création ");
            List<User> users = userRepository.findAll();
            users.forEach(user -> {
                System.out.println("User => "+user.getUsername());
            });

            // créer des roles
            Stream.of("STUDENT", "USER", "ADMIN").forEach(r->{
                Role role1 = new Role();
                role1.setRoleName(r);
                userService.addNewRole(role1);
            });
            System.out.println("Récupération des roles après création ");
            List<Role> roles = roleRepository.findAll();
            roles.forEach(role -> {
                System.out.println("Role => "+role.getRoleName());
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
