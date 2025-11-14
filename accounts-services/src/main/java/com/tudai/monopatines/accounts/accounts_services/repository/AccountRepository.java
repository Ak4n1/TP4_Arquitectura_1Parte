package com.tudai.monopatines.accounts.accounts_services.repository;

import com.tudai.monopatines.accounts.accounts_services.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Account.
 * Proporciona métodos para realizar operaciones CRUD sobre cuentas.
 * 
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Verifica si existe una cuenta con el número identificatorio especificado.
     * 
     * @param identificationNumber Número identificatorio de la cuenta
     * @return true si existe una cuenta con ese número identificatorio, false en caso contrario
     */
    boolean existsByIdentificationNumber(String identificationNumber);

    /**
     * Busca una cuenta por su número identificatorio.
     * 
     * @param identificationNumber Número identificatorio de la cuenta
     * @return Optional con la cuenta encontrada o vacío si no existe
     */
    java.util.Optional<Account> findByIdentificationNumber(String identificationNumber);

    /**
     * Obtiene todas las cuentas activas (no anuladas).
     * 
     * @return Lista de cuentas activas
     */
    List<Account> findByActiveTrue();

    /**
     * Obtiene todas las cuentas inactivas (anuladas).
     * 
     * @return Lista de cuentas inactivas
     */
    List<Account> findByActiveFalse();
}

