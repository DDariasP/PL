package prole;

import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * Adapta una JTextArea a la clase OutputStream.
 *
 * @author Diego Francisco Darias Pino
 */
public class MyOutputStream extends OutputStream {

    private JTextArea ta;

    public MyOutputStream(JTextArea textArea) {
        ta = textArea;
    }

    @Override
    public void write(int b) {
        ta.append(String.valueOf((char) b));
        ta.setCaretPosition(ta.getDocument().getLength());
    }

    @Override
    public void write(byte[] b, int off, int len) {
        ta.append(new String(b, off, len));
        ta.setCaretPosition(ta.getDocument().getLength());
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

}
