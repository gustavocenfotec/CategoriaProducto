package com.project.demo.rest.categoria;



import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;

import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.http.Meta;
import com.project.demo.logic.entity.order.Order;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriasRestController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Categoria> ordersPage = categoriaRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(ordersPage.getTotalPages());
        meta.setTotalElements(ordersPage.getTotalElements());
        meta.setPageNumber(ordersPage.getNumber() + 1);
        meta.setPageSize(ordersPage.getSize());

        return new GlobalResponseHandler().handleResponse("Categorias Recolectadas de forma Correcta",
                ordersPage.getContent(), HttpStatus.OK, meta);
    }


    @GetMapping("/{categoriaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOne(@PathVariable Long categoriaId, HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if(foundCategoria.isPresent()) {



//            Pageable pageable = PageRequest.of(page-1, size);
//            Page<Producto> ordersPage = productoRepository.getProductosBy(userId, pageable);
//            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
//            meta.setTotalPages(ordersPage.getTotalPages());
//            meta.setTotalElements(ordersPage.getTotalElements());
//            meta.setPageNumber(ordersPage.getNumber() + 1);
//            meta.setPageSize(ordersPage.getSize());

            categoriaRepository.findById(categoriaId);
            return new GlobalResponseHandler().handleResponse("Producto encontrado de forma correcta",
                    foundCategoria.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Producto id " + categoriaId + " no encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }




    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> addCategoria(@RequestBody Categoria newCategoria, HttpServletRequest request) {

        Optional<Categoria> foundCategoriaOpt = categoriaRepository.findByNombre(newCategoria.getNombre());
        if(foundCategoriaOpt.isEmpty()) {

            Categoria accesCategoria = new Categoria();

            accesCategoria.setNombre(newCategoria.getNombre());
            accesCategoria.setDescripcion(newCategoria.getNombre());
            categoriaRepository.save(accesCategoria);

            return new GlobalResponseHandler().handleResponse("Categoria creado correctamente",
                    accesCategoria, HttpStatus.OK, request);
        }else {
            return new GlobalResponseHandler().handleResponse("Categoria ya existe en el sistema con el id: " + foundCategoriaOpt.get().getId() + " y fue encontrado"  ,
                    HttpStatus.NOT_ACCEPTABLE, request);
        }
    }

    @PutMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategoria(@PathVariable Long categoriaId, @RequestBody Categoria categoriaActualizada, HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if(foundCategoria.isPresent()) {

            Categoria accesCategoria= foundCategoria.get();
            accesCategoria.setNombre(categoriaActualizada.getNombre());
            accesCategoria.setDescripcion(categoriaActualizada.getNombre());
            categoriaRepository.save(accesCategoria);
            return new GlobalResponseHandler().handleResponse(":Categoria Actualizado correctamente",
                    accesCategoria, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Categoria id " + categoriaId + " no fue encontrado"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }


    @DeleteMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long categoriaId, HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if(foundCategoria.isPresent()) {
            categoriaRepository.deleteById(categoriaId);
            return new GlobalResponseHandler().handleResponse("Categoria borrado de forma correcta",
                    foundCategoria.get(), HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Categoria id " + categoriaId + " no encontrada"  ,
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
