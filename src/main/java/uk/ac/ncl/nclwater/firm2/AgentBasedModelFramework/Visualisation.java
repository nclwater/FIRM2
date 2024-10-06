package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * Provision for a simple visualisation, in a JFrame, of the model. The visualisation has a start and a step button.
 * If visualisation is set to true, the visualisation is set to true.
 */
public class Visualisation extends JFrame implements ActionListener {
    private static final Logger logger = LoggerFactory.getLogger(Visualisation.class);
   DrawPanel drawPanel;
   JPanel buttonPanel = new JPanel();
   JButton start = new JButton("Start");
   JButton step = new JButton("Step");
   Model model;

    Runnable runModel;

    /**
     * Constructor
     * @param model the model that should be visualised
     */
    public Visualisation(Model model) {
        this.model = model;
        int cell_size = model.getCell_size();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1024,768);
        start.addActionListener(this);
        step.addActionListener(this);
        this.setLayout(new MigLayout("", "[grow][]", "[]"));
        this.setTitle(model.getModelParameters().getTitle());
        this.setResizable(true);
        LinkedHashMap<String, Grid> grids = model.getGrids();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        drawPanel = new DrawPanel(grids, cell_size, model.getModelParameters());
        drawPanel.setBorder(blackline);
        drawPanel.setSize(model.getModelParameters().getWidth() * cell_size,
                model.getModelParameters().getHeight() * cell_size);

        buttonPanel.add(start);
        start.setText((model.getModelParameters().isRunOnStartUp()?"Stop":"Start"));
        buttonPanel.add(step);
        step.setEnabled(!model.getModelParameters().isRunOnStartUp());
        drawPanel.setPreferredSize(new Dimension(model.getModelParameters().getWidth() * cell_size,
                model.getModelParameters().getHeight() * cell_size));
        this.add(drawPanel, "growy");
        this.add(buttonPanel, "grow");
        this.setVisible(true);


        if (model.getRun()) {
            doRun();
        }
    }

    /**
     * Call this method to run the model
     */
    private void doRun() {
//        step.setEnabled(false);
//        start.setText("Stop");
        runModel = () -> {
            drawPanel.setGrid(model.getGrids());
            drawPanel.repaint();
        };
        model.setRun(true);
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
            start.setText("Stop");
            step.setEnabled(false);
            doRun();
        }
        if ("Stop".equals(e.getActionCommand())) {
            start.setText("Start");
            step.setEnabled(true);
            model.setRun(false);
        }
        if ("Step".equals(e.getActionCommand())) {
            model.step();
            drawPanel.setGrid(model.getGrids());
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
