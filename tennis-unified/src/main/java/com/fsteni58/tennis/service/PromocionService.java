
package com.fsteni58.tennis.service;

import com.fsteni58.tennis.dto.PromocionResponse;
import com.fsteni58.tennis.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PromocionService {

    private final PromocionRepository repository;

    public PromocionService(PromocionRepository repository) {
        this.repository = repository;
    }

    public List<PromocionResponse> obtenerParaComprador(UUID compradorId) {

        return repository.obtenerRecomendadas(compradorId);
    }
}
