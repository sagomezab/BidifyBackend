package edu.eci.arsw.bidify.security.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.eci.arsw.bidify.security.entity.Rol;
import edu.eci.arsw.bidify.security.enums.RolNombre;
import edu.eci.arsw.bidify.security.repository.RolRepository;
import java.util.Optional;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
}