package onetoone.Persons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Kai Quach
 * 
 */ 

public interface PersonRepository extends JpaRepository<Person, Long> {
    
    Person findById(int id);

    void deleteById(int id);

    Person findByLaptop_Id(int id);
}
