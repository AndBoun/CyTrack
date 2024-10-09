package Repositories;
import Entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long>{
    User findByUserName(String name);

}
