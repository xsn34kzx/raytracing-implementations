package kz.sn34.raytrace_java_lib;

import java.awt.event.*;
import javax.swing.*;

public class NumberListener implements KeyListener
{
    private boolean isDouble;
    private boolean allowNegative;

    public NumberListener(boolean isDouble, boolean allowNegative)
    {
        this.isDouble = isDouble;
        this.allowNegative = allowNegative;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        JTextField source = (JTextField) e.getSource();
        String sourceText = source.getText();
        char curChar = e.getKeyChar();

        if(curChar == '-' && this.allowNegative)
        {
            if(!sourceText.startsWith("-"))
                source.setText("-" + sourceText);
            e.consume();
        }
        else if(curChar == '.' && this.isDouble)
        {
            if(sourceText.contains("."))
                e.consume();
        }
        else if(!Character.isDigit(curChar))
            e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {}

    @Override
    public void keyPressed(KeyEvent e)
    {}
}
