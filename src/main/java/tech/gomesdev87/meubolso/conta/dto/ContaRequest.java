package tech.gomesdev87.meubolso.conta.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.gomesdev87.meubolso.conta.TipoConta;

import java.math.BigDecimal;

public record ContaRequest(

        @NotBlank(message = "O nome da conta é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotNull(message = "O tipo da conta é obrigatório.")
        TipoConta tipo,

        @NotNull(message = "O saldo inicial é obrigatório.")
        @DecimalMin(value = "0.00", message = "O saldo inicial não pode ser negativo.")
        BigDecimal saldoInicial

) {}