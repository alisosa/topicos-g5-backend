package com.ucu.topicos.repository;

import com.ucu.topicos.model.FormQuestionEntity;
import com.ucu.topicos.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
