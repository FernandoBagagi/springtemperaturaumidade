package br.com.ferdbgg.springtemperaturaumidade.leiturasensor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final LeituraSensorRepository repository;

    @GetMapping
    public List<LeituraSensor> listarHistorico() {
        return repository.findAll();
    }
}
