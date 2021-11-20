package eu.andermann.festivalappdemo.exceptions;

public class WrongMetallicaPlayTimeException extends Throwable {

    public WrongMetallicaPlayTimeException() {
        super("Metallica only plays in the dark.");
    }
}
