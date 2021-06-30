// Author: Jose Luis Gonzalez
// Date: 6/5/2021
// Assignment: 14_CalculatorProgram_Part2
// Class: Java CS/IS 139-1521

package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Calculator extends Application {
    static Operator currentOperator;
    static boolean operatorSelected;
    static boolean calcDisplayDisplayed;
    
    private TextField temp = new TextField();
    private TextField calcDisplay = new TextField();
    private Button backBtn = new Button("\u232B");
    private Button[] numberButtons = new Button[10];
    private Button decimalButton = new Button(".");
    private Button equalButton = new Button("=");
    
    @Override
    public void start(Stage primaryStage) {
    	GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(20, 20, 20, 20));

		calcDisplay.setAlignment(Pos.CENTER_RIGHT);
		calcDisplay.setEditable(false);
		calcDisplay.setLayoutX(10);
		calcDisplay.setLayoutY(10);
		calcDisplay.setFont(Font.font("Courier",FontWeight.BOLD,FontPosture.REGULAR,24));
		calcDisplay.setPrefSize(150, 50);
		calcDisplay.setStyle("-fx-border-color: blue;");
        gridPane.add(calcDisplay, 0, 0, 3, 1);
        
        backBtn.setPrefSize(50, 50);
        backBtn.setStyle("-fx-text-fill: #99cbff");
        backBtn.setOnAction(e -> {
            String currentText = calcDisplay.getText();
            if (!currentText.isEmpty()){
                calcDisplay.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
        gridPane.add(backBtn, 3, 0, 1, 1);

        
        for (int i = 3, target = 1; i >= 1; i--) {
            for (int j = 0; j <= 2; j++) {
                String number = Integer.toString(target);

                numberButtons[target] = new Button(number);
                numberButtons[target].setPrefSize(50, 50);
                numberButtons[target].setOnAction(e -> {
                    if (calcDisplayDisplayed) {
                        calcDisplay.setText(number);
                        calcDisplayDisplayed = false;
                    } else {
                        calcDisplay.appendText(number);
                    }

                    operatorSelected = false;
                });
                gridPane.add(numberButtons[target++], j, i);
            }   
        }

        numberButtons[0] = new Button("0");
        numberButtons[0].setPrefSize(50, 50);
        numberButtons[0].setOnAction(e -> {
            if (!calcDisplay.getText().isEmpty() && !calcDisplayDisplayed) {
                calcDisplay.appendText("0");
                operatorSelected = false;
            }
        });
        gridPane.add(numberButtons[0], 0, 4);

        decimalButton.setPrefSize(50, 50);
        decimalButton.setOnAction(e -> {
            if (calcDisplay.getText().indexOf('.') == -1) {
                calcDisplay.appendText(".");
            }
        });
        gridPane.add(decimalButton, 1, 4);
        
        
        equalButton.setPrefSize(50, 50);
        equalButton.setOnAction(e -> {
        	if (!temp.getText().isEmpty()) {
                calcDisplay.setText(
                    calculate(currentOperator, calcDisplay, temp)
                );
                calcDisplayDisplayed = true;
                operatorSelected = false;
                temp.clear();
            }
        });
        gridPane.add(equalButton, 2, 4);
        equalButton.setDefaultButton(true);

        for (Operator op : Operator.values()) {
            String symbol = op.toString();
            Button button = new Button(symbol);
            button.setPrefSize(50, 50);
            button.setStyle("-fx-text-fill: #99cbff");
            button.setOnAction(e -> {
                if (temp.getText().isEmpty()) {
                    temp.setText(calcDisplay.getText().isEmpty() ? "0" : acquireValue(calcDisplay.getText()));
                    temp.appendText(" " + symbol);
                    currentOperator = op;
                    calcDisplayDisplayed = true;
                    operatorSelected = true;
                } else if (operatorSelected) {
                    currentOperator = op;
                    int end = temp.getText().length();
                    temp.replaceText(end - 1, end,  symbol);
                } else {
                    temp.setText(calculate(currentOperator, calcDisplay, temp) + " " + symbol);
                    calcDisplay.clear();
                    currentOperator = op;
                    calcDisplayDisplayed = true;
                    operatorSelected = true;
                }
            });
            gridPane.addColumn(3, button);
        }
        
        BackgroundFill background_fill = new BackgroundFill(Color.web("#2d3033"), null, null);
	    Background background = new Background(background_fill);
	    gridPane.setBackground(background);
        Scene scene = new Scene(gridPane,240,280);
        scene.getStylesheets().add(Calculator.class.getResource("stylesheet.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Calculator");
        primaryStage.show();
        primaryStage.sizeToScene();
    }

    private static String calculate(Operator op, TextField main, TextField temp) {
        double val1 = Double.parseDouble(temp.getText().replaceAll("[^\\.0-9]", ""));
        double val2 = Double.parseDouble(main.getText());

        if (val2 == 0 && op == Operator.DIVIDE) {
            return "Cannot divide by 0";
        }

        double calcDisplay = op.compute(val1, val2);
        return toCalculatorString(calcDisplay);
    }

    private static String acquireValue(String val) {
        double calcDisplay = Double.parseDouble(val);
        return toCalculatorString(calcDisplay);
    }

    private static String removeDecimalTrailingZeroes(String s) {
        return s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    private static String toCalculatorString(double input) {
        return input == (int)input ? 
            Integer.toString((int)input) : removeDecimalTrailingZeroes(String.format("%.6f", input));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}