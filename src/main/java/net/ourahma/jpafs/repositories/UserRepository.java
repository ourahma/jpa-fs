package net.ourahma.jpafs.repositories;

import net.ourahma.jpafs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
