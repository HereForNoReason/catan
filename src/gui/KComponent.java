package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Wraps a Component with a rectangle where it is positioned
 */
public class KComponent {

    private final JComponent jComp;
    private final Rectangle rect;

    /**
     * Wraps a component with a position
     *
     * @param jC The component to wrap
     * @param r  The position and size of the component
     */
    public KComponent(JComponent jC, Rectangle r) {
        jComp = jC;
        rect = r;
    }

    /**
     * Gets the wrapped component
     *
     * @return The wrapped component
     */
    public JComponent getComponent() {
        return jComp;
    }

    /**
     * Gets the position for the component
     *
     * @return The position for the component
     */
    public Rectangle getRectangle() {
        return rect;
    }
}
