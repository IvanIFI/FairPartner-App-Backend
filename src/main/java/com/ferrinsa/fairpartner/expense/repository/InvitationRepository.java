package com.ferrinsa.fairpartner.expense.repository;

import com.ferrinsa.fairpartner.expense.model.ExpenseGroup;
import com.ferrinsa.fairpartner.expense.model.Invitation;
import com.ferrinsa.fairpartner.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    Optional<Invitation> findByToken (String token);

    List<Invitation> findByInviterUser_IdOrderByCreationDateDesc(Long inviterUserId);

    boolean existsByExpenseGroupAndInvitedUserAndStatus(
            ExpenseGroup group,
            UserEntity invitedUser,
            Invitation.InvitationStatus status
    );

}
