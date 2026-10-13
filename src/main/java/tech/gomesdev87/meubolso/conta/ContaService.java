
package tech.gomesdev87.meubolso.conta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.gomesdev87.meubolso.conta.dto.ContaRequest;
import tech.gomesdev87.meubolso.conta.dto.ContaResponse;
import tech.gomesdev87.meubolso.user.User;
import tech.gomesdev87.meubolso.user.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final UserRepository userRepository;

    private User user(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado."));

    }

    @Transactional
    public ContaResponse criarConta(String email, ContaRequest request) {
        User user = this.user(email);

        Conta conta = new Conta();
        conta.setUser(user);
        conta.setNome(request.nome().trim());
        conta.setTipo(request.tipo());
        conta.setSaldoInicial(request.saldoInicial());

        Conta salva = contaRepository.save(conta);

        return ContaResponse.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> listarContas(String email) {
        User user = this.user(email);
        return contaRepository.findAllByUserId(user.getId())
                .stream()
                .map(ContaResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarConta(UUID contaId, String email) {
        User user =  this.user(email);
        Conta conta = contaRepository.findByIdAndUserId(contaId, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Conta não encontrada."));

        return ContaResponse.fromEntity(conta);
    }
}