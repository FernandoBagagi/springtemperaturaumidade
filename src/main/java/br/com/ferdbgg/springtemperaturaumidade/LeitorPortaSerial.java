package br.com.ferdbgg.springtemperaturaumidade;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import com.fazecast.jSerialComm.SerialPort;

import br.com.ferdbgg.springtemperaturaumidade.exceptions.LeitorPortaSerialException;
import br.com.ferdbgg.springtemperaturaumidade.exceptions.NenhumaPortaSerialDisponivelException;
import br.com.ferdbgg.springtemperaturaumidade.exceptions.PortaSerialNaoAbertaException;
import br.com.ferdbgg.springtemperaturaumidade.exceptions.PortaSerialNaoSelecionadaException;

public class LeitorPortaSerial implements AutoCloseable {

    private SerialPort porta;

    private LeitorPortaSerial() {

    }

    public static void builderLeitor(Consumer<InputStream> consumer) throws LeitorPortaSerialException {

        try (final var leitor = new LeitorPortaSerial()) {

            leitor.setPrimeiraPortaSerial()
                    .aplicarConfiguracaoPadrao()
                    .abrirPorta()
                    .consumeStreamPortaSerial(consumer);

        } catch (LeitorPortaSerialException e) {
            throw e;
        } catch (Exception e) {
            throw new LeitorPortaSerialException(e.getMessage());
        }

    }

    private List<SerialPort> getPortasSeriais() {

        final var portasDisponiveis = SerialPort.getCommPorts();

        return portasDisponiveis != null
                ? List.of(portasDisponiveis)
                : List.of();

    }

    private LeitorPortaSerial setPrimeiraPortaSerial() throws LeitorPortaSerialException {

        final var portasSeriais = getPortasSeriais();

        if (portasSeriais.isEmpty()) {
            throw new NenhumaPortaSerialDisponivelException();
        }

        this.porta = portasSeriais.getFirst();

        return this;

    }

    private LeitorPortaSerial aplicarConfiguracaoPadrao() throws LeitorPortaSerialException {

        if (this.porta == null) {
            throw new PortaSerialNaoSelecionadaException();
        }

        this.porta.setBaudRate(9600); // 9600 baud

        this.porta.setNumDataBits(8); // 8 bits de dados

        this.porta.setNumStopBits(1); // 1 bit de parada

        this.porta.setParity(0); // sem paridade

        this.porta.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
                5000,
                0);

        return this;

    }

    private LeitorPortaSerial abrirPorta() throws LeitorPortaSerialException {

        if (this.porta == null) {
            throw new PortaSerialNaoSelecionadaException();
        }

        if (!this.porta.openPort()) {
            throw new PortaSerialNaoAbertaException(this.porta.getSystemPortName());
        }

        return this;

    }

    private void consumeStreamPortaSerial(Consumer<InputStream> consumer) {

        consumer.accept(this.porta.getInputStream());

    }

    @Override
    public void close() throws Exception {
        fecharPorta();
    }

    public void fecharPorta() {

        if (porta != null) {
            porta.closePort();
        }

    }

}
