import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorEngine calculatorEngine = new CalculatorEngine();
            new CalculatorApp(calculatorEngine);
        });
    }
}

