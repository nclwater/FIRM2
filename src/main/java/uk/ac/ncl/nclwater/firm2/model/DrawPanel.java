package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DrawPanel extends JPanel implements MouseListener {

    int width;
    int height;
    int cell_size;
    final JDialog dialog = new JDialog();
    JTextArea dialog_text = new JTextArea("one two three");

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
        int font_height = g.getFontMetrics().getHeight();
        grids.forEach((key, grid) -> {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (grid.getCell(col, row) != null) {
                        g.setColor(grid.getCell(col, row).getColour());
                        g.fillRect(col * cell_size, row * cell_size,
                                cell_size, cell_size);
                        g.setColor(Color.WHITE);
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

    private void showInfoPopup(int x, int y) {
        StringBuilder sb = new StringBuilder("Mouse Clicked at pixel X: " + x + ", Y: " + y + "\n");
        int cell_x = x / cell_size;
        int cell_y = y / cell_size;
        int map_y = (height - 1) - (y / cell_size);
        sb.append("Map co-ordinate:").append(cell_x).append(", Y: ").append(map_y).append("\n");
        sb.append("Grid cell X:").append(cell_x).append(", Y: ").append(cell_y).append("\n");
        sb.append(grids.size()).append(" layers:\n");
        grids.forEach((key, grid) -> {
            if (grid.getCell(cell_x, cell_y) != null) {
                sb.append(grid.getCell(cell_x, cell_y).getClass().getName()).append("\n");
                sb.append(grid.getCell(cell_x, cell_y).toString()).append("\n");
            }
        });
        JOptionPane.showMessageDialog(this, sb.toString());
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
