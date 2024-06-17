package prole;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import static javax.swing.WindowConstants.*;

/**
 * Redirige la salida en consola a pantalla.
 *
 * @author Diego Francisco Darias Pino
 */
public class Sout {

    /**
     * Método principal.
     *
     * @param jf Ventana en la que se muestran las salidas.
     */
    public static void run(JFrame jf) {

        //Crea la ventana
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jf.setSize(920, 1080);

        //Crea la JTextArea
        JTextArea ta = new JTextArea();
        ta.setEditable(false);

        //Cambia el color de fondo
        ta.setBackground(Color.DARK_GRAY);
        //Cambia el color de fuente
        ta.setForeground(Color.WHITE);
        //Cambia el tipo de fuente
        ta.setFont(new Font("Monospaced", Font.LAYOUT_LEFT_TO_RIGHT, 24));

        //Crea el JScrollPane donde se añade la JTextArea
        JScrollPane sp = new JScrollPane(ta);
        jf.add(sp, BorderLayout.CENTER);

        //Redirige System.out a la JTextArea
        PrintStream ps = new PrintStream(new MyOutputStream(ta));
        System.setOut(ps);
        //Redirige los errores a la JTextArea
        System.setErr(ps);

        //Muestra el Jframe
        jf.setVisible(true);
    }
}
