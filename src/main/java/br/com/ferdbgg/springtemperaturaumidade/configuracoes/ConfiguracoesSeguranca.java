package br.com.ferdbgg.springtemperaturaumidade.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.ferdbgg.springtemperaturaumidade.autenticacao.TokenService;
import br.com.ferdbgg.springtemperaturaumidade.autenticacao.UsuarioRepository;
import br.com.ferdbgg.springtemperaturaumidade.seguranca.FiltroSeguranca;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfiguracoesSeguranca {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(this::authorizeHttpRequests)
                // Adiciona o filtro customizado antes do filtro de autenticação padrão
                .addFilterBefore(
                        new FiltroSeguranca(tokenService, usuarioRepository),
                        UsernamePasswordAuthenticationFilter.class //
                )
                .build();
    }

    private void authorizeHttpRequests(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request //
    ) {
        
        request
                // Permite fazer o POST de Login sem token
                .requestMatchers(HttpMethod.POST, "/api/autenticacao/login").permitAll()
                // Permite o Swagger funcionar para documentar
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Permite acessar o endpoint do WebSocket sem token
                .requestMatchers("/ws-sensor/**").permitAll()
                // Bloqueia qualquer outra rota
                .anyRequest().authenticated();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

}
