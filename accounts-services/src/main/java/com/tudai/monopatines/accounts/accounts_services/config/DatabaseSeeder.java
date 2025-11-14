package com.tudai.monopatines.accounts.accounts_services.config;

import com.tudai.monopatines.accounts.accounts_services.entity.Account;
import com.tudai.monopatines.accounts.accounts_services.entity.AccountUser;
import com.tudai.monopatines.accounts.accounts_services.entity.Role;
import com.tudai.monopatines.accounts.accounts_services.entity.User;
import com.tudai.monopatines.accounts.accounts_services.entity.UserRole;
import com.tudai.monopatines.accounts.accounts_services.repository.AccountRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.AccountUserRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.RoleRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.UserRepository;
import com.tudai.monopatines.accounts.accounts_services.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeder para poblar la base de datos con datos iniciales.
 * Solo se ejecuta si la propiedad app.seed.enabled está en true.
 * Verifica la existencia de registros antes de crearlos para evitar duplicados.
 */
@Component
@Order(1)
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private AccountUserRepository accountUserRepository;

    @Value("${app.seed.enabled:false}")
    private boolean seedEnabled;

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            logger.info("Database seeder está deshabilitado (app.seed.enabled=false)");
            return;
        }

        logger.info("Iniciando database seeder...");
        
        seedRoles();
        seedUsers();
        seedAccounts();
        seedAccountUsers();
        
        logger.info("Database seeder completado exitosamente");
    }

    /**
     * Crea los roles básicos del sistema si no existen.
     */
    private void seedRoles() {
        logger.info("Poblando roles...");
        
        createRoleIfNotExists("ROLE_USER", "Usuario estándar del sistema");
        createRoleIfNotExists("ROLE_EMPLOYEE", "Empleado del sistema");
        createRoleIfNotExists("ROLE_ADMIN", "Administrador del sistema");
        
        logger.info("Roles poblados correctamente");
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

    /**
     * Crea usuarios de ejemplo si no existen.
     */
    private void seedUsers() {
        logger.info("Poblando usuarios de ejemplo...");
        
        // Usuario estándar
        User user1 = createUserIfNotExists(
            "Juan",
            "Pérez",
            "juan.perez@example.com",
            "+5491112345678"
        );
        if (user1 != null) {
            assignRoleIfNotExists(user1, "ROLE_USER");
        }

        // Usuario empleado
        User user2 = createUserIfNotExists(
            "María",
            "González",
            "maria.gonzalez@example.com",
            "+5491198765432"
        );
        if (user2 != null) {
            assignRoleIfNotExists(user2, "ROLE_USER");
            assignRoleIfNotExists(user2, "ROLE_EMPLOYEE");
        }

        // Usuario administrador
        User user3 = createUserIfNotExists(
            "Admin",
            "Sistema",
            "admin@example.com",
            "+5491100000000"
        );
        if (user3 != null) {
            assignRoleIfNotExists(user3, "ROLE_USER");
            assignRoleIfNotExists(user3, "ROLE_ADMIN");
        }

        logger.info("Usuarios poblados correctamente");
    }

    /**
     * Crea un usuario si no existe.
     */
    private User createUserIfNotExists(String firstName, String lastName, String email, String phoneNumber) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User(firstName, lastName, email, phoneNumber);
            user = userRepository.save(user);
            logger.info("Usuario creado: {} {} ({})", firstName, lastName, email);
            return user;
        } else {
            logger.debug("Usuario ya existe, se omite: {}", email);
            return userRepository.findByEmail(email).orElse(null);
        }
    }

    /**
     * Asigna un rol a un usuario si no está asignado.
     */
    private void assignRoleIfNotExists(User user, String roleName) {
        java.util.Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Rol no encontrado: " + roleName);
        }
        Role role = roleOptional.get();
        
        if (!userRoleRepository.existsByUserAndRole(user, role)) {
            UserRole userRole = new UserRole(user, role);
            userRoleRepository.save(userRole);
            logger.info("Rol {} asignado al usuario {}", roleName, user.getEmail());
        } else {
            logger.debug("Rol {} ya está asignado al usuario {}, se omite", roleName, user.getEmail());
        }
    }

    /**
     * Crea cuentas de ejemplo si no existen.
     */
    private void seedAccounts() {
        logger.info("Poblando cuentas de ejemplo...");
        
        // Cuenta activa con saldo
        createAccountIfNotExists(
            "ACC-001",
            "MP-123456789",
            1000.0,
            true
        );

        // Cuenta activa sin saldo
        createAccountIfNotExists(
            "ACC-002",
            "MP-987654321",
            0.0,
            true
        );

        // Cuenta inactiva
        createAccountIfNotExists(
            "ACC-003",
            "MP-555555555",
            500.0,
            false
        );

        logger.info("Cuentas pobladas correctamente");
    }

    /**
     * Crea una cuenta si no existe.
     */
    private void createAccountIfNotExists(String identificationNumber, String mercadoPagoAccountId, 
                                         Double initialBalance, boolean active) {
        if (!accountRepository.existsByIdentificationNumber(identificationNumber)) {
            Account account = new Account(identificationNumber, mercadoPagoAccountId);
            account.setCurrentBalance(initialBalance);
            account.setActive(active);
            if (!active) {
                account.cancel();
            }
            accountRepository.save(account);
            logger.info("Cuenta creada: {} (Saldo: {}, Activa: {})", 
                identificationNumber, initialBalance, active);
        } else {
            logger.debug("Cuenta ya existe, se omite: {}", identificationNumber);
        }
    }

    /**
     * Asocia usuarios a cuentas de ejemplo.
     */
    private void seedAccountUsers() {
        logger.info("Poblando relaciones cuenta-usuario...");
        
        // Obtener usuarios
        java.util.Optional<User> user1Optional = userRepository.findByEmail("juan.perez@example.com");
        java.util.Optional<User> user2Optional = userRepository.findByEmail("maria.gonzalez@example.com");
        java.util.Optional<User> user3Optional = userRepository.findByEmail("admin@example.com");
        
        // Obtener cuentas
        Account account1 = accountRepository.findByIdentificationNumber("ACC-001").orElse(null);
        Account account2 = accountRepository.findByIdentificationNumber("ACC-002").orElse(null);
        Account account3 = accountRepository.findByIdentificationNumber("ACC-003").orElse(null);
        
        // Asociar Juan Pérez a ACC-001 (cuenta activa con saldo)
        if (user1Optional.isPresent() && account1 != null) {
            associateAccountUserIfNotExists(account1, user1Optional.get());
        }
        
        // Asociar María González a ACC-002 (cuenta activa sin saldo)
        if (user2Optional.isPresent() && account2 != null) {
            associateAccountUserIfNotExists(account2, user2Optional.get());
        }
        
        // Asociar Admin a ACC-001 y ACC-003 (múltiples cuentas)
        if (user3Optional.isPresent()) {
            if (account1 != null) {
                associateAccountUserIfNotExists(account1, user3Optional.get());
            }
            if (account3 != null) {
                associateAccountUserIfNotExists(account3, user3Optional.get());
            }
        }
        
        logger.info("Relaciones cuenta-usuario pobladas correctamente");
    }

    /**
     * Asocia un usuario a una cuenta si no existe la relación.
     */
    private void associateAccountUserIfNotExists(Account account, User user) {
        if (!accountUserRepository.existsByAccountAndUser(account, user)) {
            AccountUser accountUser = new AccountUser(account, user);
            accountUserRepository.save(accountUser);
            logger.info("Usuario {} asociado a cuenta {}", user.getEmail(), account.getIdentificationNumber());
        } else {
            logger.debug("Relación ya existe entre usuario {} y cuenta {}, se omite", 
                user.getEmail(), account.getIdentificationNumber());
        }
    }
}

