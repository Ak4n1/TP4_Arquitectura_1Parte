package com.tudai.monopatines.accounts.accounts_services.repository;

import com.tudai.monopatines.accounts.accounts_services.entity.Account;
import com.tudai.monopatines.accounts.accounts_services.entity.AccountUser;
import com.tudai.monopatines.accounts.accounts_services.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad AccountUser.
 * Proporciona métodos para realizar operaciones CRUD sobre la relación entre cuentas y usuarios.
 * 
 */
@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

    /**
     * Busca todas las relaciones de una cuenta (usuarios asociados a la cuenta).
     * 
     * @param account Cuenta
     * @return Lista de AccountUser con todos los usuarios asociados a la cuenta
     */
    List<AccountUser> findByAccount(Account account);

    /**
     * Busca todas las relaciones de un usuario (cuentas asociadas al usuario).
     * 
     * @param user Usuario
     * @return Lista de AccountUser con todas las cuentas asociadas al usuario
     */
    List<AccountUser> findByUser(User user);

    /**
     * Busca la relación entre una cuenta y un usuario específico.
     * 
     * @param account Cuenta
     * @param user Usuario
     * @return Optional con la relación encontrada o vacío si no existe
     */
    Optional<AccountUser> findByAccountAndUser(Account account, User user);

    /**
     * Verifica si existe una relación entre una cuenta y un usuario.
     * 
     * @param account Cuenta
     * @param user Usuario
     * @return true si existe la relación, false si no existe
     */
    boolean existsByAccountAndUser(Account account, User user);
}

