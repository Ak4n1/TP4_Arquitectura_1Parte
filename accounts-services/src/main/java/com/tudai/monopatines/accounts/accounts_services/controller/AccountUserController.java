package com.tudai.monopatines.accounts.accounts_services.controller;

import com.tudai.monopatines.accounts.accounts_services.dto.AccountUserRequest;
import com.tudai.monopatines.accounts.accounts_services.dto.AccountUserResponse;
import com.tudai.monopatines.accounts.accounts_services.dto.AccountsByUserResponse;
import com.tudai.monopatines.accounts.accounts_services.dto.UsersByAccountResponse;
import com.tudai.monopatines.accounts.accounts_services.service.AccountUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account-User Relationships", description = "API para gestionar relaciones entre cuentas y usuarios")
public class AccountUserController {

    @Autowired
    private AccountUserService accountUserService;

    @Operation(
        summary = "Asociar usuario a cuenta",
        description = "Crea una relacion entre una cuenta y un usuario, permitiendo que el usuario utilice los creditos cargados en esa cuenta."
    )
    @PostMapping("/associate")
    public ResponseEntity<AccountUserResponse> associateUserToAccount(
        @Valid @RequestBody AccountUserRequest request) {
        AccountUserResponse response = accountUserService.associateUserToAccount(request.getAccountId(), request.getUserId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Desasociar usuario de cuenta",
        description = "Elimina la relacion entre una cuenta y un usuario, impidiendo que el usuario utilice los creditos de esa cuenta."
    )
    @PostMapping("/disassociate")
    public ResponseEntity<AccountUserResponse> disassociateUserFromAccount(
        @Valid @RequestBody AccountUserRequest request) {
        AccountUserResponse response = accountUserService.disassociateUserFromAccount(request.getAccountId(), request.getUserId());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener usuarios de una cuenta",
        description = "Retorna la lista de usuarios asociados a una cuenta, incluyendo sus roles asignados."
    )
    @GetMapping("/{accountId}/users")
    public ResponseEntity<UsersByAccountResponse> getUsersByAccount(@PathVariable Long accountId) {
        UsersByAccountResponse response = accountUserService.getUsersByAccount(accountId);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener cuentas de un usuario",
        description = "Retorna la lista de cuentas asociadas a un usuario. Un usuario puede estar asociado a multiples cuentas."
    )
    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<AccountsByUserResponse> getAccountsByUser(@PathVariable Long userId) {
        AccountsByUserResponse response = accountUserService.getAccountsByUser(userId);
        return ResponseEntity.ok(response);
    }
}

