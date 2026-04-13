package view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.SamplingRecord;
import util.ComboItem;
import util.DisplayHelper;
import util.ValidateInput;

public class BiWeeklySample extends JPanel{
    private JLabel label[] = new JLabel[7];
    private JLabel txtLabel[] = new JLabel[5];
    private JLabel panel[] = new JLabel[4];
    private JTextField txt[] = new JTextField[2];
    private JButton button[] = new JButton[6];
    private JLabel txtDate, logo;
    private JComboBox<ComboItem> cbBatchName;

    public BiWeeklySample (MainFrame parent){
        setLayout(null);
        setOpaque(false); 
        setPreferredSize(new Dimension(1280, 720));

        String svg = "src\\display_components\\Sidebar.svg";
        panel[0] = DisplayHelper.parsingSvg(svg, 0, 0, 375, 720);

        String logoIcon = ("src\\display_components\\AppLogo.svg");
        logo = DisplayHelper.parsingSvg(logoIcon, 20, 44, 284, 76);

        ImageIcon panelIcon = new ImageIcon("src\\display_components\\AddLogPane.png");
        panel[1] = DisplayHelper.parsingImg(panelIcon, 420, 84, 817, 531);

        ImageIcon panelIcon2 = new ImageIcon("src\\display_components\\InputPanelX.png");
        panel[2] = DisplayHelper.parsingImg(panelIcon2, 490, 254, 673, 266);

        String svg2 = "src\\display_components\\Bi_Clicked.svg";
        panel[3] = DisplayHelper.parsingSvg(svg2, 32, 587, 301, 47);

        String svg3 = "src\\display_components\\DashboardNotClicked.svg";
        button[0] = DisplayHelper.buttonSvg(svg3, 43, 187, 301, 47);
        button[0].addActionListener(e -> parent.switchPage("Dashboard"));

        String svg4 = "src\\display_components\\Daily_Logs_Not_Clicked.svg";
        button[1] = DisplayHelper.buttonSvg(svg4, 43, 287, 301, 47);
        button[1].addActionListener(e -> parent.switchPage("DailyLogs"));

        String svg5 = "src\\display_components\\Analytics_NotClicked.svg";
        button[2] = DisplayHelper.buttonSvg(svg5, 43, 387, 301, 47);
        button[2].addActionListener(e -> parent.switchPage("Analytics"));

        String svg6 = "src\\display_components\\Add_NotClicked.svg";
        button[3] = DisplayHelper.buttonSvg(svg6, 43, 487, 301, 47);
        button[3].addActionListener(e -> parent.switchPage("AddBatch"));

        String svg7 = "src\\display_components\\SaveLog.svg";
        button[4] = DisplayHelper.buttonSvg(svg7, 430, 555, 250, 42);
        button[4].addActionListener(e -> {
            button[4].setEnabled(false);
        });

        String svg8 = "src\\display_components\\ClearLog.svg";
        button[5] = DisplayHelper.buttonSvg(svg8, 980, 555, 250, 42);
        button[5].addActionListener(e -> clearField());

        label[0] = DisplayHelper.fieldLabel(this,"Bi-Weekly Sampling Entry", 24, 695, 94, 322, 28);
        label[1] = DisplayHelper.fieldLabel(this,"Batch Name:", 20, 480, 175, 322, 28);
        label[2] = DisplayHelper.fieldLabel(this,"Species:", 20, 875, 175, 322, 28);
        label[3] = DisplayHelper.fieldLabel(this, "Bangus", 20, 985, 175, 322, 28);
        label[3].setForeground(new Color(0x000404));
        label[4] = DisplayHelper.fieldLabel(this, "Stock Date:", 20, 535, 280, 322, 28);
        label[4].setForeground(new Color(0x000404));
        label[5] = DisplayHelper.fieldLabel(this, "Number of Fish:", 20, 535, 350, 322, 28);
        label[5].setForeground(new Color(0x000404));
        label[6] = DisplayHelper.fieldLabel(this, "Total Weight:", 20, 535, 420, 322, 28);
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
        String textSvg = "src\\display_components\\AddBatchTxt2.svg";
        txtLabel[0] = DisplayHelper.parsingSvg(textSvg, 610, 175, 177, 37);
        String textSvg2 = "src\\display_components\\AddBatchTxt2.svg";
        txtLabel[1] = DisplayHelper.parsingSvg(textSvg2, 980, 175, 177, 37);
        String textSvg3 = "src\\display_components\\AddBatchTxtX.svg";
        txtLabel[2] = DisplayHelper.parsingSvg(textSvg3, 535, 310, 568, 37);
        String textSvg4 = "src\\display_components\\AddBatchTxtX.svg";
        txtLabel[3] = DisplayHelper.parsingSvg(textSvg4, 535, 380, 568, 37);
        String textSvg5 = "src\\display_components\\AddBatchTxtX.svg";
        txtLabel[4] = DisplayHelper.parsingSvg(textSvg5, 535, 450, 568, 37);

        //TextField appearance
        txt[0] = DisplayHelper.textField(540, 380, 563, 37);
        txt[0].setForeground(new Color(0x000404));
        txt[1] = DisplayHelper.textField(540, 450, 563, 37);
        txt[1].setForeground(new Color(0x000404));

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
    public void clearField(){
        for(JTextField t:txt){
            t.setText("");
        }
    }

    public JComboBox<ComboItem> getBatchesComboBox(){
        return cbBatchName;
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
