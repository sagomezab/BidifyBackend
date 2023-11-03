package edu.eci.arsw.bidify.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import edu.eci.arsw.bidify.model.Producto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.security.entity.Rol;
import edu.eci.arsw.bidify.security.entity.Usuario;
import edu.eci.arsw.bidify.security.enums.RolNombre;
import edu.eci.arsw.bidify.security.service.RolService;
import edu.eci.arsw.bidify.service.ProductoService;
import edu.eci.arsw.bidify.service.SubastaService;
import edu.eci.arsw.bidify.service.UsuarioService;

@Component
public class createSubastas implements CommandLineRunner{
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RolService rolService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ProductoService productoService;
    @Autowired
    SubastaService subastaService;

    
    Set<Usuario> oferentes = new HashSet<>();
    @Override
    public void run(String... args) throws Exception {
        //Roles
        Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
        Rol rolUser = new Rol(RolNombre.ROLE_USER);
        rolService.save(rolAdmin);
        rolService.save(rolUser);
        //usuario 1
        Usuario jaider = new Usuario();
        jaider.setNombre("jaider");
        jaider.setNombreUsuario("rediaj");
        jaider.setEmail("jaider@gmail.com");
        jaider.setPassword(passwordEncoder.encode("papas"));
        //usuario 2
        Usuario miguel = new Usuario();
        miguel.setNombre("miguel");
        miguel.setNombreUsuario("miguel");
        miguel.setEmail("miguel@gmail.com");
        miguel.setPassword(passwordEncoder.encode("helado"));
        //asignación de roles
        Set<Rol> roles = new HashSet<>();
        roles.add(rolUser);
        jaider.setRoles(roles);
        miguel.setRoles(roles);
        usuarioService.save(jaider);
        usuarioService.save(miguel);
        //productos
        Producto producto1 = new Producto("Jordan One", (float) 600000, "https://phantom-expansion.unidadeditorial.es/6239da431613d30a7ade440a4719e3db/crop/0x378/1074x982/resize/828/f/jpg/assets/multimedia/imagenes/2022/03/21/16478732471407.jpg");
        Producto producto2 = new Producto("Camara", (float) 800000, "https://www.workshopexperience.com/wp-content/uploads/2017/07/marcas-de-camaras-fotograficas-4.jpg");
        Producto producto3 = new Producto("Porsche 911 GT3", (float) 351500000, "https://files.porsche.com/filestore/image/multimedia/none/992-gt3-modelimage-sideshot/model/765dfc51-51bc-11eb-80d1-005056bbdc38/porsche-model.png");
        Producto producto4 = new Producto("Vinilo Michael Jackson", (float) 2000000000, "https://http2.mlstatic.com/D_NQ_NP_664991-MCO71457373138_092023-O.webp");
        Producto producto5 = new Producto("Funko pop Michael Jackson", (float) 600000, "https://http2.mlstatic.com/D_NQ_NP_631613-MCO71749069309_092023-O.webp");
        productoService.save(producto1);   
        productoService.save(producto2);  
        productoService.save(producto3);  
        productoService.save(producto4);  
        productoService.save(producto5);
        BigDecimal bigDecimalValue = new BigDecimal(Float.toString(producto1.getPrecio()));
        BigDecimal bigDecimalValue2 = new BigDecimal(Float.toString(producto2.getPrecio()));
        //subastas
        
        
        Subasta subasta1 = new Subasta(jaider, producto1, bigDecimalValue, true, 3);
        oferentes.add(miguel);
        subasta1.setOferentes(oferentes);
        subastaService.addSubasta(subasta1);
        /*
        Subasta subasta1 = new Subasta(jaider, producto1, bigDecimalValue, false, 3, null);
        oferentes.add(miguel);
        subasta1.setOferentes(oferentes);
        subasta1.setPrecioFinal(bigDecimalValue);
        subasta1.setGanador(miguel);
        // Guardar la Subasta en la base de datos
        subastaService.addSubasta(subasta1);

        // Luego, crea y asocia los mensajes y actualiza la Subasta
        Message mensaje1 = new Message();
        Message mensaje2 = new Message();
        messageList.add(mensaje1);
        messageList.add(mensaje2);
        subasta1.setMessageList(messageList);
        subastaService.addSubasta(subasta1);

         
        Subasta subasta2 = new Subasta(miguel, producto2, bigDecimalValue2, false, 3, messageList);
        
        subastaService.addSubasta(subasta2);
        
        * 
         */
    }
}
