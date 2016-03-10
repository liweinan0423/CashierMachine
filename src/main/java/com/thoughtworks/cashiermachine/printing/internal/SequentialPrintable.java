package com.thoughtworks.cashiermachine.printing.internal;

import com.thoughtworks.cashiermachine.printing.Printable;

import java.io.PrintStream;
import java.util.List;

public class SequentialPrintable implements Printable {
    private List<Printable> printables;

    public SequentialPrintable(List<Printable> printables) {
        this.printables = printables;
    }

    @Override
    public void print(PrintStream stream) {
        printables.forEach(p -> p.print(stream));
    }
}
