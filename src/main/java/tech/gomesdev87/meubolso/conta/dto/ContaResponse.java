package tech.gomesdev87.meubolso.conta.dto;

import tech.gomesdev87.meubolso.conta.Conta;
import tech.gomesdev87.meubolso.conta.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContaResponse(
        UUID id,
        String nome,
        TipoConta tipo,
        BigDecimal saldoInicial,
        boolean ativa,
        LocalDateTime createdAt
) {

    public static ContaResponse fromEntity(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNome(),
                conta.getTipo(),
                conta.getSaldoInicial(),
                conta.isAtiva(),
                conta.getCreatedAt()
        );
    }
}