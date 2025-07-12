package gui;

import javax.swing.*;
import java.awt.*;

public class KComponent {

    private final JComponent jComp;
    private final Rectangle rect;


    public KComponent(JComponent jC, Rectangle r) {
        jComp = jC;
        rect = r;
    }

    public JComponent getComponent() {
        return jComp;
    }

    public Rectangle getRectangle() {
        return rect;
    }
}
