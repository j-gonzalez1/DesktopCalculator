package calculator;

import java.util.function.BinaryOperator;

public enum Operator {
    DIVIDE("\u00F7", (x, y) -> x / y),
    MULTIPLY("\u00D7", (x, y) -> x * y),
    SUBTRACT("-", (x, y) -> x - y),
    ADD("+", (x, y) -> x + y);

    private final String symbol;
    private final BinaryOperator<Double> equation;


    Operator(String symbol, BinaryOperator<Double> equation) {
        this.symbol = symbol;
        this.equation = equation;
    }

    public double compute(double alpha, double beta) {
        return equation.apply(alpha, beta);
    }

    @Override
    public String toString() {
        return symbol;
    }
}