
package tech.gomesdev87.meubolso.conta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {

    List<Conta> findAllByUserId(UUID userId);

    Optional<Conta> findByIdAndUserId(UUID id, UUID userId);
}