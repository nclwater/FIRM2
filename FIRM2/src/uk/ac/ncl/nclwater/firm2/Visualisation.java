package uk.ac.ncl.nclwater.firm2;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visualisation extends JFrame implements ActionListener {

   DrawPanel drawPanel;
   JPanel buttonPanel = new JPanel();
   JButton start = new JButton("Start");
   JButton step = new JButton("Step");
   Model model;
   Thread modelThread;

    public Visualisation(Model model) {
        this.model = model;
        int cell_size = model.getCell_size();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        start.addActionListener(this);
        step.addActionListener(this);
        this.setLayout(null);
        this.setTitle("Conway");
        this.setResizable(true);
        Grid grid = model.getGrid();
        this.setSize(1024,768);
        drawPanel = new DrawPanel(grid, cell_size);
        drawPanel.setSize(grid.getWidth() * cell_size, grid.getHeight() * cell_size + cell_size);

        buttonPanel.add(start);
        buttonPanel.add(step);
        Insets insets = this.getInsets();
        Dimension dimension = buttonPanel.getPreferredSize();
        buttonPanel.setBounds(insets.left + 10, insets.top + 10, dimension.width, dimension.height);
        this.add(drawPanel);
        dimension = drawPanel.getPreferredSize();
        drawPanel.setBounds(insets.left + 10, insets.top + 50, grid.getWidth() * cell_size,
                grid.getHeight() * cell_size + cell_size);
        this.add(buttonPanel);
        this.setVisible(true);
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action: " + e.getActionCommand());

        if ("Start".equals(e.getActionCommand())) {
            step.setEnabled(false);
            start.setText("Stop");
            model.setRun(true);
            drawPanel.setGrid(model.getGrid());
        }
        if ("Stop".equals(e.getActionCommand())) {
            start.setText("Start");
            step.setEnabled(true);
            model.setRun(false);
        }
        if ("Step".equals(e.getActionCommand())) {
            model.step();
            drawPanel.setGrid(model.getGrid());
        }

    }

    public Thread getModelThread() {
        return modelThread;
    }

    public void setModelThread(Thread modelThread) {
        this.modelThread = modelThread;
    }
}
