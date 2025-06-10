package com.project.demo.logic.entity.producto;



import com.project.demo.logic.entity.producto.ProductoRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import java.util.Optional;
@Component
@Order(4)
public class ProductoSeeder implements ApplicationListener<ContextRefreshedEvent> {


    private final ProductoRepository productoRepository;

    public ProductoSeeder(
            ProductoRepository productoRepository
    ) {
        this.productoRepository = productoRepository;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createProductos();
    }

    private void createProductos() {

        Producto producto = new Producto();
        producto.setNombre("Trits");
        producto.setDescripcion("Producto de Dos pinos");
        producto.setPrecio(1000);
        producto.setCantidad(10);

        if (productoRepository.findByNombre(producto.getNombre()).isPresent()) {
            return;
        }

        productoRepository.save(producto);


        producto = new Producto();
        producto.setNombre("Tomate");
        producto.setDescripcion("Kg de Tomate");
        producto.setPrecio(2500);
        producto.setCantidad(100);

        if (productoRepository.findByNombre(producto.getNombre()).isPresent()) {
            return;
        }

        productoRepository.save(producto);

    }



}
