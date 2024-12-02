package com.example.model.Repository;

<<<<<<< Updated upstream
import com.example.model.User;
=======
import com.example.model.model.User;
>>>>>>> Stashed changes
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
