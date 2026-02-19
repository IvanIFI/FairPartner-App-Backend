package com.ferrinsa.fairpartner.exception.expense.expensegroup;

import com.ferrinsa.fairpartner.exception.AppException;

public class UserNotMemberOfGroupException extends AppException {

    public UserNotMemberOfGroupException(String userValue, String expenseGroupValue) {
        super("USER_NOT_MEMBER_OF_GROUP",
                String.format("El usuario %s no es miembro del grupo %s", userValue, expenseGroupValue));
    }

}
