package br.com.ferdbgg.springtemperaturaumidade.autenticacao;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody DadosAutenticacao dados) {

        final var tokenAutenticacao = //
                new UsernamePasswordAuthenticationToken(
                        dados.username(),
                        dados.password() //
                );

        
        final var autenticacao = manager.authenticate(tokenAutenticacao);

        final var usuarioAutenticado = (Usuario) autenticacao.getPrincipal();

        final var tokenResposta = tokenService.tentarGerarToken(usuarioAutenticado);

        return ResponseEntity.ok(new DadosTokenJWT(tokenResposta));

    }

}
