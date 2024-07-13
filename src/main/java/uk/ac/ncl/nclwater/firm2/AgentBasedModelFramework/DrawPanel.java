package uk.ac.ncl.nclwater.firm2.AgentBasedModelFramework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DrawPanel extends JPanel implements MouseListener {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    int width;
    int height;
    int cell_size;


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
                ArrayList<ComplexAgent> complexAgents = ((ComplexGrid) grid).getAgents();
                complexAgents.forEach(complexAgent -> {
                    g.setColor(complexAgent.getColour());
                    int index = complexAgent.getMovementIndex();
                    Point point = complexAgent.getMovements().get(index);
                    g.fillRect(point.x * cell_size, point.y * cell_size,
                            cell_size, cell_size);
                });
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
        sb.append(grids.size()).append(" layers:\n");
        grids.forEach((key, grid) -> {
            if (grid instanceof SimpleGrid simpleGrid) {
                if (simpleGrid.getCell(cell_x, cell_y) != null) {
                    sb.append(key).append("\n");
                    sb.append(simpleGrid.getCell(cell_x, cell_y).toString()).append("\n");
                }
            }
            if (grid instanceof ComplexGrid) {
                ArrayList<ComplexAgent> complexAgents = ((ComplexGrid) grid).getAgents();
                sb.append(((ComplexGrid) grid).key).append("\n");
                complexAgents.forEach(complexAgent -> {
                    int agent_x = complexAgent.getMovements().get(complexAgent.getMovementIndex()).x;
                    int agent_y = complexAgent.getMovements().get(complexAgent.getMovementIndex()).y;
                    sb.append(complexAgent.getAgent_id()).append(" ").append(agent_x).append(",")
                            .append(agent_y).append(" ").append(cell_x).append(",").append(cell_y).append("\n");
                    if (agent_y == cell_y && agent_x == cell_x) {
                        sb.append(complexAgent.getAgent_id()).append("\n");
                    }
                });

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
}
