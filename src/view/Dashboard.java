package view;

import javax.swing.*;

import util.DisplayHelper;
import util.MathEngine;

import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import dao.FeedingLogDAO;
import dao.FishBatchDAO;
import dao.SamplingRecordDAO;
import model.FishBatch;
import model.SamplingRecord;

public class Dashboard extends JPanel {
    private JLabel logo;
    private JLabel label[] = new JLabel[7];
    private JLabel panel[] = new JLabel[4];
    private JButton button[] = new JButton[6];
    private JRadioButton tempButton[] = new JRadioButton[3];
    private JRadioButton weatherButton[] = new JRadioButton[3];
    private JScrollPane scrollPane;
    private JPanel batchesContainer;
    private JPanel selectedPanel;
    private FishBatchDAO batchDAO = new FishBatchDAO();
    private FishBatch selectedBatch;
    private MathEngine mathEngine = new MathEngine();
    private Consumer<FishBatch> onHarvestCallback;
    private Consumer<FishBatch> onDeleteCallback;
    

    public void setOnHarvest(Consumer<FishBatch> callback) {
        this.onHarvestCallback = callback;
    }

    public void setOnDelete(Consumer<FishBatch> callback) {
        this.onDeleteCallback = callback;
    }

    public Dashboard(MainFrame parent) {
        setLayout(null);    
        setOpaque(false); 
        setPreferredSize(new Dimension(1280, 720));

        String svg = "src\\display_components\\Sidebar.svg";
        panel[0] = DisplayHelper.parsingSvg(svg, 0, 0, 375, 720);
        
        String logoIcon = ("src\\display_components\\AppLogo.svg");
        logo = DisplayHelper.parsingSvg(logoIcon, 20, 44, 284, 76);

        ImageIcon panelIcon = new ImageIcon("src\\display_components\\UpperPanel.png");
        panel[1] = DisplayHelper.parsingImg(panelIcon, 420, 34, 817, 392);

        ImageIcon statIcon5 = new ImageIcon("src\\display_components\\RecentLog.png");
        panel[2] = DisplayHelper.parsingImg(statIcon5,420, 440, 817, 251); 

        String svg3 = "src\\display_components\\Dashboard_Clicked.svg";
        panel[3] = DisplayHelper.parsingSvg(svg3, 32, 187, 301, 47);

        String svg4 = "src\\display_components\\Daily_Logs_Not_Clicked.svg";
        button[0] = DisplayHelper.buttonSvg(svg4, 43, 287, 301, 47);
        button[0].addActionListener(e -> parent.switchPage("DailyLogs"));

        String svg5 = "src\\display_components\\Analytics_NotClicked.svg";
        button[1] = DisplayHelper.buttonSvg(svg5, 43, 387, 301, 47);
        button[1].addActionListener(e -> parent.switchPage("Analytics"));

        String svg6 = "src\\display_components\\Add_NotClicked.svg";
        button[2] = DisplayHelper.buttonSvg(svg6, 43, 487, 301, 47);
        button[2].addActionListener(e -> parent.switchPage("AddBatch"));

        String svg7 = "src\\display_components\\Bi_NotClicked.svg";
        button[3] = DisplayHelper.buttonSvg(svg7, 43, 587, 301, 47);
        button[3].addActionListener(e -> parent.switchPage("BiWeeklySample"));

        String svg8 = "src\\display_components\\Calculate.svg";
        button[4] = DisplayHelper.buttonSvg(svg8, 430, 635, 250, 42);
        button[4].addActionListener(e -> {
            if(selectedBatch == null){
                JOptionPane.showMessageDialog(this, "Please select an active batch!");
                return;
            }
            try{
                double selectedTemp = 1.0;
                    for (JRadioButton rb : tempButton) {
                        if (rb.isSelected() && rb.getClientProperty("percentage_value") != null) {
                            selectedTemp = (double) rb.getClientProperty("percentage_value");
                            break;
                        }
                    }

                    double selectedWeather = 1.0;
                    for (JRadioButton rb : weatherButton) {
                        if (rb.isSelected() && rb.getClientProperty("percentage_value") != null) {
                            selectedWeather = (double) rb.getClientProperty("percentage_value");
                            break; 
                        }
                    }
                double ration = mathEngine.calculateRecommendedRation(selectedBatch, selectedTemp, selectedWeather);
                label[6].setText("Recommended Ration: " + String.format("%.2f kg", ration));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
        });
            
        String svg9 = "src\\display_components\\ClearLog.svg";
        button[5] = DisplayHelper.buttonSvg(svg9, 980, 635, 250, 42);
        button[5].addActionListener(e -> {
            label[2].setText("Estimated Biomass:");
            label[3].setText("Target Rate:");
            label[6].setText("Recommended Ration: "); 
            tempButton[0].setSelected(true);
            weatherButton[0].setSelected(true);
        });

        batchesContainer = new JPanel();
        batchesContainer.setLayout(new BoxLayout(batchesContainer, BoxLayout.Y_AXIS));
        batchesContainer.setOpaque(false);

        scrollPane = new JScrollPane(batchesContainer);
        scrollPane.setBounds(440,124,776,288);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        loadActiveBatches();

        ButtonGroup waterTempGroup = new ButtonGroup();
        ButtonGroup weatherGroup = new ButtonGroup();

        tempButton[0] = DisplayHelper.jRadioButton("26°C - 32°C", waterTempGroup, null, true, 950, 510, 147, 28, 1.0);
        tempButton[1] = DisplayHelper.jRadioButton("> 33°C", waterTempGroup, null, false, 1086, 510, 104, 28, 0.8);       
        tempButton[2] = DisplayHelper.jRadioButton("< 23°C", waterTempGroup, null, false, 950, 540, 104, 28, 0.6);

        weatherButton[0] = DisplayHelper.jRadioButton("Sunny", weatherGroup, null, true, 950, 570, 104, 28, 1.0);
        weatherButton[1] = DisplayHelper.jRadioButton("Cloudy", weatherGroup, null, false, 1086, 570, 110, 28, 0.9);
        weatherButton[2] = DisplayHelper.jRadioButton("Rainy", weatherGroup, null, false, 950, 600, 104, 28, 0.7);

        //Textfield Labels
        label[0] = DisplayHelper.fieldLabel(this,"Active Batches:", 24, 440, 64, 203, 28);
        label[1] = DisplayHelper.fieldLabel(this, "Today's Smart Recommendation", 24, 440, 450, 410, 28);
        label[2] = DisplayHelper.fieldLabel(this, "Estimated Biomass: ", 20, 440, 510, 338, 28);
        label[3] = DisplayHelper.fieldLabel(this, "Target Rate:", 20, 440, 570, 323, 28);
        label[4] = DisplayHelper.fieldLabel(this, "Water Temp:", 20, 830, 510, 203, 28);
        label[5] = DisplayHelper.fieldLabel(this, "Weather:", 20, 830, 570, 203, 28);
        label[6] = DisplayHelper.fieldLabel(this, "Recommended Ration: ", 24, 440, 600, 371, 28);


        for(JRadioButton temp: tempButton){
            this.add(temp);
        }
        for(JRadioButton weather: weatherButton){
            this.add(weather);
        }
        for(JLabel l:label){
            this.add(l);
        }
        for(JButton b:button){
            this.add(b);
        }
        this.add(logo);
        for(int i = panel.length - 1; i >= 0; i--){
            this.add(panel[i]);
        }
    }

    private JPanel createBatchRow(FishBatch batch){
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(null);
        rowPanel.setPreferredSize(new Dimension(756,62));
        rowPanel.setMaximumSize(new Dimension(756,62));
        rowPanel.setMinimumSize(new Dimension(756,62));
        rowPanel.setOpaque(false);

        rowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        SamplingRecordDAO recordDAO = new SamplingRecordDAO();
        rowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (selectedPanel != null) {
                    selectedPanel.setBorder(null);
                }

                selectedPanel = rowPanel;
                selectedPanel.setBorder(BorderFactory.createLineBorder(new Color(0xFB3F3F), 10, true));
                selectedPanel.repaint();
                
                selectedBatch = batch; 
                
                double currentWeight;

                try {
                    FeedingLogDAO feedingLogDAO = new FeedingLogDAO();
                    SamplingRecord latest = recordDAO.getLatestSample(selectedBatch.getId());
            
                    currentWeight = (latest != null) ? latest.getAvgWeightSample() : selectedBatch.getAvgWeightPerSample();
            
                    label[2].setText("Estimated Biomass: " + String.format("%.2f kg", selectedBatch.estimateBiomass(currentWeight, LocalDate.now(), feedingLogDAO.getTotalMortalityByBatch(selectedBatch.getId()))));
                    label[3].setText("Target Rate: " + String.format("%.0f%% of Body Weight", selectedBatch.getTargetFeedingRatePercentage() * 100));
                    label[6].setText("Recommended Ration: ");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }});

        double currentWeightToDisplay = batch.getAvgWeightPerSample(); 
        try {
            SamplingRecord latest = recordDAO.getLatestSample(batch.getId());
            if (latest != null) {
                currentWeightToDisplay = latest.getAvgWeightSample();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
            
        double progress = mathEngine.progressValue(batch.getTargetWeight(), batch.getAvgWeightPerSample(), currentWeightToDisplay);

        JLabel pondName = DisplayHelper.tableLabel(batch.getPondName(), 20, 20, 15, 150, 28);
        JLabel species = DisplayHelper.tableLabel("Bangus", 20, 150, 15, 150, 28);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) progress); 
        
        progressBar.setStringPainted(true); 
        progressBar.setString(String.format("%.1f%%", progress)); 
        
        progressBar.setForeground(new Color(0x28A745)); 
        progressBar.setBackground(new Color(0xE9ECEF)); 
        progressBar.setBorderPainted(false);
        progressBar.setFont(new Font("Poppins", Font.BOLD, 14));
        
        progressBar.setBounds(300, 18, 150, 24);

        String svg10 = "src\\display_components\\EditButt.svg";
        JButton buttonHarvest = DisplayHelper.buttonSvg(svg10, 550, 13, 50, 40);
        buttonHarvest.addActionListener(e -> {
            if (onHarvestCallback != null) {
                onHarvestCallback.accept(batch);
            }
        });

        String svg11 = "src\\display_components\\DelButt.svg";
        JButton buttonDelete = DisplayHelper.buttonSvg(svg11, 630, 13, 50, 40);
        buttonDelete.addActionListener(e -> {
            if (onDeleteCallback != null) {
                onDeleteCallback.accept(batch);
            }
        });

        ImageIcon statIcon = new ImageIcon("src\\display_components\\ActivePanel.png");
        JLabel bgImage = DisplayHelper.parsingImg(statIcon, 0, 0, 756, 62);

        rowPanel.add(pondName);
        rowPanel.add(species);
        rowPanel.add(progressBar);
        rowPanel.add(buttonHarvest);
        rowPanel.add(buttonDelete);
        rowPanel.add(bgImage);

        return rowPanel;

    } 

    public void loadActiveBatches(){
        try {
            List<FishBatch> activeBatches = batchDAO.getActiveBatches();

            batchesContainer.removeAll();

            for(FishBatch batch: activeBatches){
                JPanel row = createBatchRow(batch);
                batchesContainer.add(row);

                batchesContainer.add(Box.createRigidArea(new Dimension(0,8)));
            }
            batchesContainer.revalidate();
            batchesContainer.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to load active batches: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

}