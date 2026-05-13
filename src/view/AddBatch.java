package view;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

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
    
        panel[0] = DisplayHelper.parsingSvg("/resources/images/Sidebar.svg", 0, 0, 375, 720);
        logo = DisplayHelper.parsingSvg("/resources/images/AppLogo.svg", 20, 44, 284, 76);

        panel[1] = DisplayHelper.parsingImg("/resources/images/AddLogPane.png", 420, 84, 817, 531);

        panel[2] = DisplayHelper.parsingImg("/resources/images/InputPanelX.png", 490, 224, 673, 266);

        panel[3] = DisplayHelper.parsingSvg("/resources/images/AddBatchClicked.svg", 32, 487, 301, 47);

        button[0] = DisplayHelper.setupSidebar(parent, "/resources/images/DashboardNotClicked.svg", 187, "Dashboard");
        button[1] = DisplayHelper.setupSidebar(parent, "/resources/images/Daily_Logs_Not_Clicked.svg", 287, "DailyLogs");
        button[2] = DisplayHelper.setupSidebar(parent, "/resources/images/Analytics_NotClicked.svg", 387, "Analytics");
        button[3] = DisplayHelper.setupSidebar(parent, "/resources/images/Bi_NotClicked.svg", 587, "BiWeeklySample");

        button[4] = DisplayHelper.buttonSvg("/resources/images/SaveButton.svg", 430, 505, 250, 42);
        button[4].addActionListener(e -> {
            button[4].setEnabled(false);
            button[4].setEnabled(false);
            Timer timer = new Timer(3000, event -> {
                button[4].setEnabled(true);
            });
            timer.setRepeats(false);
            timer.start();
        });

        button[5] = DisplayHelper.buttonSvg("/resources/images/ClearLog.svg", 980, 505, 250, 42);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        txtDate.setText(today.format(formatter));

        //Textfield Appearance
        txtLabel[0] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt2.svg", 620, 175, 177, 37);
        txtLabel[1] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt2.svg", 990, 175, 177, 37);
        txtLabel[2] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt.svg", 510, 269, 259, 37);        
        txtLabel[3] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt.svg", 875, 269, 259, 37);        
        txtLabel[4] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt.svg", 510, 349, 259, 37);
        txtLabel[5] = DisplayHelper.parsingSvg("/resources/images/AddBatchTxt.svg", 875, 349, 259, 37);    

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
            JOptionPane.showMessageDialog(this, "Fields must be filled completely", "Missing input", JOptionPane.ERROR_MESSAGE);
            return null;
        }

            try{
                if(inputString[0].replaceAll("\\s+", "").length() >= 9){
                    JOptionPane.showMessageDialog(this, "Batch Name should be less than 9 characters", "Invalid batch name", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                String batchName = inputString[0].replaceAll("\\s+", "").toUpperCase();
                int stockCount = Integer.parseInt(inputString[1]);
                double totalWeight = Double.parseDouble(inputString[2]);
                double targetWeight = Double.parseDouble(inputString[3]);
                double avgWeightPerSample = totalWeight/stockCount;

                return new FishBatch(batchName, LocalDate.now(), stockCount, avgWeightPerSample, targetWeight,"Active");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number", "Invalid number", JOptionPane.ERROR_MESSAGE);
                return null;
            } catch (ArithmeticException e) {
                JOptionPane.showMessageDialog(this, "Stock count shouldn't be 0", "Invalid number", JOptionPane.ERROR_MESSAGE);
                return null;
            }
    }

}
