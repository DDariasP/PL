package proleg;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class SystemOutToJFrame {

    public static void main(String[] args) {
        // Create the JFrame
        JFrame frame = new JFrame("System.out to JTextPane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create the JTextPane
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);

        // Set the background and font colors
        textPane.setBackground(Color.LIGHT_GRAY); // Change to your preferred background color
        textPane.setForeground(Color.BLUE); // Change to your preferred font color

        // Set the font size
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr, 20); // Change to your preferred font size
        StyleConstants.setBold(attr, true);
        textPane.setParagraphAttributes(attr, true);

        // Center the text
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Create a JScrollPane and add the JTextPane to it
        JScrollPane scrollPane = new JScrollPane(textPane);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Redirect System.out to the JTextPane
        PrintStream printStream = new PrintStream(new CustomOutputStream(textPane));
        System.setOut(printStream);
        System.setErr(printStream); // Optional: redirect System.err as well

        // Show the frame
        frame.setVisible(true);

        // Print some output to test
        System.out.println("This is a test message.");
        System.out.println("Hello, World!");
    }
}

class CustomOutputStream extends OutputStream {

    private JTextPane textPane;

    public CustomOutputStream(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void write(int b) {
        appendText(String.valueOf((char) b));
    }

    @Override
    public void write(byte[] b, int off, int len) {
        appendText(new String(b, off, len));
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    private void appendText(String text) {
        try {
            StyledDocument doc = textPane.getStyledDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
            doc.insertString(doc.getLength(), text, attr);
            doc.setParagraphAttributes(doc.getLength(), 1, attr, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
