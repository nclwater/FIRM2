/******************************************************************************
 * Copyright 2025 Newcastle University
 *
 * This file is part of FIRM2.
 *
 * FIRM2 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * FIRM2 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with FIRM2. If not, see <https://www.gnu.org/licenses/>. 
 *****************************************************************************/


package uk.ac.ncl.nclwater.firm2.examples.distribution;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AgentGenerator extends ApplicationFrame {

    public AgentGenerator(String title) {
        super(title);
        JFreeChart histogram = createChart();
        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public static List<Agent> generateAgents(int numberOfAgents, double standardDeviationInMinutes, long specifiedTime) {
        List<Agent> agents = new ArrayList<>();
        Random random = new Random();

        // Convert standard deviation from minutes to milliseconds
        double standardDeviationInMillis = standardDeviationInMinutes * 60 * 1000;

        for (int i = 0; i < numberOfAgents; i++) {
            long time = Math.round(specifiedTime + random.nextGaussian() * standardDeviationInMillis);
            agents.add(new Agent(i, time));
        }

        return agents;
    }

    private JFreeChart createChart() {
        int numberOfAgents = 400; // Example input
        double standardDeviationInMinutes = 15; // Example input
        long specifiedTime = System.currentTimeMillis(); // Example input

        List<Agent> agents = generateAgents(numberOfAgents, standardDeviationInMinutes, specifiedTime);

        double[] times = new double[agents.size()];
        for (int i = 0; i < agents.size(); i++) {
            // Convert time to minutes since specified time
            times[i] = (agents.get(i).getTime() - specifiedTime) / (60.0 * 1000);
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Agent Times", times, 50);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Time Distribution of Agents",
                "Time (minutes)",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) histogram.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        plot.setRenderer(renderer);

        return histogram;
    }

    public static void main(String[] args) {
        AgentGenerator demo = new AgentGenerator("Agent Time Distribution");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
