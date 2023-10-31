package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Povision for a simple visualisation, in a JFrame, of the model. The visualisation has a start and a step button.
 * If visualisation is set to true, the visualisation is set to true.
 */
public class Visualisation extends JFrame implements ActionListener {

   DrawPanel drawPanel;
   JPanel buttonPanel = new JPanel();
   JButton start = new JButton("Start");
   JButton step = new JButton("Step");
   Model model;

    Runnable runModel;

    /**
     * Constructor
     * @param model
     */
    public Visualisation(Model model) {
        this.model = model;
        int cell_size = model.getCell_size();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        start.addActionListener(this);
        step.addActionListener(this);
        this.setLayout(null);
        this.setTitle(model.getModelParameters().getTitle());
        this.setResizable(true);
        Grid grid = model.getGrid();
        this.setSize(1024,768);
        drawPanel = new DrawPanel(grid, cell_size);
        drawPanel.setSize(grid.getWidth() * cell_size, grid.getHeight() * cell_size);

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


    /**
     * Actions to be performed when the Start and Step buttons are pressed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if ("Start".equals(e.getActionCommand())) {
            step.setEnabled(false);
            start.setText("Stop");
           runModel = new Runnable() {
                public void run() {
                    drawPanel.setGrid(model.getGrid());
                    drawPanel.repaint();
                }
            };

            model.setRun(true);
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

    /**
     * Get the model that is being running by the visualisation
     * @return
     */
    public Runnable getRunModel() {
        return runModel;
    }

    /**
     * The model that should be run in the visualisation
     * @param runModel
     */
    public void setRunModel(Runnable runModel) {
        this.runModel = runModel;
    }

}
