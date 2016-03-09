package machine.printing.internal;

import machine.printing.Printable;

import java.io.PrintStream;

public class PrintLn implements Printable {
    private String message;

    public PrintLn(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintStream stream) {
        stream.println(message);
    }
}
