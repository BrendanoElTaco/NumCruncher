import java.awt.*;
import javax.swing.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class CalculatorUI extends JFrame {
	/**
	 * CalculatorUI Class - User Interface for NumCruncher Application
	 * Developed by Brendan LeGrand for CSCI 3300 - Term Project.
	 *
	 * This class is responsible for creating and managing the user interface of the NumCruncher calculator application.
	 * It includes elements such as buttons for digits and operations, a display field for results, and additional
	 * functionality like trigonometric operations and a sign toggle.
	 *
	 * Features:
	 * - Numeric and operation buttons for performing calculations.
	 * - A result field to display current inputs and calculation results.
	 * - Additional features like trigonometric functions and toggle buttons.
	 *
	 * Key Bindings:
	 * - Numeric keys (0-9) for entering digits.
	 * - Enter key for equals operation.
	 * - Backspace key for deleting a single character.
	 * - Escape key for clearing the current input.
	 * - Plus, minus, multiply, and divide keys for respective operations.
	 *
	 * Usage:
	 * - Instantiate this class in the Calculator class to create the UI.
	 * - Interacts with CalculatorLogic to perform calculations based on user inputs and button clicks.
	 */

	private static final long serialVersionUID = 5294493750822072072L;
	public JTextField resultField;
    public JButton[] numButtons;
    public JButton[] operationButtons;
    public JButton calculateButton;
    public JButton signToggleButton;
    public JButton sinButton, cosButton, tanButton;
    public JButton decimalButton;
    public JButton powerOfButton;
    public JButton backspaceButton;
    public JButton ceButton;
    public JButton eulerButton;
    public JButton PIButton;
    public JButton powerOfTenButton;
    public JButton sqrRootButton;
    public JButton logBaseTenButton;
    public JButton logBaseEButton;
    public JButton secondaryFunctionButton;
    public JButton xPowerOf2Button;
    public JButton secButton;
    public JButton cscButton;
    public JButton cotButton;
    public JButton moduloButton;
    
    private JPanel mainPanel;
    
    //Fonts
    private final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 20);
    //Unicode PI is small, so big font
    private final Font PI_BUTTON_FONT = new Font("SansSerif", Font.BOLD, 30);

    // Flag to manage the state of key listeners
    private boolean areKeyListenersEnabled = true;

        private Calculator calculator;
    private SoundManager soundManager;
    private ThemeManager themeManager;
    private JButton muteToggleButton;
    private JMenuBar menuBar;
    private JPanel statusBar;
    private JButton volumeButton;
    private JSlider volumeSlider;
    private JPanel volumePopupPanel;
    private JLabel readyStatusLabel;

        //Initialize UI
    public void initializeUI(Calculator calc) {
        this.calculator = calc;
        this.themeManager = new ThemeManager();
        this.soundManager = new SoundManager(new String[]{"sounds/key1.wav", "sounds/key2.wav", "sounds/key3.wav"});
        setTitle("NumCruncher - A Scientific Calculator");
                // Ensuring the application exits when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setMinimumSize(new Dimension(450, 550));
        setLocationRelativeTo(null);       

        initializeResultField();
        initializeNumberButtons();
        initializeOperationButtons();
        initializeFunctionButtons();
        initializeMenuBar();
        
        //Make key presses work
        registerGlobalKeyEventListener();

        // Create the main panel to hold the result field and button panel
        mainPanel = new JPanel();
                // Setting the layout of the CalculatorUI to BorderLayout for organized component arrangement
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(resultField, BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);
        mainPanel.setBackground(themeManager.getBackgroundColor());
        
        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(createStatusBarWithVolumePopup(), BorderLayout.SOUTH);
        applyThemeToChrome();
        
        // Make the frame visible
        setVisible(true);
    }
    
    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        menuBar.add(createCustomizationMenu());
        menuBar.add(createHelpMenu());
        menuBar.add(createAboutMenu());

        setJMenuBar(menuBar);
    }

    private JMenu createCustomizationMenu() {
        JMenu customizationMenu = new JMenu("Customization");

        // Radio button menu items for light and dark mode
        JRadioButtonMenuItem lightMode = new JRadioButtonMenuItem("Light Mode");
        JRadioButtonMenuItem darkMode = new JRadioButtonMenuItem("Dark Mode");

        lightMode.setSelected(true); // Set light mode as default

        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(lightMode);
        group.add(darkMode);

        // Add radio buttons to the menu
        customizationMenu.add(lightMode);
        customizationMenu.add(darkMode);

        // Action listeners for mode changes
        lightMode.addActionListener(e -> setLightMode());
        darkMode.addActionListener(e -> setDarkMode());

        return customizationMenu;
    }

    
    private JPanel createStatusBarWithVolumePopup() {
        statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, themeManager.getBorderColor()));

        readyStatusLabel = new JLabel("Ready");
        readyStatusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        volumeButton = new JButton("Volume");
        volumeButton.setFocusable(false);
        volumeButton.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, soundManager.getCurrentVolume());
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setSnapToTicks(true);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.addChangeListener(e -> soundManager.updateVolume(volumeSlider.getValue()));

        JPopupMenu volumePopup = new JPopupMenu();
        volumePopupPanel = new JPanel(new BorderLayout());
        volumePopupPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        volumePopupPanel.add(new JLabel("Volume"), BorderLayout.NORTH);
        volumePopupPanel.add(volumeSlider, BorderLayout.CENTER);
        volumePopup.add(volumePopupPanel);

        volumeButton.addActionListener(e -> {
            volumePopup.show(volumeButton, 0, volumeButton.getHeight());
        });

        muteToggleButton = new JButton();
        muteToggleButton.setFocusable(false);
        muteToggleButton.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        updateMuteButtonIcon();
        muteToggleButton.addActionListener(e -> {
            if (soundManager.isMuted()) {
                soundManager.unmuteSound();
            } else {
                soundManager.muteSound();
            }
            updateMuteButtonIcon();
        });

        statusBar.add(readyStatusLabel, BorderLayout.WEST);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(muteToggleButton);
        rightPanel.add(volumeButton);
        statusBar.add(rightPanel, BorderLayout.EAST);
        return statusBar;
    }

    private void updateMuteButtonIcon() {
        if (muteToggleButton == null) {
            return;
        }
        if (soundManager.isMuted()) {
            muteToggleButton.setText("🔇");
            muteToggleButton.setToolTipText("Unmute");
        } else {
            muteToggleButton.setText("🔊");
            muteToggleButton.setToolTipText("Mute");
        }
    }

    private void applyThemeToChrome() {
        Color chromeBg = themeManager.getChromeBackgroundColor();
        Color textColor = themeManager.getTextColor();
        Color borderColor = themeManager.getBorderColor();

        if (menuBar != null) {
            menuBar.setBackground(chromeBg);
            menuBar.setForeground(textColor);
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu != null) {
                    menu.setBackground(chromeBg);
                    menu.setForeground(textColor);
                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem item = menu.getItem(j);
                        if (item != null) {
                            item.setBackground(chromeBg);
                            item.setForeground(textColor);
                        }
                    }
                }
            }
        }

        if (statusBar != null) {
            statusBar.setBackground(chromeBg);
            statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));
        }
        if (readyStatusLabel != null) {
            readyStatusLabel.setForeground(textColor);
        }
        if (volumeButton != null) {
            volumeButton.setBackground(chromeBg);
            volumeButton.setForeground(textColor);
        }
        if (muteToggleButton != null) {
            muteToggleButton.setBackground(chromeBg);
            muteToggleButton.setForeground(textColor);
        }
        if (volumePopupPanel != null) {
            volumePopupPanel.setBackground(chromeBg);
            for (Component component : volumePopupPanel.getComponents()) {
                component.setBackground(chromeBg);
                component.setForeground(textColor);
            }
        }
        if (volumeSlider != null) {
            volumeSlider.setBackground(chromeBg);
            volumeSlider.setForeground(textColor);
        }
    }

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Help Center");

        helpMenu.add(helpItem);

        // Action listener for help item
        helpItem.addActionListener(e -> {
            // Code to display HelpCenterPanel
            HelpCenterPanel helpPanel = new HelpCenterPanel();
            JOptionPane.showMessageDialog(null, helpPanel, "Help Center: Calculator Functionality", JOptionPane.INFORMATION_MESSAGE);
        });

        return helpMenu;
    }

    private JMenu createAboutMenu() {
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About my Calculator");

        aboutMenu.add(aboutItem);

        // Action listener for about item
        aboutItem.addActionListener(e -> {
            // Code to display AboutPanel
            AboutPanel aboutPanel = new AboutPanel();
            JOptionPane.showMessageDialog(null, aboutPanel, "About NumCruncher", JOptionPane.INFORMATION_MESSAGE);
        });

        return aboutMenu;
    }

	private void initializeResultField() {
        resultField = new JTextField(10);
        resultField.setEditable(false);
        resultField.setFont(new Font("SansSerif", Font.BOLD, 40));
        resultField.setBackground(themeManager.getBackgroundColor());
        resultField.setHorizontalAlignment(JTextField.RIGHT);
    }

	private void initializeNumberButtons() {
	    numButtons = new JButton[10];
	    for (int i = 0; i < 10; i++) {
                numButtons[i] = createButton(String.valueOf(i), BUTTON_FONT);
                numButtons[i].addActionListener(e -> {
                    soundManager.playRandomSound();
                });
            }
        }

	private void initializeOperationButtons() {
	    operationButtons = new JButton[4];
	    String[] operations = { "+", "-", "\u00d7", "\u00f7" };
	    for (int i = 0; i < 4; i++) {
                operationButtons[i] = createButton(operations[i], BUTTON_FONT);
                operationButtons[i].addActionListener(e -> {
                    soundManager.playRandomSound();
                });
            }
        }

    private void initializeFunctionButtons() {
        calculateButton = createButton("=", BUTTON_FONT);
        calculateButton.setBackground(themeManager.getEqualBackgroundColor());
        calculateButton.addActionListener(e -> soundManager.playSound("sounds/enter.wav"));
        signToggleButton = createButton("+/-", BUTTON_FONT);
        sinButton = createButton("sin", BUTTON_FONT);
        cosButton = createButton("cos", BUTTON_FONT);
        tanButton = createButton("tan", BUTTON_FONT);
        decimalButton = createButton(".", BUTTON_FONT);
        powerOfButton = createButton("xⁿ", BUTTON_FONT);
        backspaceButton = createButton("⌫", BUTTON_FONT);
        backspaceButton.addActionListener(e -> soundManager.playSound("sounds/Backspacecut.wav"));
        ceButton = createButton("CE", BUTTON_FONT);
        eulerButton = createButton("e", BUTTON_FONT);
        PIButton = createButton("𝜋", PI_BUTTON_FONT);
        powerOfTenButton = createButton("10ˣ", BUTTON_FONT);
        sqrRootButton = createButton("√x", BUTTON_FONT);
        logBaseTenButton = createButton("log" + "\u2081" + "\u2080", BUTTON_FONT);
        logBaseEButton = createButton("LN", BUTTON_FONT);
        secondaryFunctionButton = createButton("2nd", BUTTON_FONT);
        xPowerOf2Button = createButton("x²", BUTTON_FONT);
        secButton = createButton("sec", BUTTON_FONT);
        cscButton = createButton("csc", BUTTON_FONT);
        cotButton = createButton("cot", BUTTON_FONT);
        moduloButton = createButton("%", BUTTON_FONT);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 5));

        // Add buttons to the panel in the desired layout
        //Five buttons per row
        //row1
        buttonPanel.add(secondaryFunctionButton);
        buttonPanel.add(PIButton);
        buttonPanel.add(eulerButton);
        buttonPanel.add(ceButton);
        buttonPanel.add(backspaceButton);
                
        //row2
        buttonPanel.add(xPowerOf2Button);
        buttonPanel.add(sinButton);
        buttonPanel.add(cosButton);
        buttonPanel.add(tanButton);
        buttonPanel.add(moduloButton);
     
        //row3
        buttonPanel.add(sqrRootButton);
        buttonPanel.add(secButton);
        buttonPanel.add(cscButton);
        buttonPanel.add(cotButton);
        buttonPanel.add(operationButtons[3]);
        
        //row4
        buttonPanel.add(powerOfButton);
        buttonPanel.add(numButtons[7]);
        buttonPanel.add(numButtons[8]);
        buttonPanel.add(numButtons[9]);
        buttonPanel.add(operationButtons[2]);
        
        //row5
        buttonPanel.add(powerOfTenButton);
        buttonPanel.add(numButtons[4]);
        buttonPanel.add(numButtons[5]);
        buttonPanel.add(numButtons[6]);
        buttonPanel.add(operationButtons[1]);
        
        //Row6
        buttonPanel.add(logBaseTenButton);
        buttonPanel.add(numButtons[1]);
        buttonPanel.add(numButtons[2]);
        buttonPanel.add(numButtons[3]);
        buttonPanel.add(operationButtons[0]);
        
        //Row7
        buttonPanel.add(logBaseEButton);
        buttonPanel.add(signToggleButton);
        buttonPanel.add(numButtons[0]);
        buttonPanel.add(decimalButton);
        buttonPanel.add(calculateButton);

        return buttonPanel;
    }
    
    private void setDarkMode() {
        themeManager.applyDarkMode(mainPanel, resultField, numButtons, operationButtons, getNumberLikeButtons(), getFunctionButtons(), calculateButton);
        applyThemeToChrome();
    }

    private void setLightMode() {
        themeManager.applyLightMode(mainPanel, resultField, numButtons, operationButtons, getNumberLikeButtons(), getFunctionButtons(), calculateButton);
        applyThemeToChrome();
    }

    private List<JButton> getFunctionButtons() {
        return Arrays.asList(
                sinButton,
                cosButton,
                tanButton,
                powerOfButton,
                backspaceButton,
                ceButton,
                eulerButton,
                PIButton,
                powerOfTenButton,
                sqrRootButton,
                logBaseTenButton,
                logBaseEButton,
                secondaryFunctionButton,
                xPowerOf2Button,
                secButton,
                cscButton,
                cotButton,
                moduloButton
        );
    }

    private List<JButton> getNumberLikeButtons() {
        return Arrays.asList(
                signToggleButton,
                decimalButton
        );
    }
    
    // Create a JButton with the given label and font, and attach an ActionListener
    public JButton createButton(String label, Font font) {
        JButton button = new JButton(label);
        button.addActionListener(calculator);
        button.setFont(font);
        button.setBackground(themeManager.getButtonBackgroundColor());
        return button;
    }

	// Global method to handle key events
    private void handleGlobalKeyEvents(AWTEvent event) {
    	// if listeners are disabled for a dialog input
    	if (!areKeyListenersEnabled) {
    		return;
    	}
    	
    	// When key is pressed get its ID
        if (event instanceof KeyEvent) {
            KeyEvent ke = (KeyEvent) event;
            if (ke.getID() == KeyEvent.KEY_PRESSED) {
                int key = ke.getKeyCode();

                // Check for top row number key presses
                if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
                    int index = key - KeyEvent.VK_0;
                    numButtons[index].doClick();
                }

                // Check for numpad number key presses
                if (key >= KeyEvent.VK_NUMPAD0 && key <= KeyEvent.VK_NUMPAD9) {
                    int index = key - KeyEvent.VK_NUMPAD0;
                    numButtons[index].doClick();
                }

                // Check for operation key presses
                switch (key) {
                    case KeyEvent.VK_ADD:
                        operationButtons[0].doClick();
                        break;
                    case KeyEvent.VK_SUBTRACT:
                        operationButtons[1].doClick();
                        break;
                    case KeyEvent.VK_MULTIPLY:
                        operationButtons[2].doClick();
                        break;
                    case KeyEvent.VK_DIVIDE:
                        operationButtons[3].doClick();
                        break;
                    case KeyEvent.VK_ENTER:
                        calculateButton.doClick();
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                    	ke.consume();  // Stop windows error sound playing when pressed
                        backspaceButton.doClick();
                        break;
                    case KeyEvent.VK_DELETE:
                    	ke.consume();  // Stop windows error sound playing when pressed
                        ceButton.doClick();
                        break;
                    case KeyEvent.VK_DECIMAL:
                    	decimalButton.doClick();
                    	break;
                }
            }
        }
    }
    
    public void updateSecondaryFunctionLabels(boolean isSecondaryMode) {
        if (isSecondaryMode) {
            getSqrRootButton().setText("\u221b" + "x");
            getPowerOfTenButton().setText("2ˣ");
            getPowerOfButton().setText("n√x");
            getSinButton().setText("sin" + "\u207B" + "\u00B9");
            getCosButton().setText("cos" + "\u207B" + "\u00B9");
            getTanButton().setText("tan" + "\u207B" + "\u00B9");
            getxPowerOf2Button().setText("x³");
            getSecButton().setText("sec" + "\u207B" + "\u00B9");
            getCscButton().setText("csc" + "\u207B" + "\u00B9");
            getCotButton().setText("cot" + "\u207B" + "\u00B9");
            getLogBaseTenButton().setText("log" + "\u2099" + "x");
            logBaseEButton.setText("eˣ");
        } else {
            getSqrRootButton().setText("√x");
            getPowerOfButton().setText("xⁿ");
            getPowerOfTenButton().setText("10ˣ");                    
            getSinButton().setText("sin");
            getCosButton().setText("cos");
            getTanButton().setText("tan");
            getxPowerOf2Button().setText("x²");
            getSecButton().setText("sec");
            getCscButton().setText("csc");
            getCotButton().setText("cot");
            getLogBaseTenButton().setText("log" + "\u2081" + "\u2080");
            getLogBaseEButton().setText("LN");
        }
    }

    // Method to register the global event listener
    public void registerGlobalKeyEventListener() {
        AWTEventListener listener = new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                handleGlobalKeyEvents(event);
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
    }

    // Method to disable key listeners
    public void disableKeyListeners() {
        areKeyListenersEnabled = false;
    }

    // Method to enable key listeners
    public void enableKeyListeners() {
        areKeyListenersEnabled = true;
    }
    
    //Getters
    public JTextField getResultField() {
		return resultField;
	}

	public JButton[] getNumButtons() {
		return numButtons;
	}

	public JButton[] getOperationButtons() {
		return operationButtons;
	}

	public JButton getSignToggleButton() {
		return signToggleButton;
	}

	public JButton getSinButton() {
		return sinButton;
	}

	public JButton getCosButton() {
		return cosButton;
	}

	public JButton getTanButton() {
		return tanButton;
	}

	public JButton getDecimalButton() {
		return decimalButton;
	}

	public JButton getPowerOfButton() {
		return powerOfButton;
	}

	public JButton getBackspaceButton() {
		return backspaceButton;
	}

	public JButton getCeButton() {
		return ceButton;
	}

	public JButton getEulerButton() {
		return eulerButton;
	}

	public JButton getPIButton() {
		return PIButton;
	}

	public JButton getPowerOfTenButton() {
		return powerOfTenButton;
	}

	public JButton getSqrRootButton() {
		return sqrRootButton;
	}

	public JButton getLogBaseTenButton() {
		return logBaseTenButton;
	}

	public JButton getLogBaseEButton() {
		return logBaseEButton;
	}

	public JButton getSecondaryFunctionButton() {
		return secondaryFunctionButton;
	}

	public JButton getxPowerOf2Button() {
		return xPowerOf2Button;
	}

	public JButton getSecButton() {
		return secButton;
	}

	public JButton getCscButton() {
		return cscButton;
	}

	public JButton getCotButton() {
		return cotButton;
	}

	public JButton getModuloButton() {
		return moduloButton;
	}
}
