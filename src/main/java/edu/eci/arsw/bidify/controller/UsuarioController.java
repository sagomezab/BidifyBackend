package edu.eci.arsw.bidify.controller;

import edu.eci.arsw.bidify.dto.Mensaje;
import edu.eci.arsw.bidify.model.Producto;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.service.UsuarioService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/lista")
    public ResponseEntity<List<Usuario>> list(){
        List<Usuario> list = usuarioService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario){
        if(StringUtils.isBlank(usuario.getUserName()) || StringUtils.isBlank(usuario.getPassword()))
            return new ResponseEntity<>(new Mensaje("El nombre de usuario y la contraseña son obligatorios"), HttpStatus.BAD_REQUEST);

        usuarioService.registrarUsuario(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario registrado"), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{userName}")
    public ResponseEntity<?> delete(@PathVariable("userName") String userName){
        if(!usuarioService.existsByUserName(userName))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);

        usuarioService.deleteUserName(userName);
        return new ResponseEntity<>(new Mensaje("Usuario eliminado"), HttpStatus.OK);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> update(@RequestBody Usuario usuario){
        if(!usuarioService.existsById(usuario.getId()))
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);

        usuarioService.updateUser(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        boolean isAuthenticated = usuarioService.login(usuario.getUserName(), usuario.getPassword());
        if(isAuthenticated){
            return new ResponseEntity<>(new Mensaje("Login exitoso"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Mensaje("Credenciales inválidas"), HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    @GetMapping("/productos/{userName}")
    public ResponseEntity<?> getProductos(@PathVariable("userName") String userName) {
        Usuario usuario = usuarioService.findByUserName(userName);
        if (usuario == null) {
            return new ResponseEntity<>(new Mensaje("El usuario no existe"), HttpStatus.NOT_FOUND);
        }
        List<Producto> productos = usuario.getProductos();
        if (productos.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("El usuario no tiene productos"), HttpStatus.NOT_FOUND);
        } else {
            List<String> nombresProductos = new ArrayList<>();
            for (Producto producto : productos) {
                nombresProductos.add(producto.getNombre());
            }
            return new ResponseEntity<>(nombresProductos, HttpStatus.OK);
        }
    }
}
