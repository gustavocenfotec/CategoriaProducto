package com.project.demo.logic.entity.producto;

import com.project.demo.logic.entity.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
