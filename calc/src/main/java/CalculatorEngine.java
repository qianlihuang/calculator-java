import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import org.graalvm.polyglot.*;

public class CalculatorEngine {
    private FileWriter fileWriter;
    private Context context;
    private BigDecimal lastResult;

    public CalculatorEngine() {
        context = Context.newBuilder().allowAllAccess(true).build();
        lastResult = BigDecimal.ZERO;
        try {
            fileWriter = new FileWriter("calculations.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCalculation(String expression, double result, JTextArea historyTextArea) {
        try {
            fileWriter.write(expression + " = " + result + "\n");
            fileWriter.flush();
            historyTextArea.append(expression + " = " + result + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFileWriter() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double evaluate(String expression) throws CalculationException {
        expression = expression.replace("×", "*");
        expression = expression.replace("÷", "/");
        expression = expression.replace("sin", "Math.sin"); // Radian mode
        expression = expression.replace("cos", "Math.cos"); 
        expression = expression.replace("tan", "Math.tan"); 
        expression = expression.replace("π", "Math.PI");
        expression = expression.replace("e", "Math.E");
        expression = expression.replace("√", "Math.sqrt");
        expression = expression.replace("ln", "Math.log");
        //expression = expression.replace("lg", "Math.log10");
        expression = expression.replace("abs", "Math.abs");
        expression = expression.replace("^", "**"); 
        expression = expression.replaceAll("\\((\\d+)\\)!", "factorial($1)"); //(2)! -> factorial(2)
        expression = expression.replaceAll("(\\d+)!", "factorial($1)"); //2! -> factorial(2)
        expression = expression.replace("mod", "%");
        expression = expression.replace("ans", lastResult.toString());
        try {
            context.eval("js", "function factorial(n) { if (n === 0) { return 1; } else { return n * factorial(n - 1); } }");
            Value result = context.eval("js", expression);
            
            if (result.isNumber()) {
                lastResult = BigDecimal.valueOf(result.asDouble());
                return result.asDouble();
            } else {
                throw new CalculationException("Unsupported result type: " + result.getClass().getSimpleName());
            }
        } catch (PolyglotException e) {
            throw new CalculationException("Error evaluating expression: " + e.getMessage());
        }
    }

    public void evaluateExpression(JTextField inputField, JTextField resultField, JFrame mainFrame, JTextArea historyTextArea) {
        String expression = inputField.getText();
        try {
            double result = evaluate(expression);
            if (Double.isInfinite(result) || Double.isNaN(result)) {
                resultField.setText("");
                JOptionPane.showMessageDialog(mainFrame, "Error", "Calculation Error", JOptionPane.ERROR_MESSAGE);
            } else {                
                String formattedResult;
                if (Math.abs(result) >= 1e6 || Math.abs(result) < 1e-6) {
                    formattedResult = String.format("%.6e", result);
                    formattedResult = formattedResult.replaceAll("0*(\\d+)", "$1");
                    formattedResult = formattedResult.replaceAll("(\\.\\d*?)0+e", "$1e").replaceAll("e", "E");
                    formattedResult = formattedResult.replaceAll("(\\.\\d*?)E", "$10E");
                    //System.out.println(formattedResult);
                    if(formattedResult.equals("1.2246470E-16") || formattedResult.equals("-1.2246470E-16") || formattedResult.equals("6.1232340E-17") || formattedResult.equals("-6.1232340E-17")) formattedResult = "0.0";
                    if(formattedResult.equals("1.6331240E+16") || formattedResult.equals("-1.6331240E+16") ) formattedResult = "∞";
                } else {
                    BigDecimal roundedResult = BigDecimal.valueOf(result).setScale(10, BigDecimal.ROUND_HALF_UP);
                    formattedResult = roundedResult.stripTrailingZeros().toPlainString();
                }
                formattedResult = formattedResult.replace("e", "E");
                resultField.setText("=" + formattedResult);
                saveCalculation(expression, Double.parseDouble(formattedResult), historyTextArea);
            }
        } catch (CalculationException e) {
            JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addToInput(String text, JTextField inputField) {
        inputField.setText(inputField.getText() + text);
    }

    public void clearInput(JTextField inputField) {
        inputField.setText("");
    }

    public void clearLastInput(JTextField inputField) {
        String currentText = inputField.getText();
        if (!currentText.isEmpty()) {
            inputField.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    
}

