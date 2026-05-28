package br.com.ferdbgg.springtemperaturaumidade.leiturasensor;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.com.ferdbgg.springtemperaturaumidade.exceptions.LeitorPortaSerialException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeitorPortaSerialService {

    private static final int NUMERO_ERROS_LEITURA_CONSECUTIVOS_TOLERAVEL = 10;

    private final LeituraSensorRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void iniciarThreadLeituraDadosSerial() {

        final var threadLeituraDadosSerial = new Thread(this::lerDadosSerial);
        threadLeituraDadosSerial.setName("Thread-Leitura-Dados-Serial");

        threadLeituraDadosSerial.start();

    }

    private void lerDadosSerial() {

        try {
            LeitorPortaSerial.builderLeitor(this::consumerStreamDadosSerial);
        } catch (LeitorPortaSerialException e) {
            System.err.println("Erro na comunicação serial: " + e.getMessage());
        }

    }

    private void consumerStreamDadosSerial(InputStream stream) throws LeitorPortaSerialException {

        try (final var scanner = new Scanner(stream)) {

            var numeroErrosLeituraConsecutivos = 0;

            while (scanner.hasNextLine()) {

                if (numeroErrosLeituraConsecutivos > NUMERO_ERROS_LEITURA_CONSECUTIVOS_TOLERAVEL) {
                    throw new LeitorPortaSerialException("Ocorreram muitos erros de leitura.");
                }

                final var dados = scanner.nextLine().split(",");

                if (dados.length == 2) {
                    numeroErrosLeituraConsecutivos = 0;
                } else {
                    numeroErrosLeituraConsecutivos++;
                    continue;
                }

                final var umidade = new BigDecimal(dados[0].trim());
                final var temperatura = new BigDecimal(dados[1].trim());

                final var leitura = new LeituraSensor(umidade, temperatura);

                repository.save(leitura);

                messagingTemplate.convertAndSend("/topic/sensor", leitura);

            }

        }

    }

}
