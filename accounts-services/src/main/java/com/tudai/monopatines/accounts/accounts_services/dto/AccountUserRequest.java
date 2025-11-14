package com.tudai.monopatines.accounts.accounts_services.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) para asociar o desasociar un usuario de una cuenta.
 * 
 * Se utiliza en los endpoints POST para recibir los IDs de la cuenta y el usuario
 * desde el cliente. Las validaciones se realizan automáticamente con @Valid
 * en el controller.
 * 
 */
public class AccountUserRequest {

    /**
     * ID de la cuenta.
     * No puede ser null.
     */
    @NotNull(message = "Account ID is required")
    private Long accountId;

    /**
     * ID del usuario.
     * No puede ser null.
     */
    @NotNull(message = "User ID is required")
    private Long userId;

    // Constructors
    public AccountUserRequest() {
    }

    public AccountUserRequest(Long accountId, Long userId) {
        this.accountId = accountId;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

