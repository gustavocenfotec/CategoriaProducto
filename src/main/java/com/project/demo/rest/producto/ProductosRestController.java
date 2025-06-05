package com.project.demo.rest.producto;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;

import com.project.demo.logic.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductosRestController {

    @Autowired
    private ProductoRepository productoRepository;


    @GetMapping
    //@PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Producto> ordersPage = productoRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Productos Recolectados de forma Correcta",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }


    @GetMapping("/{productoId}")
   //@PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public ResponseEntity<?> getOne(@PathVariable Long productoId, HttpServletRequest request) {
        Optional<Producto> foundProducto = productoRepository.findById(productoId);
        if(foundProducto.isPresent()) {
            productoRepository.findById(productoId);
            return new GlobalResponseHandler().handleResponse("Producto encontrado de forma correcta",
                    foundProducto.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto id " + productoId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }



    /**
     * TODO PARA CAMBIAR QUE DEBE SER QUEMADO VARIOS OBJETOS AL INICIALIZAR.
     *
     * @author Gustavo Alv
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addProducto(@RequestBody Producto newProducto, HttpServletRequest request) {

        Optional<Producto> foundProductoOpt = productoRepository.findByNombre(newProducto.getNombre());
        if(foundProductoOpt.isEmpty()){
//        Producto accesProduct= new Producto();
//        accesProduct.setNombre(newProducto.getNombre());
//        accesProduct.setDescripcion(newProducto.getNombre());
//        accesProduct.setPrecio(newProducto.getPrecio());
//        accesProduct.setCantidad(newProducto.getCantidad());
        productoRepository.save(newProducto);

        return new GlobalResponseHandler().handleResponse("Producto creado correctamente",
                newProducto, HttpStatus.OK, request);}
        else{
            return new GlobalResponseHandler().handleResponse("Producto ya existe en el sistema" + foundProductoOpt.get().getId() + " no fue encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/{productoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long productoId, @RequestBody Producto productoActualizado, HttpServletRequest request) {
        Optional<Producto> foundProductoOpt = productoRepository.findById(productoId);
        if(foundProductoOpt.isPresent()) {

            Producto foundProducto2 = foundProductoOpt.get();


            foundProducto2.setNombre(productoActualizado.getNombre());
            foundProducto2.setDescripcion(productoActualizado.getDescripcion());
            foundProducto2.setPrecio(productoActualizado.getPrecio());
            foundProducto2.setCantidad(productoActualizado.getCantidad());
            foundProducto2.setCategoria(productoActualizado.getCategoria());
            productoRepository.save(foundProducto2);
            return new GlobalResponseHandler().handleResponse(":Producto Actualizado correctamente",
                    foundProducto2, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto id " + productoId + " no fue encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{productoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProducto(@PathVariable Long productoId, HttpServletRequest request) {
        Optional<Producto> foundProducto = productoRepository.findById(productoId);
        if(foundProducto.isPresent()) {
            productoRepository.deleteById(productoId);
            return new GlobalResponseHandler().handleResponse("Producto borrado de forma correcta",
                    foundProducto.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto id " + productoId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }


}
