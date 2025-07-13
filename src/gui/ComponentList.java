package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * List of {@link JComponent}s wrapped in {@link KComponent}s
 */
public class ComponentList extends ArrayList<KComponent> {

    /**
     * Creates a new empty ComponentList
     */
    public ComponentList() {
        super();
    }

    /**
     * Add the component inside the specified Rectangle
     * @param jC Component to add
     * @param r Location of the Component
     */
    public void add(JComponent jC, Rectangle r) {
        super.add(new KComponent(jC, r));
    }
}
