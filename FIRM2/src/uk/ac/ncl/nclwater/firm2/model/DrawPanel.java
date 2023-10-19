package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    int width;
    int height;
    int cell_size;

    Grid grid;

    public DrawPanel(Grid grid, int cell_size) {
        this.grid = grid;
        this.setBackground(Color.WHITE);
        this.width = grid.getWidth();
        this.height = grid.getHeight();
        this.cell_size = cell_size;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int font_height = g.getFontMetrics().getHeight();
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
    }


    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
