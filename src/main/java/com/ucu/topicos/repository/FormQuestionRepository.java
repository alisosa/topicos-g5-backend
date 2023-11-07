package com.ucu.topicos.repository;

import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.model.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormQuestionRepository extends JpaRepository<FormQuestionEntity, Long> {
}
