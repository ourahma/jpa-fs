package net.ourahma.jpafs.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;
import net.ourahma.jpafs.repositories.RoleRepository;
import net.ourahma.jpafs.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor // a ne pas mettre le constructeur sans parametre a cause d"injectction de dependances
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    /*public UserServiceImpl(@Qualifier UserRepository userRepository,
                            @Qualifier RoleRepository roleRepository) {
        this.userRepository = userRepository;
    }
    on peut utiliser Autowired , ou le constructeur et l'annotation
    AllArgConstructor
    */

    @Override
    public User addNewUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
    User user = this.findUserByUserName(userName);
    Role role = this.findRoleByRoleName(roleName);
    if(user.getRoles()!=null) {
        user.getRoles().add(role);
        // si nous avons pas inrialiser la liste roles on aura un NullPointerException
        //userRepository.save(user); ce n'est pas necessaire parce que on a que la methode est transactionnelle
        role.getUsers().add(user);
    }
    }

    @Override
    public User authenticate(String userName, String password) {
        User user = userRepository.findByUsername(userName);
        if (user == null ) throw new RuntimeException("Bad Credentials");
        if(user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Bad Credentials");
    }
}
