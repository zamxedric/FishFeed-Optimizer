package view;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.DisplayHelper;

public class MainFrame extends JFrame{
    private JLabel bgLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Dashboard dashboard;
    private DailyLogs dailyLogs;
    private Analytics analytics;
    private AddBatch addBatch;
    private BiWeeklySample biWeeklySample;
    private JButton exitButton;

    public MainFrame(){
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1280,720);
        setResizable(false);
        setUndecorated(true);

        ImageIcon background = new ImageIcon("src\\display_components\\Background.png");
        bgLabel = DisplayHelper.parsingImg(background, 0, 0, 1280, 720);
        this.setContentPane(bgLabel);

        setLayout(null);

        exitButton = DisplayHelper.buttonSvg("src\\display_components\\CloseButton.svg", 1245, 5, 27, 27);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.setOpaque(false);
        mainPanel.setBounds(0, 0, 1280, 720);

        dashboard = new Dashboard(this); 
        dailyLogs = new DailyLogs(this);
        analytics = new Analytics(this);
        addBatch = new AddBatch(this);
        biWeeklySample = new BiWeeklySample(this);
        
        mainPanel.add(dashboard, "Dashboard");
        mainPanel.add(dailyLogs, "DailyLogs");
        mainPanel.add(analytics, "Analytics");
        mainPanel.add(addBatch, "AddBatch");
        mainPanel.add(biWeeklySample, "BiWeeklySample");

        add(mainPanel);
        add(exitButton);
        cardLayout.show(mainPanel, "Dashboard");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void switchPage(String pageName){
        cardLayout.show(mainPanel, pageName);
    }

    public Dashboard getDashboardPanel(){
        return dashboard;
    }

    public DailyLogs getDailyLogsPanel(){
        return dailyLogs;
    }

    public AddBatch getAddBatchPanel(){
        return addBatch;
    }

    public Analytics getAnalyticsPanel(){
        return analytics;
    }

    public BiWeeklySample getBiWeeklySamplePanel(){
        return biWeeklySample;
    }

    public void addExitButtonListener(ActionListener listener){
        exitButton.addActionListener(listener);
    }

    public int showReminderPopUp(List<String> unloggedBatches){
        StringBuilder message = new StringBuilder("You haven't logged for the following batches for today:\n\n");

        for(String batch: unloggedBatches){
            message.append("- ").append(batch).append("\n");
        }

        message.append("Are you sure you want to exit?");

        return JOptionPane.showConfirmDialog(this, message.toString(), "Missing Daily Logs", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public void closeApplication(){
        this.dispose();
        System.exit(0);
    }

}
