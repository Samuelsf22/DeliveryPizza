package org.example.Modelo;

import javax.swing.*;
import java.awt.*;

public class Mensajes {
    public void mostrarMensaje(JPanel panel, String mensaje, String titulo, int tipoMensaje) {
        Color amarillo = new Color(255, 183, 58);
        Color negro1 = new Color(35,35,35);

        UIManager.put("OptionPane.background", negro1);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", negro1);
        UIManager.put("Button.foreground", amarillo);
        UIManager.put("Button.border", BorderFactory.createEmptyBorder()); // Quitar los bordes del botón
        UIManager.put("Panel.background", negro1);

        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        UIManager.put("Button.defaultButtonFollowsFocus", true);
        UIManager.put("OptionPane.buttonFont", UIManager.getFont("OptionPane.messageFont"));

        JOptionPane.showMessageDialog(panel, mensaje, titulo, tipoMensaje);
    }
    public int mensajeCOnfirmar(JPanel panel, String mensaje, String titulo, int tipoMensaje) {
        Color amarillo = new Color(255, 183, 58);
        Color negro1 = new Color(35,35,35);

        UIManager.put("OptionPane.background", negro1);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", negro1);
        UIManager.put("Button.foreground", amarillo);
        UIManager.put("Button.border", BorderFactory.createEmptyBorder()); // Quitar los bordes del botón
        UIManager.put("Panel.background", negro1);

        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        UIManager.put("Button.defaultButtonFollowsFocus", true);
        UIManager.put("OptionPane.buttonFont", UIManager.getFont("OptionPane.messageFont"));

        return JOptionPane.showConfirmDialog(panel, mensaje, titulo, tipoMensaje);
    }
}
