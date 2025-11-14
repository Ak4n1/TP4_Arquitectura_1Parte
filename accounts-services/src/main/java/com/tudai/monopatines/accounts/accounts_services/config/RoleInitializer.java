package com.tudai.monopatines.accounts.accounts_services.config;

import com.tudai.monopatines.accounts.accounts_services.entity.Role;
import com.tudai.monopatines.accounts.accounts_services.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inicializador que crea los roles básicos del sistema al iniciar la aplicación.
 * Se ejecuta siempre, independientemente del seeder de datos de ejemplo.
 */
@Component
@Order(0)
public class RoleInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoleInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Inicializando roles básicos del sistema...");
        
        createRoleIfNotExists("ROLE_USER", "Usuario comun del sistema");
        createRoleIfNotExists("ROLE_EMPLOYEE", "Empleado del sistema");
        createRoleIfNotExists("ROLE_ADMIN", "Administrador del sistema");
        
        logger.info("Roles básicos inicializados correctamente");
    }

    /**
     * Crea un rol si no existe.
     */
    private void createRoleIfNotExists(String roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName, description);
            roleRepository.save(role);
            logger.info("Rol creado: {}", roleName);
        } else {
            logger.debug("Rol ya existe, se omite: {}", roleName);
        }
    }
}

