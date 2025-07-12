package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ComponentList extends ArrayList<KComponent> {

    public void add(JComponent jC, Rectangle r) {
        super.add(new KComponent(jC, r));
    }
}
