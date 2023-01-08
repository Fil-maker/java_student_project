package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BarChart extends JFrame {
    private DefaultCategoryDataset dataset;
    private JFreeChart chart;

    public BarChart(DefaultCategoryDataset dataset) {
        this.dataset = dataset;
        initUI();
    }

    private void initUI() {

//        DefaultCategoryDataset dataset = createDataset();

        chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        chartPanel.setBackground(Color.white);
        add(new ChartPanel(chart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1024, 512);
            }
        });

        pack();
        setTitle("Гистограмма");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Распределение пользователей в интернете от всего населения по субрегионам",
                "Субрегионы",
                "%",
                dataset,
                PlotOrientation.HORIZONTAL,
                false, true, false);

        return barChart;
    }

    public void launch() throws IOException {
        ChartUtils.saveChartAsPNG(new File("src\\main\\resources\\histogram.png"), chart, 1024, 512);
        EventQueue.invokeLater(() -> {
            this.setVisible(true);

        });
    }

}
