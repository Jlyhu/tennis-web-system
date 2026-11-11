
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.CompraResponse;
import com.fsteni58.tennis.repository.HistorialCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HistorialCompraService {

    private final HistorialCompraRepository repository;

    public HistorialCompraService(HistorialCompraRepository repository) {
        this.repository = repository;
    }

    public List<CompraResponse> consultarHistorial(UUID compradorId) {
        return repository.listarPorComprador(compradorId);
    }
}
