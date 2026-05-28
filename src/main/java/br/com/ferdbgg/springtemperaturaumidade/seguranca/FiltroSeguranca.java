package br.com.ferdbgg.springtemperaturaumidade.seguranca;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ferdbgg.springtemperaturaumidade.autenticacao.TokenService;
import br.com.ferdbgg.springtemperaturaumidade.autenticacao.Usuario;
import br.com.ferdbgg.springtemperaturaumidade.autenticacao.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FiltroSeguranca extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain //
    ) throws ServletException, IOException {

        final var token = extrairToken(request.getHeader("Authorization"));

        final var idUsuario = tokenService.validarToken(token);

        if (idUsuario != TokenService.ID_ERRO_VALIDACAO_TOKEN) {

            final var usuario = usuarioRepository
                    .findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(getAuthentication(usuario));

        }

        filterChain.doFilter(request, response);
        
    }

    private String extrairToken(String authorizationHeader) {

        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.replace("Bearer ", "")
                : "";

    }

    private Authentication getAuthentication(Usuario usuario) {
        
        return new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                usuario.getAuthorities() //
        );

    }

}
