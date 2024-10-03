package Repositiories;

import Entity.Meal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Meal findById(int id);

    @Transactional
    void deleteById(int id);
}
