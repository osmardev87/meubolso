
package tech.gomesdev87.meubolso.conta;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contas")
@Getter
@Setter
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private tech.gomesdev87.meubolso.user.User user;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoConta tipo;

    @Column(name = "saldo_inicial", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean ativa = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        createdAt = LocalDateTime.now();

        if (saldoInicial == null) {
            saldoInicial = BigDecimal.ZERO;
        }
    }
}