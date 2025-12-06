import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
	/**
	 * Main Calculator Class - NumCruncher Application
	 * Developed by Brendan LeGrand for CSCI 3300 - Term Project.
	 *
	 * This class serves as the main entry point for the NumCruncher calculator application.
	 * It integrates the user interface components and logic, and sets up the primary JFrame window.
	 * Features include basic arithmetic operations, a user-friendly interface, and helpful tooltips.
	 * 
	 * Usage:
	 * - Instantiate this class to start the application.
	 * - Use the graphical interface for performing calculations.
	 * - Refer to the FAQ in the help center for detailed operating instructions.
	 *
	 */
	private static final long serialVersionUID = 4519143440307608770L;    
    private final CalculatorUI UI;
    private final CalculatorLogic logic;
    
    public Calculator() {
        UI = new CalculatorUI();
        logic = new CalculatorLogic(UI, this);
        UI.initializeUI(this);
        String welcomeMessage = """
                Welcome to NumCruncher!
                Please read the FAQ in the help center for operating instructions
                """;
        JOptionPane.showMessageDialog(null, welcomeMessage, "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }
        
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); // Identify the source of the action event
		
		//BUTTON HANDLING
		if (source instanceof JButton button) {
			String buttonText = button.getText();
			
			// Handle actions for numeric buttons (0-9)
			if (isNumeric(buttonText)) {
				logic.handleNumericButton(buttonText);			
			}

			// Handle actions for basic arithmetic operator buttons (+, -, ×, ÷)
			else if ("+-×÷".contains(buttonText)) {
				logic.handleOperatorButtons(buttonText);
			}

			// Handle the action for the equals button (=)
			else if ("=".equals(buttonText)) {
				logic.handleEqualsButton(buttonText);
			}
			
			// Handle the action for the sign toggle button
			else if (source == UI.getSignToggleButton()) {
				logic.handleToggleSignButton(buttonText);
			}

			// Handle the action for trigonometric function buttons (sin, cos, tan)
			else if (source == UI.getSinButton() || source == UI.getCosButton() || source == UI.getTanButton()) {
				logic.handleTrigButtons(source);
			}

			// Handle the action for the decimal button
			else if (source == UI.getDecimalButton()) {
				logic.handleDecimalButton(source);
			}

			// Handle the action for the backspace button
			else if (source == UI.getBackspaceButton()) {
				logic.handleBackspaceButton(source);
			}

			// Handle the action for the power of button (xⁿ)
			else if (source == UI.getPowerOfButton()) {
				logic.handlePowerOfButton(source);
			}

			// Handle the action for the clear button (CE)
			else if (source == UI.getCeButton()) {
				logic.handleClearButton(source);
			}

			// Handle the action for Euler's button (e)
			else if (source == UI.getEulerButton()) {
				logic.handleEulersButton(source);
			}

			// Handle the action for the PI button (π)
			else if (source == UI.getPIButton()) {
				logic.handlePIButton(source);
			}

			// Handle the action for the power of ten button (10^x)
			else if (source == UI.getPowerOfTenButton()) {
				logic.handlePowerOfTenButton(source);
			}
			
			//Handle the action for the square root button
			else if (source == UI.getSqrRootButton()) {
				logic.handleSqrRootButton(source);
			}
			
			//Handle the action for the log base 10 button
			else if (source == UI.getLogBaseTenButton()) {
				logic.handleLogBaseTenButton(source);			
			}
			
			//Handle the action for the log base e button
			else if (source == UI.getLogBaseEButton()) {
				logic.handleLogBaseEButton(source);				
			}
			
			//Handle the action for the xPowerOf2Button button
			else if (source == UI.getxPowerOf2Button()) {
				logic.handleXPowerOf2Button(source);
			}
			
			//Handle the action for the secant function button
			else if (source == UI.getSecButton()) {
				logic.handleSecButton(source);
			}
			
			//Handle the action for the cosecant function button
			else if (source == UI.getCscButton()) {
				logic.handleCscButton(source);
			}
			
			//Handle the action for the cotangent function button
			else if (source == UI.getCotButton()) {
				logic.handleCotButton(source);
			}
			
			// Handle the action for the modulo button
			else if (source == UI.getModuloButton()) {
				logic.handleModuloButton(source);
			}
						
			//Handle the action for the secondary function button
			else if (source == UI.getSecondaryFunctionButton()) {
				logic.handleSecondaryFunctionButton(source);
			}
		}		
	}
	
	private boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	public void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Calculator::new);
	}
}
