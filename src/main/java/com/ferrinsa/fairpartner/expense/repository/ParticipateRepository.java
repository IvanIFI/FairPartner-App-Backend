package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.Participate;
import com.ferrinsa.fairpartner.expense.model.compositeid.ParticipateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipateRepository extends JpaRepository<Participate, ParticipateId> {
}
