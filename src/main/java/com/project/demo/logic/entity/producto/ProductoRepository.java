package com.project.demo.logic.entity.producto;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT u FROM Producto u WHERE u.nombre = ?1")
    Optional<Producto> findByNombre(String nombre);

}
