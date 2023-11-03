package edu.eci.arsw.bidify.security.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.arsw.bidify.security.entity.Rol;
import edu.eci.arsw.bidify.security.enums.RolNombre;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
}