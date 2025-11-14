package com.tudai.monopatines.accounts.accounts_services.controller;

import com.tudai.monopatines.accounts.accounts_services.dto.CreateUserRequest;
import com.tudai.monopatines.accounts.accounts_services.dto.UpdateUserRequest;
import com.tudai.monopatines.accounts.accounts_services.dto.UserResponse;
import com.tudai.monopatines.accounts.accounts_services.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/users")
@Tag(name = "Users", description = "API para gestionar usuarios del sistema")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario en el sistema. El password debe venir hasheado desde auth-service. Se asigna ROLE_USER por defecto."
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
        @Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Obtener usuario por ID",
        description = "Obtiene los datos de un usuario incluyendo sus roles asignados."
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener usuario por email",
        description = "Obtiene los datos de un usuario por su email (unico en el sistema) incluyendo sus roles."
    )
    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna la lista completa de usuarios del sistema, incluyendo sus roles asignados."
    )
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente. No incluye password (se cambia desde auth-service)."
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
}

