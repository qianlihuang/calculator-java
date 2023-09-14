import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;


public class CalculatorApp {
    private JFrame mainFrame;
    private JTextField inputField;
    private JTextField resultField;
    private JTextArea historyTextArea;
    private JButton buttonAllClear;
    private JButton buttonBackspace;
    private JButton buttonEquals;
    private JButton buttonOpenParenthesis;
    private JButton buttonCloseParenthesis;
    private JButton buttonSin;
    private JButton buttonCos;
    private JButton buttonTan;
    private JButton buttonPi;
    private JButton buttonExponent;
    private JButton buttonFactorial;
    // private JButton buttonPercentage;
    private JButton buttonLn;
    private JButton buttonE;
    private JButton buttonSquareRoot;
    //private JButton buttonLg;
    private JButton buttonAbsoluteValue;
    private JButton buttonMod;
    // private JButton buttonLeft;
    // private JButton buttonRight;
    // private JButton buttonBinaryDecimal;
    // public boolean isBinaryMode = false;
    private JButton buttonAns;
    private JPanel buttonPanel;
    private CalculatorEngine calculatorEngine;

    private Clip buttonClickSound;
    private Clip equalsClickSound;
    

    public CalculatorApp(CalculatorEngine calculatorEngine) {
        this.calculatorEngine = calculatorEngine;
        try {
            buttonClickSound = loadAudioClip("button_click.wav");
            equalsClickSound = loadAudioClip("equals_click.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        initialize();
    }

    

    private Clip loadAudioClip(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(path);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }



    public JTextField getInputField() {
        return inputField;
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        mainFrame = new JFrame("Dyl's Calculator");
        mainFrame.setBounds(200, 200, 800, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.DARK_GRAY);
        mainFrame.getContentPane().setLayout(new BorderLayout());

        historyTextArea = new JTextArea();
        historyTextArea.setEditable(false);
        historyTextArea.setBackground(Color.DARK_GRAY);
        historyTextArea.setForeground(Color.WHITE);
        Font historyFont = new Font("Arial", Font.PLAIN, 14);
        historyTextArea.setFont(historyFont);
        JScrollPane historyScrollPane = new JScrollPane(historyTextArea);
        historyScrollPane.setPreferredSize(new Dimension(200, mainFrame.getHeight()));
        mainFrame.getContentPane().add(historyScrollPane, BorderLayout.EAST);
        JPanel inputPanel = new JPanel(new BorderLayout());

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.WHITE);
        Font font = new Font("Arial", Font.PLAIN, 66);
        inputField.setFont(font);
        inputPanel.add(inputField, BorderLayout.NORTH);

        resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        resultField.setBackground(Color.DARK_GRAY);
        resultField.setForeground(Color.WHITE);
        resultField.setFont(font);
        inputPanel.add(resultField, BorderLayout.CENTER);
        mainFrame.getContentPane().add(inputPanel, BorderLayout.NORTH);


        buttonPanel = new JPanel(new GridLayout(8, 8, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton("7");
        addButton("8");
        addButton("9");
        addButton("/");

        addButton("4");
        addButton("5");
        addButton("6");
        addButton("*");

        addButton("1");
        addButton("2");
        addButton("3");
        addButton("-");

        addButton("0");
        addButton(".");
        buttonEquals = addButton("=");
        buttonEquals.setBackground(Color.BLUE);
        buttonEquals.setOpaque(true);

        addButton("+");

        buttonAllClear = addButton("AC");
        buttonAllClear.setBackground(Color.RED);
        buttonAllClear.addActionListener(e -> {
            calculatorEngine.clearInput(inputField);
            resultField.setText("");
        });

        buttonBackspace = addButton("←");
        buttonBackspace.setBackground(Color.YELLOW);
        buttonBackspace.addActionListener(e -> calculatorEngine.clearLastInput(inputField));

        buttonOpenParenthesis = addButton("(");
        buttonCloseParenthesis = addButton(")");

        buttonSin = addButton("sin");
        buttonSin.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "sin(";
            inputField.setText(expression);
        });

        buttonCos = addButton("cos");
        buttonCos.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "cos(";
            inputField.setText(expression);
        });

        buttonTan = addButton("tan");
        buttonTan.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "tan(";
            inputField.setText(expression);
        });

        buttonPi = addButton("π");
        buttonPi.addActionListener(e -> calculatorEngine.addToInput("π", inputField)); 

        buttonExponent = addButton("^");
        buttonExponent.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "^";
            inputField.setText(expression);
        });

        buttonFactorial = addButton("!");
        buttonFactorial.addActionListener(e -> {
            String expression = inputField.getText() + "!";
            inputField.setText(expression);
        });

        // buttonPercentage = addButton("%");
        // buttonPercentage.addActionListener(e -> {
        //     String expression = inputField.getText() + "%";
        //     inputField.setText(expression);
        // });

        buttonLn = addButton("ln");
        buttonLn.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "ln(";
            inputField.setText(expression);
        });

        buttonE = addButton("e");
        buttonE.addActionListener(e -> calculatorEngine.addToInput("e", inputField));

        buttonSquareRoot = addButton("√");
        buttonSquareRoot.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "√(";
            inputField.setText(expression);
        });

        // buttonLg = addButton("lg");
        // buttonLg.addActionListener(e -> {
        //     String expression = inputField.getText();
        //     expression += "lg(";
        //     inputField.setText(expression);
        // });

        buttonAbsoluteValue = addButton("|x|");
        buttonAbsoluteValue.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "abs(";
            inputField.setText(expression);
        });

        buttonMod = addButton("mod");
        buttonMod.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "mod(";
            inputField.setText(expression);
        });

        // buttonLeft = addButton("left"); // Add left button
        // buttonLeft.setBackground(Color.YELLOW);
        // buttonLeft.addActionListener(e -> moveCursorLeft(inputField));

        // buttonRight = addButton("right"); // Add right button
        // buttonRight.setBackground(Color.YELLOW);
        // buttonRight.addActionListener(e -> moveCursorRight(inputField));   

        buttonAns = addButton("ans"); // Added button
        buttonAns.addActionListener(e -> {
            String expression = inputField.getText();
            expression += "ans";
            inputField.setText(expression);
        });

        buttonEquals.addActionListener(e -> calculatorEngine.evaluateExpression(inputField, resultField, mainFrame, historyTextArea));

        mainFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);

        mainFrame.setIconImage(new ImageIcon("calculator_icon.jpg").getImage());

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isDigit(keyChar) || keyChar == '.' || "+-*/".indexOf(keyChar) != -1) {
                    String keyText = Character.toString(keyChar);
                    calculatorEngine.addToInput(keyText, inputField);
                } else if (keyChar == '=' || keyChar == '\n') {
                    calculatorEngine.evaluateExpression(inputField, resultField, mainFrame, historyTextArea);
                } else if (keyChar == '\b') {
                    calculatorEngine.clearLastInput(inputField);
                }
            }
        });

        mainFrame.setFocusable(true);
        mainFrame.setVisible(true);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                calculatorEngine.closeFileWriter();
            }
        });
    }

    private JButton addButton(String text) {
        JButton button = new JButton(text);
        if (text.equals("/")) {
            button.setText("÷");
        } else if (text.equals("*")) {
            button.setText("×");
        }
        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 28));
        button.setToolTipText(getButtonToolTipText(text));
        button.addActionListener(e -> handleButtonClick(text));
        buttonPanel.add(button);
        return button;
    }

    private String getButtonToolTipText(String buttonText) {
        switch (buttonText) {
            case "AC":
                return "Clear All";
            case "←":
                return "Backspace";
            case "=":
                return "Calculate";
            case "sin":
                return "Sine Function";
            case "cos":
                return "Cosine Function";
            case "tan":
                return "Tangent Function";
            case "π":
                return "Pi";
            // case "^":
            //     return "Exponent";
            case "!":
            //     return "Factorial";
            // case "%":
            //     return "Percentage";
            case "ln":
                return "Natural Logarithm";
            case "e":
                return "Euler's Number";
            case "√":
                return "Square Root";
            // case "lg":
            //     return "Logarithm";
            case "|x|":
                return "Absolute Value";
            case "mod":
                return "Modulo";
            // case "left":
            //     return "Move Cursor Left";
            // case "right":
            //     return "Move Cursor Right";
            // case "Bin/Dec":
            //     return "Binary/Decimal Mode";
            case "ans":
                return "Previous Answer";
            default:
                return buttonText;
        }
    }

    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "=":
                equalsClickSound.setFramePosition(0);
                equalsClickSound.start();
                break;
            case "AC":
                calculatorEngine.clearInput(inputField);
                resultField.setText("");
                break;
            case "←":
                break;
            case "(":
            case ")":
                calculatorEngine.addToInput(buttonText, inputField);
                break;
            case "sin":
                String expression = inputField.getText();
                expression += "sin(";
                break;
            case "cos":
                expression = inputField.getText();
                expression += "cos(";
                break;
            case "tan":
                expression = inputField.getText();
                expression += "tan(";
                break;
            case "π":
                break;
            case "^":
                expression = inputField.getText();
                expression += "^";
                break;
            case "!":
                expression = inputField.getText() + "!";
                break;
            // case "%":
            //     expression = inputField.getText() + "%";
            //     //inputField.setText(expression);
            //     break;
            case "ln":
                expression = inputField.getText();
                expression += "ln(";
                break;
            case "e":
                //calculatorEngine.addToInput("e", inputField);
                break;
            case "√":
                expression = inputField.getText();
                expression += "√(";
                break;
            // case "lg":
            //     expression = inputField.getText();
            //     expression += "lg(";
            //     //inputField.setText(expression);
            //     break;
            case "|x|":
                expression = inputField.getText();
                expression += "abs(";
                break;
            case "/":
                calculatorEngine.addToInput("÷", inputField);
                break;
            case "*":
                calculatorEngine.addToInput("×", inputField);
                break;
            case "mod":
                expression = inputField.getText();
                expression += "mod(";
                break;
            // case "left":
            //     moveCursorLeft(inputField);
            //     break;
            // case "right":
            //     moveCursorRight(inputField);
            //     break;
            // case "Bin/Dec":
            //     break;
            case "ans":
                break;
            default:
                buttonClickSound.setFramePosition(0);
                buttonClickSound.start();
                calculatorEngine.addToInput(buttonText, inputField);
                break;
        }
    }

    // // Method to move the cursor to the left
    // private void moveCursorLeft(JTextField textField) {
    //     int position = textField.getCaretPosition();
    //     if (position > 0) {
    //         textField.setCaretPosition(position - 1);
    //     }
    // }

    // // Method to move the cursor to the right
    // private void moveCursorRight(JTextField textField) {
    //     int position = textField.getCaretPosition();
    //     int length = textField.getText().length();
    //     if (position < length) {
    //         textField.setCaretPosition(position + 1);
    //     }
    // }
}
