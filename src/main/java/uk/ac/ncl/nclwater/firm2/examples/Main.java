package uk.ac.ncl.nclwater.firm2.examples;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JFrame implements MouseListener {
    public static void main(String[] args) {
        Main frame = new Main();
    }
        // Create the frame
    public Main() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);

        // Create the main panel with MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("fill", "[246px!][grow]", "[]"));

        // Create the left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 1024, 768);
        leftPanel.setPreferredSize(new Dimension(246, 179));
        leftPanel.setBackground(java.awt.Color.RED); // Just to visualize the panel
        leftPanel.addMouseListener(this);

        // Create the right panel with two buttons
        JPanel rightPanel = new JPanel(new MigLayout("wrap 1", "[]", "[]20[]"));
        JButton button1 = new JButton("Button 1");
        JButton button2 = new JButton("Button 2");
        rightPanel.add(button1, "growx");
        rightPanel.add(button2, "growx");

        // Add panels to the main panel
        mainPanel.add(leftPanel, "growy");
        mainPanel.add(rightPanel, "grow");

        // Add the main panel to the frame
        add(mainPanel);

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        JOptionPane.showConfirmDialog(null,  "XY: " + x + ", " + y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
