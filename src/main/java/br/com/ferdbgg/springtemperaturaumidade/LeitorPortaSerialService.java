package br.com.ferdbgg.springtemperaturaumidade;

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

    private final LeituraSensorRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void iniciarLeituraPortaSerial() {
        final Thread leitorDadosSerialThread = new Thread(this::lerDadosSerial);
        leitorDadosSerialThread.setName("Thread-Leitor-Dados-Serial");
        leitorDadosSerialThread.start();
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

            while (scanner.hasNextLine()) {

                final var linha = scanner.nextLine();
                final var dados = linha.split(",");

                if (dados.length != 2) {
                    System.err.println("Dados inconsistentes");
                    continue;
                }

                final var temperatura = new BigDecimal(dados[0].trim());
                final var umidade = new BigDecimal(dados[1].trim());

                final var leitura = new LeituraSensor(
                        temperatura,
                        umidade);

                repository.save(leitura);

                messagingTemplate.convertAndSend("/topic/sensor", leitura);

            }

        }

    }

}
