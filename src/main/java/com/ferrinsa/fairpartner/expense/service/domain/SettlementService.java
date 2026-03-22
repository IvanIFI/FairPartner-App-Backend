package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.dto.settlement.NewSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.UpdateSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.model.Settlement;

import java.util.List;

public interface SettlementService {

    Settlement createSettlement(Long userId, NewSettlementRequestDTO newSettlementRequestDTO);

    Settlement getSettlementById(Long userId, Long settlementId);

    List<Settlement> findAllByGroupId(Long userId, Long expenseGroupId);

    Settlement updateSettlement(Long userId, Long settlementId, UpdateSettlementRequestDTO updateSettlementRequestDTO);

    void deleteSettlement(Long userId, Long settlementId);

}
