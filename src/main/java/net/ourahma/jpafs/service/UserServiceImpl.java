package net.ourahma.jpafs.service;

import jakarta.transaction.Transactional;
import net.ourahma.jpafs.entities.Role;
import net.ourahma.jpafs.entities.User;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Override
    public User addNewUser(User user) {
        return null;
    }

    @Override
    public Role addNewRole(Role role) {
        return null;
    }

    @Override
    public User findUserByUserName(String userName) {
        return null;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return null;
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {

    }
}
