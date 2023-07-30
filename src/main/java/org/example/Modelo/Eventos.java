package org.example.Modelo;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Eventos {
    public void isLetter(KeyEvent e) {
        char caracter = e.getKeyChar();
        if (!Character.isLetter(caracter) && !(caracter == KeyEvent.VK_SPACE)) {
            e.consume();
        }
    }

    public void isDigit(KeyEvent e, int digitos) {
        char caracter = e.getKeyChar();
        if (!Character.isDigit(caracter)) {
            e.consume();
        }else if (((JTextField)e.getSource()).getText().length() >= digitos) {
            e.consume();
        }
    }

    public void isDigitDecimal(KeyEvent e, int limite, int decimales) {
        char caracter = e.getKeyChar();

        if (!Character.isDigit(caracter) && caracter != '.') {
            e.consume();
        } else if (((JTextField)e.getSource()).getText().contains(".")) {
            //indexOf, se utiliza para obtener la posición de la primera aparición de un carácter o una cadena dentro de otra cadena.
            int posicionPunto = ((JTextField)e.getSource()).getText().indexOf(".");
            //devuelve un entero que representa la posición del cursor en relación con el texto contenido
            int posicionCursor = ((JTextField)e.getSource()).getCaretPosition();

            if (posicionCursor > posicionPunto && posicionCursor - posicionPunto > decimales) {
                e.consume();
            }
        } else if (((JTextField)e.getSource()).getText().length() >= limite) {
            e.consume();
        }
    }
    public void isUpperCasOrDigit(KeyEvent e, int limite){
        char caracter = e.getKeyChar();
        if (!Character.isUpperCase(caracter) && !Character.isDigit(caracter)) {
            e.consume();
        } else if (((JTextField)e.getSource()).getText().length() >= limite) {
            e.consume();
        }
    }
}
