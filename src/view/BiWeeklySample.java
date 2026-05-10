package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import java.awt.Cursor;

import controller.AppController;
import model.FishBatch;
import model.SamplingRecord;
import util.ComboItem;
import util.DisplayHelper;
import util.ValidateInput;

public class BiWeeklySample extends JPanel{
    private JLabel label[] = new JLabel[7];
    private JLabel txtLabel[] = new JLabel[5];
    private JLabel panel[] = new JLabel[6];
    private JTextField txt[] = new JTextField[3];
    private JButton button[] = new JButton[9];
    private JLabel txtDate, logo;
    private JPanel batchesContainer;
    private JScrollPane scrollPane;
    private JComboBox<ComboItem> cbBatchName;
    private AppController controller;
    private MainFrame parent;
    private Consumer<SamplingRecord> onEditCallback;

    public void setOnEdit(Consumer<SamplingRecord> callback){
        this.onEditCallback = callback;
    }

    public BiWeeklySample (MainFrame parent){
        this.parent = parent;

        setLayout(null);
        setOpaque(false); 
        setPreferredSize(new Dimension(1280, 720));

        panel[0] = DisplayHelper.parsingSvg("/resources/images/Sidebar.svg", 0, 0, 375, 720);
        logo = DisplayHelper.parsingSvg("/resources/images/AppLogo.svg", 20, 44, 284, 76);

        panel[1] = DisplayHelper.parsingImg("/resources/images/AddLogPane.png", 420, 84, 817, 531);
        panel[2] = DisplayHelper.parsingImg("/resources/images/InputPanelX.png", 490, 254, 673, 266);
        panel[3] = DisplayHelper.parsingSvg("/resources/images/Bi_Clicked.svg", 32, 587, 301, 47);

        panel[4] = DisplayHelper.parsingImg("/resources/images/Prev_Samp.png", 420, 84, 817, 49);
        panel[5] = DisplayHelper.parsingSvg("/resources/images/Search_Bar.svg", 955, 90, 225, 37);
        for(int i = 4; i <= 5; i++) panel[i].setVisible(false);

        button[0] = DisplayHelper.setupSidebar(parent, "/resources/images/DashboardNotClicked.svg", 187, "Dashboard");
        button[1] = DisplayHelper.setupSidebar(parent, "/resources/images/Daily_Logs_Not_Clicked.svg", 287, "DailyLogs");
        button[2] = DisplayHelper.setupSidebar(parent, "/resources/images/Analytics_NotClicked.svg", 387, "Analytics");
        button[3] = DisplayHelper.setupSidebar(parent, "/resources/images/Add_NotClicked.svg", 487, "AddBatch");

        button[4] = DisplayHelper.buttonSvg("/resources/images/SaveLog.svg", 430, 555, 250, 42);
        button[4].addActionListener(e -> {
            button[4].setEnabled(false);
            Timer timer = new Timer(3000, event -> {
                button[4].setEnabled(true);
            });
            timer.setRepeats(false);
            timer.start();
        });

        button[5] = DisplayHelper.buttonSvg("/resources/images/ClearLog.svg", 980, 555, 250, 42);
        button[5].addActionListener(e -> clearField());

        button[6] = DisplayHelper.buttonSvg("/resources/images/Add_Sampling.svg", 420, 44, 204, 49);
        button[6].addActionListener(e -> toggleViewMode(false));

        button[7] = DisplayHelper.buttonSvg("/resources/images/History_Butt.svg", 600, 44, 204, 49);
        button[7].addActionListener(e -> toggleViewMode(true));

        button[8] = new JButton();
        button[8].setBounds(955, 90, 37, 37);
        button[8].setOpaque(false);
        button[8].setContentAreaFilled(false);
        button[8].setBorderPainted(false);
        button[8].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button[8].addActionListener(e -> {
            String searchBatch = txt[2].getText().replaceAll("\\s+", "");
            if (searchBatch.isEmpty()) {
                loadSamplingHistory();
                return; 
            }
            String finalSearch = searchBatch.substring(0, 1).toUpperCase() + searchBatch.substring(1);
            searchRecord(finalSearch);
        });

        label[0] = DisplayHelper.fieldLabel(this,"Bi-Weekly Sampling Entry", 24, 695, 94, 322, 28);
        label[1] = DisplayHelper.fieldLabel(this,"Batch Name:", 20, 480, 175, 322, 28);
        label[2] = DisplayHelper.fieldLabel(this,"Species:", 20, 875, 175, 322, 28);
        label[3] = DisplayHelper.fieldLabel(this, "Bangus", 20, 985, 175, 322, 28);
        label[3].setForeground(new Color(0x000404));
        label[4] = DisplayHelper.fieldLabel(this, "Stock Date:", 20, 535, 280, 322, 28);
        label[4].setForeground(new Color(0x000404));
        label[5] = DisplayHelper.fieldLabel(this, "Number of Fish:", 20, 535, 350, 322, 28);
        label[5].setForeground(new Color(0x000404));
        label[6] = DisplayHelper.fieldLabel(this, "Total Weight(grams):", 20, 535, 420, 322, 28);
        label[6].setForeground(new Color(0x000404));

        txtDate = DisplayHelper.fieldLabel(this,null, 18, 540, 315, 115, 28);
        txtDate.setForeground(new Color(0x000404));
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtDate.setText(today.format(formatter));

        cbBatchName = DisplayHelper.jComboBox(610, 175, 177, 37);
        this.add(cbBatchName);
        this.setComponentZOrder(cbBatchName, 0);

        //TextField Appearance
        txtLabel[0] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt2.svg", 610, 175, 177, 37);
        txtLabel[1] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt2.svg", 980, 175, 177, 37);
        txtLabel[2] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxtX.svg", 535, 310, 568, 37);
        txtLabel[3] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxtX.svg", 535, 380, 568, 37);
        txtLabel[4] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxtX.svg", 535, 450, 568, 37);

        //Text appearance
        txt[0] = DisplayHelper.textField(540, 380, 563, 37);
        txt[0].setForeground(new Color(0x000404));
        txt[1] = DisplayHelper.textField(540, 450, 563, 37);
        txt[1].setForeground(new Color(0x000404));
        txt[2] = DisplayHelper.textField(995, 90, 185, 37);
        txt[2].setForeground(new Color(0x000404));
        txt[2].setEditable(false);

        batchesContainer = new JPanel();
        batchesContainer.setLayout(new BoxLayout(batchesContainer,BoxLayout.Y_AXIS));
        batchesContainer.setOpaque(false);
        
        scrollPane = DisplayHelper.scrollPane(batchesContainer, 440, 204, 770, 420);

        for(JTextField t:txt){
            this.add(t);
            this.setComponentZOrder(t, 0);
        }
        for(JLabel t:txtLabel){
            this.add(t);
        }
        for(JButton b:button){
            this.add(b);
        }
        this.add(logo);
        this.add(scrollPane);

        for(int i = panel.length - 1; i >= 0; i--){
            this.add(panel[i]);
        }
    }

    private void toggleViewMode(boolean isHistory){
        if(!isHistory){
            parent.switchPage("BiWeeklySample");
        }

        for(int i = 4; i <= 5; i++) panel[i].setVisible(isHistory);
        panel[2].setVisible(!isHistory);
        cbBatchName.setVisible(!isHistory);
        scrollPane.setVisible(isHistory);
        txtDate.setVisible(!isHistory);
        txt[2].setVisible(isHistory);
        txt[2].setEditable(isHistory);
        txt[2].addActionListener(e -> button[8].doClick());
        button[8].setVisible(isHistory);

        for (JLabel t : txtLabel) t.setVisible(!isHistory);
        for (int i = 0; i < 2; i++) {
            txt[i].setEditable(!isHistory);
            txt[i].setVisible(!isHistory);
        }
        for (int i = 4; i <= 6; i++) label[i].setVisible(!isHistory);
        for (int i = 4; i <= 5; i++) button[i].setVisible(!isHistory);

        if (isHistory) {
            label[0].setText("Previous Samplings");
            label[0].setBounds(695, 94, 249, 28);
            label[1].setText("Sample Date");
            label[1].setBounds(480, 175, 322, 28); 
            label[2].setText("Batch Name");
            label[2].setBounds(680, 175, 322, 28); 
            label[3].setText("Avg. Weight(g)");
            label[3].setBounds(880, 175, 322, 28);
            label[3].setForeground(new Color(0xFFFFFF));
            loadSamplingHistory();
        } else {
            label[0].setText("Bi-Weekly Sampling Entry");
            label[0].setBounds(695, 94, 322, 28);
            label[1].setText("Batch Name:");
            label[1].setBounds(480, 175, 322, 28); 
            label[2].setText("Species:");
            label[2].setBounds(875, 175, 322, 28);
            label[3].setText("Bangus");
            label[3].setBounds(985, 175, 322, 28);
            label[3].setForeground(new Color(0x000404));
        }

        repaint();
        revalidate();        
    }

    public void clearField(){
        for(JTextField t:txt){
            t.setText("");
        }
    }

    public JComboBox<ComboItem> getBatchesComboBox(){
        return cbBatchName;
    }

    private JPanel createSamplingRow(SamplingRecord sample){
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(null);
        rowPanel.setPreferredSize(new Dimension(756, 62));
        rowPanel.setMaximumSize(new Dimension(756, 62));
        rowPanel.setMinimumSize(new Dimension(756, 62)); 
        rowPanel.setOpaque(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateStr = sample.getSampleDate().format(formatter);

        JLabel lblDate = DisplayHelper.tableLabel(dateStr, 18, 38, 9, 120, 28);
        lblDate.setForeground(new Color(0xFFFFFF));
        String batchName = "Unknown";
        try {
            FishBatch batch = controller.getBatchDAO().getBatchById(sample.getBatchId());
            if(batch != null){
                batchName = batch.getPondName();
            } else {
                System.out.println("Batch Name not found");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JLabel lblBatch = DisplayHelper.tableLabel(batchName, 18, 250, 9, 200, 28);
        lblBatch.setForeground(new Color(0xFFFFFF));
        JLabel lblWeight = DisplayHelper.tableLabel(String.format("%.1fg", sample.getAvgWeightSample()), 18, 470, 9, 100, 28);
        lblWeight.setForeground(new Color(0xFFFFFF));

        JButton editButton = DisplayHelper.buttonSvg("/resources/images/EditButt.svg", 550, 5, 50, 40);
        editButton.addActionListener(e -> {
            if (onEditCallback != null) {
                onEditCallback.accept(sample);
            }
        });

        rowPanel.add(lblDate);
        rowPanel.add(lblBatch);
        rowPanel.add(lblWeight);
        rowPanel.add(editButton);

        return rowPanel;
    }

    public void loadSamplingHistory(){
        try {
            List<SamplingRecord> records = controller.getRecordDAO().getSamplingRecords();

            batchesContainer.removeAll();

            for(SamplingRecord record: records){
                JPanel row = createSamplingRow(record);
                
                batchesContainer.add(row);
                batchesContainer.add(Box.createRigidArea(new Dimension(0,5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to load recent logs: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchRecord(String searchBatch){
        try {
            List<SamplingRecord> records = controller.getRecordDAO().searchRecords(searchBatch);
            batchesContainer.removeAll();

            if (records == null || records.isEmpty()) {
                JLabel emptyLabel = DisplayHelper.fieldLabel(batchesContainer, "No records found", 20,695, 550, 322, 28);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
                batchesContainer.add(Box.createRigidArea(new Dimension(0, 100)));
                batchesContainer.add(emptyLabel);
            } else {
                for(SamplingRecord record:records){
                    JPanel row = createSamplingRow(record);
                    batchesContainer.add(row);
                    batchesContainer.add(Box.createRigidArea(new Dimension(0,5)));
                }  
            }
            batchesContainer.revalidate();
            batchesContainer.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setController(AppController controller){
        this.controller = controller;
    }

    public JButton getSaveButton(){
        return button[4];
    }

    public SamplingRecord getSamplingRecordData(){
        ComboItem selectedBatch = (ComboItem) cbBatchName.getSelectedItem();

        if (selectedBatch == null) {
            JOptionPane.showMessageDialog(this, "Please select a Batch.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String inputString[] = {txt[0].getText(),txt[1].getText()};
        boolean isValid = ValidateInput.validateInput(inputString);

        if(!isValid){
            return null;
        }
        
        try {
            int batchId = selectedBatch.getId();
            double sampleSize = Double.parseDouble(txt[0].getText());
            double totalWeight = Double.parseDouble(txt[1].getText());
            double avgWeightPerSample = totalWeight/sampleSize;

            return new SamplingRecord(batchId,LocalDate.now(),avgWeightPerSample);
        } catch (NumberFormatException e) {
            return null;
        } catch (ArithmeticException e) {
            return null;
        }
    }
}