import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

public final class AboutPanel extends JPanel {
	/**
	 * AboutPanel Class - Information Panel for NumCruncher Application
	 * Developed by Brendan LeGrand for CSCI 3300 - Term Project.
	 *
	 * This class represents an informational panel that is part of the NumCruncher calculator application.
	 * It provides details about the application, such as version information, developer credits, and other relevant
	 * acknowledgements. The panel uses a JTextPane to display formatted text and hyperlinks.
	 *
	 * Features:
	 * - Displays information about the NumCruncher application.
	 * - Contains hyperlinks to relevant resources or external websites.
	 * - Utilizes Swing components for a consistent look and feel with the rest of the application.
	 *
	 * Usage:
	 * - This panel can be accessed from the main application window to provide users with additional information.
	 */

	private static final long serialVersionUID = -6578036529732131603L;
    private static final String ABOUT_HTML = """
            <html>
            <head>
                <style>
                    body {
                        font-family: 'Calibri', sans-serif;
                        font-size: 12px;
                    }
                    p {
                        margin-bottom: 10px;
                    }
                    a {
                        color: blue;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <p><strong>Version:</strong> 1.1.3</p>
                <p>Developed by Brendan LeGrand</p>
                <p>For more information, visit my <a href="https://github.com/BrendanoElTaco/CSCI3300_Term_Project">GitHub</a></p>
                <p>NumCruncher is a user-friendly calculator designed to perform basic arithmetic operations, trigonometric calculations, and more.</p>
                <p>It was developed as a part of a term project for my software development class.</p>
                <p>If you find any bugs, please submit a bug report on <a href="https://github.com/BrendanoElTaco/CSCI3300_Term_Project">GitHub</a>.</p>
                <p>&nbsp;</p>
                <p><strong>Credits:</strong></p>
                <p>- Special thanks to <a href="https://ung.edu/human-resources/faculty-staff-bio/michael-mcleod.php">Michael McLeod, MBT</a> for teaching this class, and StackOverflow for saving my sanity.</p>
                <p>&nbsp;</p>
                <p><strong>Disclaimer:</strong></p>
                <p>- NumCruncher is provided "as is" for educational purposes and is not intended for professional use.</p>
            </body>
            </html>
            """;

	public AboutPanel() {
        JTextPane aboutTextPane = new JTextPane();
        aboutTextPane.setEditable(false);
        
        aboutTextPane.setContentType("text/html"); // Enable HTML content
        aboutTextPane.setText(ABOUT_HTML);

        // Add a hyperlink listener
        aboutTextPane.addHyperlinkListener((HyperlinkEvent e) -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                openLink(e);
            }
        });

        JScrollPane scrollPane = new JScrollPane(aboutTextPane);
        this.add(scrollPane);
    }

    private void openLink(HyperlinkEvent event) {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            showLinkError("Opening links is not supported on this system.");
            return;
        }

        if (event.getURL() == null) {
            showLinkError("This link cannot be opened.");
            return;
        }

        try {
            Desktop.getDesktop().browse(event.getURL().toURI());
        } catch (IOException | URISyntaxException | SecurityException ex) {
            String details = ex.getMessage();
            String message = "Unable to open the selected link.";
            if (details != null && !details.isBlank()) {
                message += "\n" + details;
            }
            showLinkError(message);
        }
    }

    private void showLinkError(String message) {
        JOptionPane.showMessageDialog(this, message, "Unable to Open Link", JOptionPane.ERROR_MESSAGE);
    }
}
