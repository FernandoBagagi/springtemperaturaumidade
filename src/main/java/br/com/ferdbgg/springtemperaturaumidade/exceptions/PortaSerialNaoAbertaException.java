package br.com.ferdbgg.springtemperaturaumidade.exceptions;

public class PortaSerialNaoAbertaException extends LeitorPortaSerialException {

    public PortaSerialNaoAbertaException(String arg0) {
        super("Falha ao abrir a porta serial: " + arg0);
    }

}
