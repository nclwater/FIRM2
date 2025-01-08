package uk.ac.ncl.nclwater.firm2.firm2.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class TimeLineMainPanel extends JPanel implements ActionListener {
    static Logger logger = LoggerFactory.getLogger(TimeLineMainPanel.class);
    static Hashtable<String, Integer> types = new Hashtable<>();

    MigLayout migLayout = new MigLayout("", "[]rel[]rel[]", "[]10[]");
    MigLayout migLayout1 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout2 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout3 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout4 = new MigLayout("", "[]rel[]rel[]", "[]10[]");

    JPanel pnl_topButtons = new JPanel(new MigLayout("", "[]rel[]rel[]", "[]10[]"));
    JButton btn_clearAll = new JButton("Clear All");
    JButton btn_clearEntry = new JButton("Clear Entry");

    JTextField tf_timeEntry = new JTextField("00:00:00");
    JTextField tf_seaLevel = new JTextField("0", 3);
    JPanel pnl_top = new JPanel(migLayout1);

    ArrayList<JTextField> tf_defenceBreach = new ArrayList<>();
    ArrayList<String> defenceBreaches = new ArrayList<>();
    JTextField tf_defenceBreaches = new JTextField("0", 3);
    JButton btn_defenceBreaches = new JButton("Defence Breaches");
    JPanel pnl_defenceBreaches = new JPanel(migLayout2);

    JTextField tf_numberOfCars = new JTextField(String.valueOf(0), 4);
    JTextField tf_carId = new JTextField(10);
    JTextField tf_itineraryLegs = new JTextField(3);
    JPanel pnl_cars = new JPanel(migLayout3);

    ArrayList<JComboBox> cb_startBuildingType = new ArrayList<>();
    ArrayList<JComboBox> cb_endBuildingType = new ArrayList<>();
    ArrayList<JTextField> tf_waitTime = new ArrayList<>();
    JButton btn_itineraryLegs = new JButton("Add itinerary Legs");
    JPanel pnl_itineraryLegs = new JPanel(migLayout4);
    JPanel pnl_buttons = new JPanel(new MigLayout("", "[]rel[]rel[]", "[]10[]"));
    JButton btn_save = new JButton("Write timeline item to file.");
    JButton btn_addTimeLineEntry = new JButton("Add time line entry");

    ModelStateChanges modelStateChanges = new ModelStateChanges();

    public TimeLineMainPanel() {
        setLayout(migLayout);
        pnl_topButtons.add(btn_clearAll);
        pnl_topButtons.add(btn_clearEntry);
        btn_clearAll.addActionListener(this);
        btn_clearEntry.addActionListener(this);

        pnl_top.add(new JLabel("Time Entry :"), "");
        pnl_top.add(tf_timeEntry, "wrap");
        pnl_top.add(new JLabel("Sea Level :"), "");
        pnl_top.add(tf_seaLevel, "wrap");
        pnl_top.add(new JLabel("Number of Defence Breaches :"), "");
        pnl_top.add(tf_defenceBreaches);
        pnl_top.add(btn_defenceBreaches, "wrap");
        btn_defenceBreaches.addActionListener(this);

        pnl_cars.add(new JLabel("Number of cars"), "");
        pnl_cars.add(tf_numberOfCars, "wrap");
        pnl_cars.add(new JLabel("Car Id start:"), "");
        pnl_cars.add(tf_carId, "wrap");
        pnl_cars.add(new JLabel("Itinerary Legs :"), "");
        pnl_cars.add(tf_itineraryLegs, "");
        pnl_cars.add(btn_itineraryLegs, "wrap");
        btn_itineraryLegs.addActionListener(this);
        btn_save.setActionCommand("save");
        btn_save.addActionListener(this);
        btn_addTimeLineEntry.setActionCommand("addTimeLineEntry");
        btn_addTimeLineEntry.addActionListener(this);

        Border lineBorder = BorderFactory.createTitledBorder("Clear");
        pnl_topButtons.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder("Time, Sea level, Defences");
        pnl_top.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder("Defence IDs");
        pnl_defenceBreaches.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder("Time, Sea level, Defences");
        pnl_cars.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder("Cars and itineraries");
        pnl_itineraryLegs.setBorder(lineBorder);
        lineBorder = BorderFactory.createTitledBorder("Append and save");
        pnl_buttons.setBorder(lineBorder);

        add(pnl_topButtons, "span 10, growx, pushx, wrap");
        add(pnl_top, "span 10, growx, pushx, wrap");
        add(pnl_defenceBreaches, "span 10, growx, pushx, wrap");
        add(pnl_cars, "span 10, growx, pushx, wrap");
        add(pnl_buttons, "span 10, growx, pushx, wrap");
        add(pnl_itineraryLegs, "span 10, growx, pushx, wrap");
        pnl_buttons.add(btn_addTimeLineEntry).setEnabled(true);
        pnl_buttons.add(btn_save, "wrap");
        btn_save.setEnabled(false);
    }

    private void clear() {
        tf_timeEntry.setText("00:00:00");
        tf_seaLevel.setText("0");
        tf_defenceBreaches.setText("0");
        tf_numberOfCars.setText("0");
        tf_carId.setText("");
        tf_itineraryLegs.setText("");
        pnl_itineraryLegs.removeAll();
        revalidate();

    }

    static boolean b = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Clear All":
                clear();
                break;
            case "Clear Entry":
                clear();
                break;
            case "Defence Breaches":
                tf_defenceBreach.clear();
                pnl_defenceBreaches.removeAll();
                repaint();
                for (int i = 0; i < Integer.parseInt(tf_defenceBreaches.getText()); i++) {
                    tf_defenceBreach.add(i, new JTextField("", 3));
                    pnl_defenceBreaches.add(tf_defenceBreach.get(i), "");
                }
                revalidate();
                break;
            case "Add itinerary Legs":
                Object[] obj = getBuildingTypes().keySet().toArray(new String[0]);
                if (!tf_numberOfCars.getText().trim().equals("0")) {
                    if (tf_carId.getText().isEmpty() || tf_itineraryLegs.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null,
                                "You have to enter the car id to start at and the number of itinerary legs.");
                    } else {
                        for (int i = 0; i < Integer.parseInt(tf_itineraryLegs.getText()); i++) {
                            cb_startBuildingType.add(new JComboBox<>(obj));
                            cb_endBuildingType.add(new JComboBox<>(obj));
                            tf_waitTime.add(new JTextField("0", 3));
                            pnl_itineraryLegs.add(cb_startBuildingType.get(i), "");
                            pnl_itineraryLegs.add(cb_endBuildingType.get(i), "");
                            pnl_itineraryLegs.add(tf_waitTime.get(i), "wrap");
                        }

                    }
                }
                btn_addTimeLineEntry.setEnabled(true);
                btn_save.setEnabled(true);
                revalidate();
                break;
            case "addTimeLineEntry":
                pnl_itineraryLegs.removeAll();
                revalidate();
                String startNode = "";
                String endNode = "";
                ModelState modelState = new ModelState();
                modelState.setTime(tf_timeEntry.getText());
                modelState.setSeaLevel(Float.parseFloat(tf_seaLevel.getText()));
                defenceBreaches.clear();
                for (int i = 0; i < Integer.parseInt(tf_defenceBreaches.getText()); i++) {
                    defenceBreaches.add(tf_defenceBreach.get(i).getText());
                }
                modelState.setDefenceBreach(defenceBreaches);
                int numberOfCars = Integer.parseInt(tf_numberOfCars.getText());
                if (numberOfCars > 0) {
                    int carId = Integer.parseInt(tf_carId.getText());
                    Cars cars = new Cars();
                    DecimalFormat df = new DecimalFormat("#####00000");
                    for (int car = 0; car < numberOfCars; car++) {
                        carId = car + 1;
                        String formatted = df.format(carId); // Formats as three digits with leading zeros
                        ArrayList<ItineraryItem> itItems = new ArrayList<>();
                        for (int i = 0; i < Integer.parseInt(tf_itineraryLegs.getText()); i++) {
                            int waitTime;
                            waitTime = (tf_waitTime.isEmpty()) ? 0 : Integer.parseInt(tf_waitTime.get(i).getText());
                            int startNodeType = types.get(cb_startBuildingType.get(i).getSelectedItem().toString());
                            int endNodeType = types.get(cb_endBuildingType.get(i).getSelectedItem().toString());
                            if (i == 0) {
                                startNode = getBuilding(startNodeType);
                                endNode = getBuilding(endNodeType);
                            } else {
                                startNode = endNode;
                                endNode = getBuilding(endNodeType);
                            }
                            logger.info("car {} start node: {} {}, end node: {} {}", "car" + formatted, startNodeType, startNode,
                                    endNodeType, endNode);
                            ItineraryItem itineraryItem = new ItineraryItem(startNode, endNode, waitTime);
                            itItems.add(itineraryItem);
                        }
                        Car newCar = new Car("car" + formatted, itItems);
                        cars.addCar(newCar);
                    }
                    carId++;
                    tf_carId.setText(String.valueOf(carId));
                    modelState.setCars(cars.getCars());
                }
                logger.info("Insert state change");
                modelStateChanges.insertModelState(modelState);
                revalidate();
                btn_save.setEnabled(true);
                break;
            case "save":
                logger.trace("Disable buttons");
                btn_save.setEnabled(false);
                //btn_addTimeLineEntry.setEnabled(false);
                writeToFile(modelStateChanges);
                break;
        }
    }

    private void writeToFile(ModelStateChanges modelStateChanges) {
        logger.info("Write timeline to file");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String filename = jfc.getSelectedFile().getPath();
            filename = (filename.endsWith(".json") || filename.endsWith(".JSON")) ? filename : filename + ".json";

            try {
                Writer writer = new FileWriter(filename);
                gson.toJson(modelStateChanges, writer);
                writer.flush();
                writer.close();
                logger.debug("Writing done");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            logger.debug("cancelled");
        }
    }

    private static Hashtable<String, Integer> getBuildingTypes() {
        String url = "jdbc:sqlite:data/inputs/database.db";

        try (var conn = DriverManager.getConnection(url)) {
            String sql = "select * from buildingtypes";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                types.put(rs.getString("description"), rs.getInt("building_type"));
            }
        } catch (
                SQLException e) {
            logger.error(e.getMessage());
        }
        return types;
    }

    private String getBuilding(int type) {
        String url = "jdbc:sqlite:/data/inputs/database.db";
        ArrayList<String> roadID = new ArrayList<>();
        int randomNumber = 0;
        try (var conn = DriverManager.getConnection(url)) {
            String sql = "select nearest_node_code\n" +
                    "from buildings b\n" +
                    "inner join classification c on b.class_id=c.class_id\n" +
                    "inner JOIN buildingtypes b2 on b2.building_type = c.building_type \n" +
                    "where b2.building_type = " + type;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roadID.add(rs.getString("nearest_node_code"));
            }
            randomNumber = (int) (Math.random() * roadID.size());
        } catch (
                SQLException e) {
            logger.error(e.getMessage());
        }

        return roadID.get(randomNumber);
    }

}
