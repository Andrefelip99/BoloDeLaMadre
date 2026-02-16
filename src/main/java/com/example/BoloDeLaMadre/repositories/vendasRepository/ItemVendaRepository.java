package com.example.BoloDeLaMadre.repositories.vendasRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BoloDeLaMadre.entities.vendas.ItemVenda;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, UUID> {

    List<ItemVenda> findByVenda_Id(UUID vendaId);
}
