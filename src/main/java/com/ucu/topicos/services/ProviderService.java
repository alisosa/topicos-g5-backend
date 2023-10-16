package com.ucu.topicos.services;

import com.ucu.topicos.mapper.ProviderMapper;
import com.ucu.topicos.model.ProviderEntity;
import com.ucu.topicos.repository.ProviderRepository;
import dtos.ProvidersResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;


    public ProvidersResponse getProviders (String nombre, String rut,
                                           Integer puntajeDesde, Integer puntajeHasta, Integer offset){

        try{
            ProvidersResponse response = new ProvidersResponse();

            List<ProviderEntity> providers = providerRepository.findAll();

            List<ProviderEntity> filteredProviders = providers.stream()
                    .filter(p -> null == nombre || p.getName().contains(nombre))
                    .filter(p -> null == rut || p.getRut().contains(rut))
                    .filter(p -> null == puntajeDesde || p.getScore() >= puntajeDesde)
                    .filter(p -> null == puntajeHasta || p.getScore() <= puntajeHasta)
                    .collect(Collectors.toList());

            response.setPages(filteredProviders.isEmpty() ? 0 : filteredProviders.size() / 9);
            response.setProviders(ProviderMapper.mapProviders(filteredProviders, offset));

            return response;

        }catch (Exception e){
            return null;
        }
    }
}
