import java.awt.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ThemeManager {
    private final Color bgColor = Color.decode("#f3f3f3");
    private final Color equalBgColor = Color.decode("#6ddcdb");
    private final Color buttonBgColor = Color.decode("#f9f9f9");
    private final Color darkBgColor = Color.decode("#202020");
    private final Color darkEqualBgColor = Color.decode("#d8b597");
    private final Color darkNumberBgColor = Color.decode("#3b3b3b");
    private final Color darkButtonBgColor = Color.decode("#323232");
    private final Color darkTextColor = Color.decode("#e1e1e1");

    private boolean darkModeEnabled = false;

    public Color getButtonBackgroundColor() {
        return darkModeEnabled ? darkNumberBgColor : buttonBgColor;
    }

    public Color getEqualBackgroundColor() {
        return darkModeEnabled ? darkEqualBgColor : equalBgColor;
    }

    public Color getBackgroundColor() {
        return darkModeEnabled ? darkBgColor : bgColor;
    }

    public void applyDarkMode(JPanel mainPanel, JTextField resultField, JButton[] numButtons, JButton[] operationButtons,
                              List<JButton> numberLikeButtons, List<JButton> functionButtons, JButton calculateButton) {
        darkModeEnabled = true;

        mainPanel.setBackground(darkBgColor);
        resultField.setBackground(darkBgColor);
        resultField.setForeground(darkTextColor);

        setButtonStyles(Arrays.asList(numButtons), darkNumberBgColor, darkTextColor);
        setButtonStyles(numberLikeButtons, darkNumberBgColor, darkTextColor);
        setButtonStyles(Arrays.asList(operationButtons), darkButtonBgColor, darkTextColor);
        setButtonStyles(functionButtons, darkButtonBgColor, darkTextColor);

        calculateButton.setBackground(darkEqualBgColor);
        calculateButton.setForeground(darkTextColor);
    }

    public void applyLightMode(JPanel mainPanel, JTextField resultField, JButton[] numButtons, JButton[] operationButtons,
                               List<JButton> numberLikeButtons, List<JButton> functionButtons, JButton calculateButton) {
        darkModeEnabled = false;

        mainPanel.setBackground(bgColor);
        resultField.setBackground(bgColor);
        resultField.setForeground(null);

        setButtonStyles(Arrays.asList(numButtons), buttonBgColor, null);
        setButtonStyles(numberLikeButtons, buttonBgColor, null);
        setButtonStyles(Arrays.asList(operationButtons), buttonBgColor, null);
        setButtonStyles(functionButtons, buttonBgColor, null);

        calculateButton.setBackground(equalBgColor);
        calculateButton.setForeground(null);
    }

    private void setButtonStyles(List<JButton> buttons, Color backgroundColor, Color foregroundColor) {
        for (JButton button : buttons) {
            button.setBackground(backgroundColor);
            button.setForeground(foregroundColor);
        }
    }
}
