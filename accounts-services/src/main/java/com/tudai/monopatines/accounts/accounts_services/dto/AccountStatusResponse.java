package com.tudai.monopatines.accounts.accounts_services.dto;

/**
 * DTO (Data Transfer Object) para la respuesta del estado de una cuenta.
 * 
 * Se utiliza en el endpoint GET /api/accounts/{id}/active para retornar
 * el estado de la cuenta junto con su ID.
 * 
 */
public class AccountStatusResponse {

    /**
     * ID de la cuenta.
     */
    private Long accountId;

    /**
     * Estado de la cuenta (true si está activa, false si está anulada).
     */
    private Boolean status;

    // Constructors
    public AccountStatusResponse() {
    }

    public AccountStatusResponse(Long accountId, Boolean status) {
        this.accountId = accountId;
        this.status = status;
    }

    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

