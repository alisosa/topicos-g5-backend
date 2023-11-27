package com.ucu.topicos.mapper;

import com.ucu.topicos.model.ProviderEntity;
import dtos.Provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProviderMapper {

    public static List<Provider> mapProviders (List<ProviderEntity> providersEntity, Integer offset){

        int pageSize = 9;
        int startIndx = null != offset ? offset : 0;
        int endIndx = Math.min(startIndx + pageSize, providersEntity.size());

        if (startIndx < endIndx){
            List<ProviderEntity> subList = providersEntity.subList(startIndx, endIndx);
            return subList.stream().map(providerEntity -> mapProvider(providerEntity)).collect(Collectors.toList());
        }else {
            return Collections.emptyList();
        }

    }

    private static Provider mapProvider (ProviderEntity providerEntity){
        Provider response = new Provider();
        response.setName(providerEntity.getName());
        response.setRut(providerEntity.getRut());
        response.setScore(providerEntity.getScore());
        response.setLogo(providerEntity.getLogo());
        response.setAddress(providerEntity.getAddress());
        response.setEmail(providerEntity.getEmail());
        response.setCategory(providerEntity.getCategory());
        return response;
    }
}
