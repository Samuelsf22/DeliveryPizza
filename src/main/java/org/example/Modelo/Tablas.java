package org.example.Modelo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Tablas extends DefaultTableCellRenderer {
    private Color color = null;
    public void colorResaltado(Color color) {
        this.color = color;
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);//para centrar los datos de las columnas
        if (esPar(row)){//color de fondo si es par
            setBackground(new Color(103, 103, 103));
            setForeground(new Color(255, 255, 255));
        }else {//color de fondo si es impar
            setBackground(new Color(28, 28, 28));
            setForeground(new Color(255, 255, 255));
        }
        if (isSelected){//cuando selecciona una fila
            setBackground(color);
            setForeground(new Color(0, 0, 0));
        }

        return this;
    }
    protected boolean esPar(int row){
        if (row%2 ==0){
            //si el resgistro es divisible(mod) entre 2 y el retorno
            // es cero, significa que es par
            return true;
        }else {
            return false;
        }

    }
}
