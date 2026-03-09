package com.ferrinsa.fairpartner.expense.service.domain;

import com.ferrinsa.fairpartner.expense.dto.settlement.NewSettlementRequestDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.SettlementResponseDTO;
import com.ferrinsa.fairpartner.expense.dto.settlement.UpdateSettlementRequestDTO;

public interface SettlementService {

    SettlementResponseDTO createSettlement(Long userId, NewSettlementRequestDTO newSettlementRequestDTO);

    SettlementResponseDTO updateSettlement(Long userId, Long settlementId, UpdateSettlementRequestDTO updateSettlementRequestDTO);

    void deleteSettlement(Long userId, Long settlementId);

    SettlementResponseDTO findSettlementById(Long userId, Long settlementId);

}
