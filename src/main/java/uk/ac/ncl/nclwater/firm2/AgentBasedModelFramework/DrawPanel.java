package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static uk.ac.ncl.nclwater.firm2.firm2.controller.Utilities.GridXY2BNG;

public class DrawPanel extends JPanel implements MouseListener {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    int width;
    int height;
    int cell_size;
    float x_origin = 0;
    float y_origin = 0;


    LinkedHashMap<String, Grid> grids;

    public DrawPanel(LinkedHashMap<String, Grid> grids, int cell_size, ModelParameters modelParameters) {
        super();
        addMouseListener(this);
        this.grids = grids;
        this.setBackground(Color.WHITE);
        this.width = modelParameters.getWidth();
        this.height = modelParameters.getHeight();
        this.cell_size = cell_size;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        grids.forEach((key, grid) -> {
            if (grid instanceof SimpleGrid simpleGrid) {
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (simpleGrid.getCell(col, row) != null) {
                            g.setColor(simpleGrid.getCell(col, row).getColour());
                            g.fillRect(col * cell_size, row * cell_size,
                                    cell_size, cell_size);
                            g.setColor(Color.WHITE);
                        }
                    }
                }
            }
            if (grid instanceof ComplexGrid complexGrid) {
                // for each complex agent
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        ArrayList<Agent> agents = complexGrid.getCells(col, row);
                        int finalCol = col;
                        int finalRow = row;
                        if (agents != null) {
                            agents.forEach(agent -> {
                                g.setColor(agent.getColour());
                                g.fillRect(finalCol * cell_size, finalRow * cell_size,
                                        cell_size, cell_size);
                                g.setColor(Color.WHITE);
                            });
                        }
                    }
                }
            }
        });
    }


    public void setGrid(LinkedHashMap<String, Grid> grids) {
        this.grids = grids;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        showInfoPopup(x, y);
    }

    /**
     * A double click on a cell would cause this popup to be displayed with info of all the
     * layers of the cell
     * @param mouse_x
     * @param mouse_y
     */
    private void showInfoPopup(int mouse_x, int mouse_y) {
        StringBuilder sb = new StringBuilder("Mouse Clicked at pixel X: " + mouse_x + ", Y: " + mouse_y + "\n");
        int cell_x = mouse_x / cell_size;
        int cell_y = mouse_y / cell_size;
        int map_y = (height - 1) - cell_y; // invert y co-ordinate
        sb.append("Map co-ordinate:").append(cell_x).append(", Y: ").append(map_y).append("\n");
        sb.append("Grid cell X:").append(cell_x).append(", Y: ").append(cell_y).append("\n\n");
        Point point = GridXY2BNG(x_origin, y_origin, cell_x, cell_y, cell_size);
        sb.append("BNG co-oridnates: X:").append(point.x).append(", Y: ").append(point.y).append("\n");
        sb.append(grids.size()).append(" layers:\n");
        grids.forEach((key, grid) -> {
            if (grid instanceof SimpleGrid simpleGrid) {
                if (simpleGrid.getCell(cell_x, cell_y) != null) {
                    sb.append(key).append("\n");
                    sb.append(simpleGrid.getCell(cell_x, cell_y).toString()).append("\n");
                }
            }
            if (grid instanceof ComplexGrid complexGrid) {
                if (complexGrid.getCells(cell_x, cell_y) != null) {
                    ArrayList<Agent> agents = complexGrid.getCells(cell_x, cell_y);
                    agents.forEach(agent -> sb.append(agent.getAgent_id()).append("\n"));
                }
            }
        });
        JTextArea textArea = new JTextArea(sb.toString());
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        panel.add(textArea, BorderLayout.CENTER);
        JOptionPane.showConfirmDialog(null, scrollPane, "Cell Info",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public float getX_origin() {
        return x_origin;
    }

    public void setX_origin(float x_origin) {
        this.x_origin = x_origin;
    }

    public float getY_origin() {
        return y_origin;
    }

    public void setY_origin(float y_origin) {
        this.y_origin = y_origin;
    }
}
