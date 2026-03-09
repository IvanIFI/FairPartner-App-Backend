package com.ferrinsa.fairpartner.exception.expense.settlement;

import com.ferrinsa.fairpartner.exception.AppException;

public class OnlyOneMemberException extends AppException {

    public OnlyOneMemberException() {
        super("ONLY_ONE_MEMBER", "No se puede liquidar con  un unico miembro en el grupo");
    }

}
