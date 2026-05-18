package com.ferrinsa.fairpartner.expense.dto.settlement;

import com.ferrinsa.fairpartner.expense.model.Settlement;

import java.math.BigDecimal;
import java.time.Instant;

public record SettlementResponseDTO(
        Long id,
        Long expenseGroupId,
        SettlementUserDTO fromUserId,
        SettlementUserDTO toUserId,
        BigDecimal amount,
        Instant createDate
) {

    public static SettlementResponseDTO of(Settlement settlement) {
        return new SettlementResponseDTO(
                settlement.getId(),
                settlement.getExpenseGroup().getId(),
                new SettlementUserDTO(settlement.getFromUser().getId(), settlement.getFromUser().getName()),
                new SettlementUserDTO(settlement.getToUser().getId(), settlement.getToUser().getName()),
                settlement.getAmount(),
                settlement.getCreateDate()
        );
    }

}
