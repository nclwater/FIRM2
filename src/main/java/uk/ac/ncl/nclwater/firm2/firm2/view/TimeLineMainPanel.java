package uk.ac.ncl.nclwater.firm2.firm2.view;

import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ncl.nclwater.firm2.firm2.model.ItineraryItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimeLineMainPanel extends JPanel implements ActionListener {
    Logger logger = LoggerFactory.getLogger(TimeLineMainPanel.class);

    MigLayout migLayout = new MigLayout("", "[]rel[]rel[]", "[]10[]");
    MigLayout migLayout1 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout2 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout3 = new MigLayout("", "[]rel[]", "[]10[]");
    MigLayout migLayout4 = new MigLayout("", "[]rel[]rel[]", "[]10[]");

    String timeEntry = "00:00:00";
    String seaLevel = "0";
    JTextField tf_timeEntry = new JTextField(timeEntry);
    JTextField tf_seaLevel = new JTextField(seaLevel,3);
    JPanel pnl_top = new JPanel(migLayout1);

    ArrayList<JTextField> tf_defenceBreach = new ArrayList<>();
    ArrayList<String> defenceBreaches = new ArrayList<>();
    JTextField tf_defenceBreaches = new JTextField("0", 3);
    JButton btn_defenceBreaches = new JButton("Defence Breaches");
    JPanel pnl_defenceBreaches = new JPanel(migLayout2);

    int numberOfCars = 0;
    String carId = "";
    int itineraryLegs = 0;
    JTextField tf_carId = new JTextField(10);
    JTextField tf_itineraryLegs = new JTextField(3);
    JPanel pnl_cars = new JPanel(migLayout3);

    JTextField tf_start = new JTextField(10);
    JTextField tf_end = new JTextField(10);
    JTextField tf_wait = new JTextField(10);
    ArrayList<ItineraryItem> it_items = new ArrayList<>();
    ArrayList<JTextField> tf_items = new ArrayList<>();
    JButton btn_itineraryLegs = new JButton("Add itinerary Legs");
    JPanel pnl_itineraryLegs = new JPanel(migLayout4);

    public TimeLineMainPanel() {
        setLayout(migLayout);
        pnl_top.add(new JLabel("Time Entry :"), "");
        pnl_top.add(tf_timeEntry, "wrap");
        pnl_top.add(new JLabel("Sea Level :"), "");
        pnl_top.add(tf_seaLevel, "wrap");
        pnl_top.add(new JLabel("Number of Defence Breaches :"), "");
        pnl_top.add(tf_defenceBreaches);
        pnl_top.add(btn_defenceBreaches, "wrap");
        btn_defenceBreaches.addActionListener(this);

        pnl_cars.add(new JLabel("Car Id start:"), "");
        pnl_cars.add(tf_carId, "wrap");
        pnl_cars.add(new JLabel("Itinerary Legs :"), "");
        pnl_cars.add(tf_itineraryLegs, "");
        pnl_cars.add(btn_itineraryLegs, "wrap");
        btn_itineraryLegs.addActionListener(this);

        Border lineBorder = BorderFactory.createLineBorder(Color.black);
        pnl_top.setBorder(lineBorder);
        pnl_defenceBreaches.setBorder(lineBorder);
        pnl_cars.setBorder(lineBorder);



        add(pnl_top, "span 10, growx, pushx, wrap");
        add(pnl_defenceBreaches, "span 10, growx, pushx, wrap");
        add(pnl_cars, "span 10, growx, pushx, wrap");
        add(pnl_itineraryLegs, "span 10, growx, pushx, wrap");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.info(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Defence Breaches":
                for (int i = 0; i < Integer.parseInt(tf_defenceBreaches.getText()); i++) {
                    tf_defenceBreach.add(i, new JTextField("", 3));
                    pnl_defenceBreaches.add(tf_defenceBreach.get(i), "");
                    defenceBreaches.add(tf_defenceBreach.get(i).getText());
                }
                revalidate();
                break;
            case "Add itinerary Legs":
                //because there are three fields in ItineraryItem
                for (int i = 0; i < Integer.parseInt(tf_itineraryLegs.getText()) * 3; i++) {
                    tf_items.add(new JTextField("", 10));
                    pnl_itineraryLegs.add(tf_items.get(i), ((i + 1)% 3 == 0)?"wrap":"");
                }
                revalidate();
                break;
        }
    }
}
