package com.project.demo.logic.entity.categoria;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoriaSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoriaRepository categoriaRepository;


    public CategoriaSeeder(
            CategoriaRepository categoriaRepository
    ) {
        this.categoriaRepository = categoriaRepository;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createCategoria();
    }

    private void createCategoria() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Lacteos");
        categoria1.setDescripcion("Categoria dedicada a lechee");

        Optional<Categoria> optionalCategoria = categoriaRepository.findByNombre(categoria1.getNombre());

        if (optionalCategoria.isPresent()) {
            return;
        }

        var categoria = new Categoria();
        categoria.setNombre(categoria1.getNombre());
        categoria.setDescripcion(categoria1.getDescripcion());

        categoriaRepository.save(categoria);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Vegetales");
        categoria2.setDescripcion("Categoria dedicada a veggies");

        optionalCategoria = categoriaRepository.findByNombre(categoria2.getNombre());

        if (optionalCategoria.isPresent()) {
            return;
        }

        categoria = new Categoria();
        categoria.setNombre(categoria2.getNombre());
        categoria.setDescripcion(categoria2.getDescripcion());

        categoriaRepository.save(categoria);

    }
}
