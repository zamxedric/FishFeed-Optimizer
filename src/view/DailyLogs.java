package view;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;

import dao.FeedingLogDAO;

import model.DailyFeedLog;
import util.ComboItem;
import util.DisplayHelper;
import util.ValidateInput;

public class DailyLogs extends JPanel{
    private JLabel logo, txtDate;
    private JLabel label[] = new JLabel[12];
    private JLabel panel[] = new JLabel[4];
    private JLabel txtLabel[] = new JLabel[5];
    private JTextField txt[] = new JTextField[4];
    private JButton button[] = new JButton[6];
    private JScrollPane scrollPane;
    private JPanel batchesContainer;
    private FeedingLogDAO feedLogDAO = new FeedingLogDAO();
    private JComboBox<ComboItem> cbBatchName;

    public DailyLogs(MainFrame parent){
        setLayout(null);
        setOpaque(false); 
        setPreferredSize(new Dimension(1280, 720));
        
        String svg = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\Sidebar.svg";
        panel[0] = DisplayHelper.parsingSvg(svg, 0, 0, 375, 720);

        String logoIcon = ("C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\AppLogo.svg");
        logo = DisplayHelper.parsingSvg(logoIcon, 20, 44, 284, 76);

        ImageIcon panelIcon = new ImageIcon("C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\AddLog.png");
        panel[1] = DisplayHelper.parsingImg(panelIcon, 420, 34, 817, 348);

        ImageIcon panel2Icon = new ImageIcon("C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\RecentLog.png");
        panel[2] = DisplayHelper.parsingImg(panel2Icon, 420, 440, 817, 251);

        String svg2 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\DailyLogsClicked.svg";
        panel[3] = DisplayHelper.parsingSvg(svg2, 32, 287, 301, 47);

        String svg3 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\DashboardNotClicked.svg";
        button[0] = DisplayHelper.buttonSvg(svg3, 43, 187, 301, 47);
        button[0].addActionListener(e -> parent.switchPage("Dashboard"));

        String svg4 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\Analytics_NotClicked.svg";
        button[1] = DisplayHelper.buttonSvg(svg4, 43, 387, 301, 47);
        button[1].addActionListener(e -> parent.switchPage("Analytics"));

        String svg5 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\Add_NotClicked.svg";
        button[2] = DisplayHelper.buttonSvg(svg5, 43, 487, 301, 47);
        button[2].addActionListener(e -> parent.switchPage("AddBatch"));

        String svg6 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\Bi_NotClicked.svg";
        button[3] = DisplayHelper.buttonSvg(svg6, 43, 587, 301, 47);
        button[3].addActionListener(e -> parent.switchPage("BiWeeklySample"));

        String svg7 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\SaveLog.svg";
        button[4] = DisplayHelper.buttonSvg(svg7, 430, 390, 250, 42);
        button[4].addActionListener(e -> {
            button[4].setEnabled(false);
            Timer timer = new Timer(5000, event -> {
                button[4].setEnabled(true);
            });
            timer.setRepeats(false);
            timer.start();
        });

        String svg8 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\ClearLog.svg";
        button[5] = DisplayHelper.buttonSvg(svg8, 980, 390, 250, 42);
        button[5].addActionListener(e -> clearField());

        label[0] = DisplayHelper.fieldLabel(this,"Date: ", 20, 440, 44, 225, 28);
        label[1] = DisplayHelper.fieldLabel(this,"Feed Input and Environmental Data ", 24, 440, 94, 455, 28);
        label[2] = DisplayHelper.fieldLabel(this,"Batch Name:", 20, 540, 144, 225, 28);
        label[3] = DisplayHelper.fieldLabel(this,"Amount given(kg):", 20, 870, 144, 203, 28);
        label[4] = DisplayHelper.fieldLabel(this,"Feed Cost per kg:", 20, 540, 224, 203, 28);
        label[5] = DisplayHelper.fieldLabel(this,"Water temp:", 20, 870, 224, 203, 28);
        label[6] = DisplayHelper.fieldLabel(this, "Mortality", 20, 710, 304, 203, 28);

        label[7] = DisplayHelper.fieldLabel(this,"Recent Logs", 24, 440, 450, 203, 28);
        label[8] = DisplayHelper.fieldLabel(this,"Date", 20, 440, 505, 96, 28);
        label[9] = DisplayHelper.fieldLabel(this,"Batch Name", 20, 600, 505, 135, 28);
        label[10] = DisplayHelper.fieldLabel(this,"Feed(kg)", 20, 830, 505, 96, 28);
        label[11] = DisplayHelper.fieldLabel(this,"Water Temp", 20, 1030, 505, 135, 28);

        txtDate = DisplayHelper.fieldLabel(this,null, 20, 500, 44, 115, 28);
        txtDate.setForeground(new Color(0xFFFFFF));
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtDate.setText(today.format(formatter));

        txt[0] = DisplayHelper.textField(875, 175, 215, 37);
        txt[1] = DisplayHelper.textField(545, 255, 215, 37);
        txt[2] = DisplayHelper.textField(875, 255, 215, 37);
        txt[3] = DisplayHelper.textField(715, 335, 215, 37);

        cbBatchName = DisplayHelper.jComboBox(545, 175, 215, 37);
        this.add(cbBatchName);
        this.setComponentZOrder(cbBatchName, 0);


        //Textfield Appearance
        String textSvg = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\TextField.svg";
        txtLabel[0] = DisplayHelper.parsingSvg(textSvg, 540, 175, 225, 37);
        String textSvg2 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\TextField.svg";
        txtLabel[1] = DisplayHelper.parsingSvg(textSvg2, 870, 175, 225, 37);
        String textSvg3 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\TextField.svg";
        txtLabel[2] = DisplayHelper.parsingSvg(textSvg3, 540, 255, 225, 37);
        String textSvg4 = "C:\\Users\\user\\Documents\\Java Projects\\Fish Yield Forecaster\\Fish Yield Forecaster\\src\\display_components\\TextField.svg";
        txtLabel[3] = DisplayHelper.parsingSvg(textSvg4, 870, 255, 225, 37);
        String textSvg5 = "src\\display_components\\TextField.svg";
        txtLabel[4] = DisplayHelper.parsingSvg(textSvg5, 710, 335, 225, 37);

        batchesContainer = new JPanel();
        batchesContainer.setLayout(new BoxLayout(batchesContainer,BoxLayout.Y_AXIS));
        batchesContainer.setOpaque(false);
        
        scrollPane = new JScrollPane(batchesContainer);
        scrollPane.setBounds(440,540, 770, 140);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        loadRecentLogs();
        this.add(scrollPane);
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
        for(int i = panel.length - 1; i >= 0; i--){
            this.add(panel[i]);
        }
    }

    private JPanel createLogRow(DailyFeedLog log) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(null);
        
        rowPanel.setPreferredSize(new Dimension(750, 35));
        rowPanel.setMaximumSize(new Dimension(750, 35));
        rowPanel.setMinimumSize(new Dimension(750, 35)); 
        rowPanel.setOpaque(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateStr = log.getLogDate().format(formatter);

        JLabel lblDate = DisplayHelper.tableLabel(dateStr, 18, 0, 5, 120, 28);
        lblDate.setForeground(new Color(0xFFFFFF));
        JLabel lblBatch = DisplayHelper.tableLabel(log.getBatchName(), 18, 160, 5, 200, 28);
        lblBatch.setForeground(new Color(0xFFFFFF));
        JLabel lblFeed = DisplayHelper.tableLabel(String.format("%.1f", log.getFeedGivenKg()), 18, 390, 5, 100, 28);
        lblFeed.setForeground(new Color(0xFFFFFF));
        JLabel lblTemp = DisplayHelper.tableLabel(String.format("%.1f °C", log.getWaterTemp()), 18, 590, 5, 120, 28);
        lblTemp.setForeground(new Color(0xFFFFFF));

        rowPanel.add(lblDate);
        rowPanel.add(lblBatch);
        rowPanel.add(lblFeed);
        rowPanel.add(lblTemp);

        return rowPanel;
    }

    public void loadRecentLogs(){
        try {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            List<DailyFeedLog> logs = feedLogDAO.getRecentLogs(today, yesterday);

            batchesContainer.removeAll();

            for(DailyFeedLog log: logs){
                JPanel row = createLogRow(log);
                batchesContainer.add(row);

                batchesContainer.add(Box.createRigidArea(new Dimension(0,5)));
            }

            batchesContainer.revalidate();
            batchesContainer.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Failed to load recent logs: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public JComboBox<ComboItem> getBatchComboBox(){
        return cbBatchName;
    }

    public JButton getSaveButton() {
        return button[4];
    }

    public DailyFeedLog getLogData() {
        ComboItem selectedBatch = (ComboItem) cbBatchName.getSelectedItem();

        if (selectedBatch == null) {
            JOptionPane.showMessageDialog(this, "Please select a Batch.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String inputString[] = {txt[0].getText(),txt[1].getText(),txt[2].getText(), txt[3].getText()};
    
        boolean isValid = ValidateInput.validateInput(inputString);

        if(!isValid){
            return null;
        }

            try {
                int batchId = selectedBatch.getId(); 
                String batchName = selectedBatch.getName();
                double feedAmount = Double.parseDouble(inputString[0]);
                double feedCost = Double.parseDouble(inputString[1]);
                double temp = Double.parseDouble(inputString[2]);
                int mortality = Integer.parseInt(inputString[3]);

                return new DailyFeedLog(0, batchId, batchName, LocalDate.now(), feedAmount, feedCost, temp, mortality);
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public void clearField(){
        for(JTextField t:txt){
            t.setText("");
        }

        if (cbBatchName.getItemCount() > 0) {
            cbBatchName.setSelectedIndex(0);
        }
    }
}
