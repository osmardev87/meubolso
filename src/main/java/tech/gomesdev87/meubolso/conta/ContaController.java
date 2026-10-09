
package tech.gomesdev87.meubolso.conta;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.gomesdev87.meubolso.conta.dto.ContaRequest;
import tech.gomesdev87.meubolso.conta.dto.ContaResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Contas")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponse criar(
            @Valid @RequestBody ContaRequest request,
            Authentication authentication
    ) {

        return contaService.criarConta(getEmail(authentication), request);
    }

    @GetMapping
    public List<ContaResponse> listar(Authentication authentication) {

        return contaService.listarContas(getEmail(authentication));
    }

    @GetMapping("/{id}")
    public ContaResponse buscar(
            @PathVariable UUID id,
            Authentication authentication
    ) {

        return contaService.buscarConta(id, getEmail(authentication));
    }

    private String getEmail(Authentication authentication) {
        // Por enquanto, o JWT autentica pelo e-mail.
        return authentication.getName();
    }
}