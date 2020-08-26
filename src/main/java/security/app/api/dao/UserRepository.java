package security.app.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import security.app.api.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
