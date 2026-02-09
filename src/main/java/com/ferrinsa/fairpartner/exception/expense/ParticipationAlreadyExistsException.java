package com.ferrinsa.fairpartner.exception.expense;

import com.ferrinsa.fairpartner.exception.AppException;

public class ParticipationAlreadyExistsException extends AppException {

    public ParticipationAlreadyExistsException(String userValue, String expenseGroupValue) {
        super("PARTICIPATION_ALREADY_EXISTS",
                String.format("User %s already exists in group %s", userValue, expenseGroupValue));
    }
}
