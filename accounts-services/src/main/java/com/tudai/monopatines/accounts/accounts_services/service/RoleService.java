package com.tudai.monopatines.accounts.accounts_services.service;

import com.tudai.monopatines.accounts.accounts_services.entity.Role;
import com.tudai.monopatines.accounts.accounts_services.entity.User;
import com.tudai.monopatines.accounts.accounts_services.entity.UserRole;
import com.tudai.monopatines.accounts.accounts_services.repository.RoleRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.UserRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar roles y asignaciones de roles a usuarios.
 * 
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Obtiene todos los roles asignados a un usuario.
     * 
     * Retorna la lista de nombres de roles (ej: "ROLE_USER", "ROLE_ADMIN")
     * asignados al usuario especificado.
     * 
     * @param userId ID del usuario
     * @return Lista de nombres de roles asignados al usuario
     */
    @Transactional(readOnly = true)
    public List<String> getRolesByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<String> roles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getRole() != null) {
                roles.add(userRole.getRole().getName());
            }
        }
        return roles;
    }

    /**
     * Asigna un rol a un usuario.
     * 
     * Crea una relación entre el usuario y el rol especificado.
     * El rol debe existir previamente en el sistema.
     * 
     * @param userId ID del usuario
     * @param roleName Nombre del rol (ej: "ROLE_USER", "ROLE_ADMIN")
     * @throws RuntimeException si el usuario no existe o si el rol no existe
     */
    public void assignRoleToUser(Long userId, String roleName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        User user = userOptional.get();

        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role not found with name: " + roleName);
        }
        Role role = roleOptional.get();

        // Verificar si ya existe la asignación
        if (!userRoleRepository.existsByUserAndRole(user, role)) {
            UserRole userRole = new UserRole(user, role);
            userRoleRepository.save(userRole);
        }
    }

    /**
     * Elimina un rol de un usuario.
     * 
     * Elimina la relación entre el usuario y el rol especificado.
     * 
     * @param userId ID del usuario
     * @param roleName Nombre del rol a eliminar
     */
    public void removeRoleFromUser(Long userId, String roleName) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        User user = userOptional.get();

        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isEmpty()) {
            return;
        }
        Role role = roleOptional.get();

        Optional<UserRole> userRoleOptional = userRoleRepository.findByUserAndRole(user, role);
        if (userRoleOptional.isPresent()) {
            userRoleRepository.delete(userRoleOptional.get());
        }
    }

    /**
     * Crea un rol si no existe.
     * 
     * @param roleName Nombre del rol a crear
     * @return Role creado o existente
     */
    public Role createRoleIfNotExists(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        Role role = new Role(roleName);
        return roleRepository.save(role);
    }
}
