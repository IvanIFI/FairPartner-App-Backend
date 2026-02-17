package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "invitation")
public class Invitation {

    public enum InvitationStatus {
        SENT, ACCEPTED, REJECTED, EXPIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "expense_group_id", nullable = false)
    private ExpenseGroup expenseGroup;
    @ManyToOne
    @JoinColumn(name = "inviter_user_id", nullable = false)
    private UserEntity inviterUser;
    @ManyToOne
    @JoinColumn(name = "invited_user_id", nullable = false)
    private UserEntity invitedUser;
    @Column(nullable = false, unique = true)
    private String token;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    public Invitation() {
        // Required by JPA
    }

    public Invitation(ExpenseGroup expenseGroup,
                      UserEntity inviterUser,
                      UserEntity invitedUser,
                      String token, InvitationStatus status) {
        this.expenseGroup = expenseGroup;
        this.inviterUser = inviterUser;
        this.invitedUser = invitedUser;
        this.token = token;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseGroup getExpenseGroup() {
        return expenseGroup;
    }

    public void setExpenseGroup(ExpenseGroup expenseGroup) {
        this.expenseGroup = expenseGroup;
    }

    public UserEntity getInviterUser() {
        return inviterUser;
    }

    public void setInviterUser(UserEntity inviterUser) {
        this.inviterUser = inviterUser;
    }

    public UserEntity getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(UserEntity invitedUser) {
        this.invitedUser = invitedUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation that = (Invitation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
