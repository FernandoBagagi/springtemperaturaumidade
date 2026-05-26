package br.com.ferdbgg.springtemperaturaumidade.exceptions;

public class PortaSerialNaoSelecionadaException extends LeitorPortaSerialException {

    public PortaSerialNaoSelecionadaException() {
        super("Porta não selecionada antes de configurar.");
    }

}
