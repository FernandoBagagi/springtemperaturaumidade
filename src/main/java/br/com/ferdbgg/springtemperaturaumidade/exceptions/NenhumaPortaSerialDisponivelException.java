package br.com.ferdbgg.springtemperaturaumidade.exceptions;

public class NenhumaPortaSerialDisponivelException extends LeitorPortaSerialException {

    public NenhumaPortaSerialDisponivelException () {
        super("Nenhuma porta serial física ou emulada foi encontrada no sistema.");
    }

}
