package view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.FishBatch;
import util.DisplayHelper;
import util.ValidateInput;

public class AddBatch extends JPanel{
    private JLabel label[] = new JLabel[8];
    private JLabel panel[] = new JLabel[4];
    private JLabel txtLabel[] = new JLabel[6];
    private JButton button[] = new JButton[6];
    private JTextField txt[] = new JTextField[4];
    private JLabel txtDate, logo;

    public AddBatch(MainFrame parent){
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
        panel[2] = DisplayHelper.parsingImg(panelIcon2, 490, 224, 673, 266);

        String svg2 = "src\\display_components\\AddBatchClicked.svg";
        panel[3] = DisplayHelper.parsingSvg(svg2, 32, 487, 301, 47);

        String svg3 = "src\\display_components\\DashboardNotClicked.svg";
        button[0] = DisplayHelper.buttonSvg(svg3, 43, 187, 311, 47);
        button[0].addActionListener(e -> parent.switchPage("Dashboard"));

        String svg4 = "src\\display_components\\Daily_Logs_Not_Clicked.svg";
        button[1] = DisplayHelper.buttonSvg(svg4, 43, 287, 311, 47);
        button[1].addActionListener(e -> parent.switchPage("DailyLogs"));

        String svg5 = "src\\display_components\\Analytics_NotClicked.svg";
        button[2] = DisplayHelper.buttonSvg(svg5, 43, 387, 301, 47);
        button[2].addActionListener(e -> parent.switchPage("Analytics"));

        String svg6 = "src\\display_components\\Bi_NotClicked.svg";
        button[3] = DisplayHelper.buttonSvg(svg6, 43, 587, 301, 47);
        button[3].addActionListener(e -> parent.switchPage("BiWeeklySample"));

        String svg7 = "src\\display_components\\SaveButton.svg";
        button[4] = DisplayHelper.buttonSvg(svg7, 430, 505, 250, 42);
        button[4].addActionListener(e -> {
            //button[4].setEnabled(false);
        });

        String svg8 = "src\\display_components\\ClearLog.svg";
        button[5] = DisplayHelper.buttonSvg(svg8, 980, 505, 250, 42);
        button[5].addActionListener(e -> clearField());

        label[0] = DisplayHelper.fieldLabel(this,"Create New Pond Batch", 24, 695, 94, 322, 28);
        label[1] = DisplayHelper.fieldLabel(this,"Batch Name:", 20, 490, 175, 322, 28);
        label[2] = DisplayHelper.fieldLabel(this,"Species:", 20, 885, 175, 322, 28);
        label[3] = DisplayHelper.fieldLabel(this, "Bangus", 20, 995, 175, 322, 28);
        label[3].setForeground(new Color(0x000404));
        label[4] = DisplayHelper.fieldLabel(this,"Stock Date:", 20, 510, 239, 322, 28);
        label[4].setForeground(new Color(0x000404));
        label[5] = DisplayHelper.fieldLabel(this,"Total Weight(grams):", 20, 510, 309, 322, 28);
        label[5].setForeground(new Color(0x000404));
        label[6] = DisplayHelper.fieldLabel(this,"Stock Count:", 20, 875, 239, 322, 28);
        label[6].setForeground(new Color(0x000404));
        label[7] = DisplayHelper.fieldLabel(this,"Target Weight(grams):", 20, 875, 309, 322, 28);
        label[7].setForeground(new Color(0x000404));

        txtDate = DisplayHelper.fieldLabel(this,null, 18, 515, 274, 115, 28);
        txtDate.setForeground(new Color(0x000404));
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        txtDate.setText(today.format(formatter));

        //Textfield Appearance
        String textSvg = "src\\display_components\\AddBatchTxt2.svg";
        txtLabel[0] = DisplayHelper.parsingSvg(textSvg, 620, 175, 177, 37);
        String textSvg2 = "src\\display_components\\AddBatchTxt2.svg";
        txtLabel[1] = DisplayHelper.parsingSvg(textSvg2, 990, 175, 177, 37);
        String textSvg3 = "src\\display_components\\AddBatchTxt.svg";
        txtLabel[2] = DisplayHelper.parsingSvg(textSvg3, 510, 269, 259, 37);        
        String textSvg4 = "src\\display_components\\AddBatchTxt.svg";
        txtLabel[3] = DisplayHelper.parsingSvg(textSvg4, 875, 269, 259, 37);        
        String textSvg5 = "src\\display_components\\AddBatchTxt.svg";
        txtLabel[4] = DisplayHelper.parsingSvg(textSvg5, 510, 349, 259, 37);
        String textSvg6 = "src\\display_components\\AddBatchTxt.svg";
        txtLabel[5] = DisplayHelper.parsingSvg(textSvg6, 875, 349, 259, 37);    

        //Textfield
        txt[0] = DisplayHelper.textField(625, 175, 172, 37);
        txt[1] = DisplayHelper.textField(880, 269, 254, 37);
        txt[1].setForeground(new Color(0x000404));
        txt[2] = DisplayHelper.textField(515, 349, 254, 37);
        txt[2].setForeground(new Color(0x000404));
        txt[3] = DisplayHelper.textField(880, 349, 254, 37);
        txt[3].setForeground(new Color(0x000404));

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

    public JButton getSaveButton(){
        return button[4];
    }

    public FishBatch getBatchData(){
        String inputString[] = {txt[0].getText(),txt[1].getText(),txt[2].getText(),txt[3].getText()};
    
        boolean isValid = ValidateInput.validateInput(inputString);

        if(!isValid){
            return null;
        }

            try{
                String batchName = inputString[0];
                int stockCount = Integer.parseInt(inputString[1]);
                double totalWeight = Double.parseDouble(inputString[2]);
                double targetWeight = Double.parseDouble(inputString[3]);
                double avgWeightPerSample = totalWeight/stockCount;

                return new FishBatch(batchName, LocalDate.now(), stockCount, avgWeightPerSample, targetWeight,"Active");
            } catch (NumberFormatException e) {
                return null;
            } catch (ArithmeticException e) {
                return null;
            }
    }

}
