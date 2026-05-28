package br.com.ferdbgg.springtemperaturaumidade.leiturasensor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leituras")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final LeituraSensorRepository repository;

    @GetMapping
    public List<LeituraSensor> listarHistorico() {
        return repository.findAll();
    }
}
