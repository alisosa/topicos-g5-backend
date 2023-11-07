package com.ucu.topicos.repository;

import com.ucu.topicos.model.ProviderEntity;
import org.hibernate.cfg.annotations.QueryHintDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    //ProviderEntity findByRUT(String rut);
}
