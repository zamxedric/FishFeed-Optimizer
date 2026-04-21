package view;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import controller.AppController;
import model.DailyFeedLog;
import model.FishBatch;
import model.SamplingRecord;
import util.ComboItem;
import util.DisplayHelper;
import util.MathEngine;

public class Analytics extends JPanel{
    private JLabel logo;
    private JLabel label[] = new JLabel[8];
    private JLabel panel[] = new JLabel[4];
    private JButton button[] = new JButton[4];
    private JComboBox<ComboItem> cbBatchName;
    private AppController controller;

    public Analytics(MainFrame parent){
        setLayout(null);
        setOpaque(false); 
        setPreferredSize(new Dimension(1280, 720));
        
        String svg = "src\\display_components\\Sidebar.svg";
        panel[0] = DisplayHelper.parsingSvg(svg, 0, 0, 375, 720);

        String logoIcon = ("src\\display_components\\AppLogo.svg");
        logo = DisplayHelper.parsingSvg(logoIcon, 20, 44, 284, 76);

        ImageIcon panelIcon = new ImageIcon("src\\display_components\\ChartComp.png");
        panel[1] = DisplayHelper.parsingImg(panelIcon, 420, 84, 810, 333);
        
        ImageIcon panelIcon2 = new ImageIcon("src\\display_components\\SummaryPanel.png");
        panel[2] = DisplayHelper.parsingImg(panelIcon2, 420, 394, 810, 333);

        String svg2 = "src\\display_components\\AnalyticsClicked.svg";
        panel[3] = DisplayHelper.parsingSvg(svg2, 32, 387, 301, 47);

        String svg3 = "src\\display_components\\DashboardNotClicked.svg";
        button[0] = DisplayHelper.buttonSvg(svg3, 43, 187, 301, 47);
        button[0].addActionListener(e -> parent.switchPage("Dashboard"));

        String svg4 = "src\\display_components\\Daily_Logs_Not_Clicked.svg";
        button[1] = DisplayHelper.buttonSvg(svg4, 43, 287, 301, 47);
        button[1].addActionListener(e -> parent.switchPage("DailyLogs"));

        String svg5 = "src\\display_components\\Add_NotClicked.svg";
        button[2] = DisplayHelper.buttonSvg(svg5, 43, 487, 301, 47);
        button[2].addActionListener(e -> parent.switchPage("AddBatch"));

        String svg6 = "src\\display_components\\Bi_NotClicked.svg";
        button[3] = DisplayHelper.buttonSvg(svg6, 43, 587, 301, 47);
        button[3].addActionListener(e -> parent.switchPage("BiWeeklySample"));

        cbBatchName = DisplayHelper.jComboBox(610, 89, 165, 36);
        this.add(cbBatchName);
        this.setComponentZOrder(cbBatchName, 0);

        cbBatchName.addActionListener(e -> {
            getBatchData();
            growthEfficiencyChart();
        });

        label[0] = DisplayHelper.fieldLabel(this,"Select Batch: ", 24, 440, 89, 165, 36);
        label[1]= DisplayHelper.fieldLabel(this,"Batch Summary", 24, 440, 478, 185, 36);
        label[2] = DisplayHelper.fieldLabel(this,"Total Feed:", 20, 440, 534, 269, 36);
        label[3]= DisplayHelper.fieldLabel(this,"Total Cost:", 20, 440, 584, 269, 36);
        label[4]= DisplayHelper.fieldLabel(this,"Current FCR:", 20, 740, 534, 165, 36);
        label[5]= DisplayHelper.fieldLabel(this,"", 20, 740, 584, 165, 36);
        label[6] = DisplayHelper.fieldLabel(this,"Status:", 20, 1140, 534, 165, 36);
        label[7] = DisplayHelper.fieldLabel(this,"", 20, 1140, 584, 165, 36);
        
        for(JLabel t:label){
            this.add(t);
        }
        for(JButton b:button){
            this.add(b);
        }
        this.add(logo);
        for(int i = panel.length - 1; i >= 0; i--){
            this.add(panel[i]);
        }
    }

    public void setController(AppController controller){
        this.controller = controller;
    }

    public JComboBox<ComboItem> getBatchComboBox() {
        return cbBatchName;
    }

    public void getBatchData(){
        ComboItem selectedBatch = (ComboItem) cbBatchName.getSelectedItem();

        if(selectedBatch == null){
            return;
        }

        try{
            MathEngine mathEngine = new MathEngine();
            FishBatch batch = controller.getBatchDAO().getBatchById(selectedBatch.getId());
            double total_cost_by_batch = controller.getLogDAO().getTotalFeedByBatch(selectedBatch.getId()) * controller.getLogDAO().getTotalCostByBatch(selectedBatch.getId());

            label[2].setText("Total Feed: " + String.format("%.2f kg", controller.getLogDAO().getTotalFeedByBatch(selectedBatch.getId())));
            label[3].setText("Total Cost: ₱" + String.format("%.2f Php", total_cost_by_batch));
            label[5].setText("" + String.format("%.2f", mathEngine.getLatestMovingFCR(batch)));
            if(mathEngine.getLatestMovingFCR(batch) > 2.4){
                label[7].setText("Critical");
            } else if (mathEngine.getLatestMovingFCR(batch) > 1.9){
                label[7].setText("Warning");
            } else if (mathEngine.getLatestMovingFCR(batch) > 1.5){
                label[7].setText("Fair");
            } else if (mathEngine.getLatestMovingFCR(batch) > 1.1){
                label[7].setText("Optimal");
            } else if (mathEngine.getLatestMovingFCR(batch) == -1.0){
                label[7].setText("No FCR");
                label[5].setText("Unavailable FCR");
            } else {
                label[7].setText("Excellent");
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void growthEfficiencyChart() {
    ComboItem selectedBatch = (ComboItem) cbBatchName.getSelectedItem();

    if (selectedBatch == null) {
        return; 
    }

    try {
        FishBatch batch = controller.getBatchDAO().getBatchById(selectedBatch.getId());

        XYSeries feedSeries = new XYSeries("Cumulative Feed (kg)");
        XYSeries gainSeries = new XYSeries("Total Biomass (kg)");

        // Populate Feed Series (Cumulative)
        List<DailyFeedLog> logs = controller.getLogDAO().getLogByBatch(selectedBatch.getId());
        double runningFeedTotal = 0.0;
        for (DailyFeedLog log : logs) {
            runningFeedTotal += log.getFeedGivenKg();
            long days = ChronoUnit.DAYS.between(batch.getStockDate(), log.getLogDate());
            feedSeries.add(days, runningFeedTotal);
        }

        double initialBiomass = batch.estimateBiomass(batch.getAvgWeightPerSample(), batch.getStockDate(), 0);
        gainSeries.add(0, initialBiomass);

        List<SamplingRecord> records = controller.getRecordDAO().getSamplesByBatch(selectedBatch.getId());
        for (SamplingRecord record : records) {
            long days = ChronoUnit.DAYS.between(batch.getStockDate(), record.getSampleDate());
            double totalGain = batch.estimateBiomass(
                record.getAvgWeightSample(), 
                record.getSampleDate(), 
                controller.getLogDAO().getTotalMortalityUpToDate(selectedBatch.getId(), record.getSampleDate())
            );
            gainSeries.add(days, totalGain);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(feedSeries);
        dataset.addSeries(gainSeries);
        JFreeChart chart = ChartFactory.createXYLineChart("Growth vs. Feeding Trends","Days Since Stocking", "Kilograms (kg)", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(440, 130, 770, 270);
        chartPanel.setOpaque(false);

        for (java.awt.Component comp : getComponents()) {
            if (comp instanceof ChartPanel) {
                remove(comp);
            }
        }

        this.add(chartPanel);
        this.setComponentZOrder(chartPanel, 0);
        
        revalidate();
        repaint();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

}