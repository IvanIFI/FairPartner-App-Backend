package com.ferrinsa.fairpartner.expense.model;

import com.ferrinsa.fairpartner.user.model.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "invitation")
public class Invitation {

    public enum InvitationStatus {
        SENT, ACCEPTED, REJECTED, EXPIRED, CANCELED
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
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    public Invitation() {
        // Required by JPA
    }

    public Invitation(ExpenseGroup expenseGroup, UserEntity inviterUser, UserEntity invitedUser, String token, LocalDateTime creationDate, LocalDateTime expirationDate) {
        this.expenseGroup = expenseGroup;
        this.inviterUser = inviterUser;
        this.invitedUser = invitedUser;
        this.token = token;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.status = InvitationStatus.SENT;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
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
