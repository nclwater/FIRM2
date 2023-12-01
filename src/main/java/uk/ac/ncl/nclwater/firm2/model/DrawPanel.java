package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

    int width;
    int height;
    int cell_size;

    ArrayList<Grid> grids;

    public DrawPanel(ArrayList<Grid> grids, int cell_size) {
        this.grids = grids;
        this.setBackground(Color.WHITE);
        this.width = grids.get(0).getWidth();
        this.height = grids.get(0).getHeight();
        this.cell_size = cell_size;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int font_height = g.getFontMetrics().getHeight();
        for (Grid grid : grids) {
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
    }


    public void setGrid(ArrayList<Grid> grids) {
        this.grids = grids;
    }
}
