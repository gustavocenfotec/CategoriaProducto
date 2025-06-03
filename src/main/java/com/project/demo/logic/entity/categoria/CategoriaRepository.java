package com.project.demo.logic.entity.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
