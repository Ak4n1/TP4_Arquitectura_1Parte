package com.tudai.monopatines.accounts.accounts_services.controller;

import com.tudai.monopatines.accounts.accounts_services.dto.AccountRequest;
import com.tudai.monopatines.accounts.accounts_services.dto.AccountResponse;
import com.tudai.monopatines.accounts.accounts_services.dto.AccountStatusResponse;
import com.tudai.monopatines.accounts.accounts_services.dto.BalanceRequest;
import com.tudai.monopatines.accounts.accounts_services.dto.BalanceResponse;
import com.tudai.monopatines.accounts.accounts_services.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "API para gestionar cuentas del sistema")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(
        summary = "Crear cuenta",
        description = "Crea una nueva cuenta asociada a una cuenta de Mercado Pago."
    )
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
        @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Obtener cuenta por ID",
        description = "Obtiene los datos de una cuenta por su identificador unico."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener todas las cuentas",
        description = "Retorna la lista completa de cuentas del sistema."
    )
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> response = accountService.getAllAccounts();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener cuentas activas",
        description = "Retorna unicamente las cuentas que estan activas (no anuladas)."
    )
    @GetMapping("/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccounts() {
        List<AccountResponse> response = accountService.getActiveAccounts();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Actualizar cuenta",
        description = "Actualiza los datos de una cuenta existente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
        @PathVariable Long id,
        @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Anular o reactivar cuenta",
        description = "Anula o reactiva una cuenta dinámicamente. Si la cuenta está activa, la anula. Si está anulada, la reactiva."
    )
    @PutMapping("/{id}/toggle_status")
    public ResponseEntity<AccountResponse> cancelAccount(@PathVariable Long id) {
        AccountResponse response = accountService.cancelAccount(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Cargar saldo a cuenta",
        description = "Incrementa el saldo actual de la cuenta con el monto especificado."
    )
    @PutMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> loadBalance(
        @PathVariable Long id,
        @Valid @RequestBody BalanceRequest request) {
        BalanceResponse response = accountService.loadBalance(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener saldo de cuenta",
        description = "Retorna el saldo actual de una cuenta."
    )
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long id) {
        BalanceResponse response = accountService.getBalance(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Descontar saldo de cuenta",
        description = "Descuenta un monto del saldo de una cuenta. Se utiliza cuando se activa un monopatin o se finaliza un viaje."
    )
    @PutMapping("/{id}/balance/deduct")
    public ResponseEntity<BalanceResponse> deductBalance(
        @PathVariable Long id,
        @RequestParam Double amount) {
        BalanceResponse response = accountService.deductBalance(id, amount);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Verificar si cuenta esta activa",
        description = "Retorna un JSON con el estado de la cuenta (status: true/false) y el ID de la cuenta. Usado por otros microservicios."
    )
    @GetMapping("/{id}/active")
    public ResponseEntity<AccountStatusResponse> isAccountActive(@PathVariable Long id) {
        AccountStatusResponse response = accountService.isAccountActive(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Eliminar cuenta",
        description = "Elimina una cuenta del sistema permanentemente."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}

