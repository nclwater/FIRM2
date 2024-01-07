package uk.ac.ncl.nclwater.firm2.model;

import uk.ac.ncl.nclwater.firm2.utils.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements MouseListener {

    int width;
    int height;
    int cell_size;
    final JDialog dialog = new JDialog();
    JTextArea dialog_text = new JTextArea("one two three");

    ArrayList<Grid> grids;

    public DrawPanel(ArrayList<Grid> grids, int cell_size) {
        super();
        addMouseListener(this);
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
        sb.append("Map co-ordinate:" + cell_x + ", Y: " +  map_y + "\n");
        sb.append("Grid cell X:" + cell_x + ", Y: " + (cell_y) + "\n");
        sb.append(grids.size() + " layers:\n");
        for (Grid grid: grids) {
            if (grid.getCell((cell_x), (cell_y)) != null) {
                sb.append(grid.getCell(cell_x, cell_y).getClass().getName() + "\n");
                sb.append(grid.getCell(cell_x, cell_y).toString() + "\n");
            }
        }
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
