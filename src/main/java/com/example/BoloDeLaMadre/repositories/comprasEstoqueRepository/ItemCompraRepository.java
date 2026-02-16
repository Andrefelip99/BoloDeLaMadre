package com.example.BoloDeLaMadre.repositories.comprasEstoqueRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.comprasEstoque.ItemCompra;

public interface ItemCompraRepository extends JpaRepository<ItemCompra, UUID> {

    List<ItemCompra> findByCompra_Id(UUID compraId);
}
