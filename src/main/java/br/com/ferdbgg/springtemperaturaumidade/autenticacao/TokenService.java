package br.com.ferdbgg.springtemperaturaumidade.autenticacao;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenService {

    public static final long ID_ERRO_VALIDACAO_TOKEN = -1L;

    private static final String ISSUER = "Spring Temperatura e Umidade";
    private static final String CLAIM_ID = "id";
    private static final long SEGUNDOS_VALIDADE_TOKEN = 2L * 60L * 60L; // 2h

    @Value("${springtemperaturaumidade.token.jwt.secret-key}")
    private String secret;

    public String tentarGerarToken(Usuario usuario) throws JWTCreationException {

        final var agora = Instant.now();

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_ID, usuario.getId())
                .withSubject(usuario.getUsername())
                .withIssuedAt(agora)
                .withExpiresAt(agora.plusSeconds(SEGUNDOS_VALIDADE_TOKEN))
                .sign(getAlgoritmo());

    }

    public long validarToken(String token) {

        try {

            return JWT.require(getAlgoritmo())
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getClaim(CLAIM_ID).asLong();

        } catch (Exception e) {

            return ID_ERRO_VALIDACAO_TOKEN;

        }
    }

    private Algorithm getAlgoritmo() {
        return Algorithm.HMAC256(secret);
    }

}
