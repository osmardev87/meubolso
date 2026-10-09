package tech.gomesdev87.meubolso.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tech.gomesdev87.meubolso.user.User;


public record RegisterRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 180, message = "Email deve ter no máximo 180 caracteres")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String phone,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter entre 6 caracteres")
        String password
) {

        public static User toRequest(RegisterRequest request) {
                User user = new User();
                user.setName(request.name);
                user.setEmail(request.email().trim().toLowerCase());
                user.setPassword(request.password);
                user.setPhone(request.phone);
                return  user;
        }
}

