package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.FeedingLogDAO;
import dao.FishBatchDAO;
import dao.SamplingRecordDAO;
import model.DailyFeedLog;
import model.FishBatch;
import model.SamplingRecord;
import util.ComboItem;
import view.MainFrame;

public class AppController {
    private final FeedingLogDAO logDAO;
    private final FishBatchDAO batchDAO;
    private final SamplingRecordDAO recordDAO;
    private final MainFrame view;

    public AppController(MainFrame view){
        this.view = view;
        this.batchDAO = new FishBatchDAO();
        this.logDAO = new FeedingLogDAO();
        this.recordDAO = new SamplingRecordDAO();

        initListeners();
        refreshBatchComboBoxes();
    }

    private void initListeners(){
        this.view.addExitButtonListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                attemptExit();
            }
        });

        this.view.getAnalyticsPanel().setController(this);
        this.view.getBiWeeklySamplePanel().setController(this);
        this.view.getDailyLogsPanel().setController(this);

        this.view.getDailyLogsPanel().getSaveButton().addActionListener(e -> saveDailyLog());
        this.view.getAddBatchPanel().getSaveButton().addActionListener(e -> saveAddBatch());
        this.view.getBiWeeklySamplePanel().getSaveButton().addActionListener(e -> saveRecord());
        this.view.getDashboardPanel().setOnHarvest(this::harvestBatch);
        this.view.getDashboardPanel().setOnDelete(this::deleteBatch);
        this.view.getDailyLogsPanel().setOnEdit(this::showEditDialogForLog);
        this.view.getBiWeeklySamplePanel().setOnEdit(this::showEditDialogForSample);
    }

    public FishBatchDAO getBatchDAO() {
        return this.batchDAO;
    }

    public FeedingLogDAO getLogDAO() {
        return this.logDAO;
    }

    public SamplingRecordDAO getRecordDAO() {
        return this.recordDAO;
    }

    private void refreshBatchComboBoxes(){
        try {
            List<FishBatch> batches = batchDAO.getBatchNames();

            JComboBox<ComboItem> analyticsCombo = view.getAnalyticsPanel().getBatchComboBox();
            JComboBox<ComboItem> dailyLogsCombo = view.getDailyLogsPanel().getBatchComboBox();
            JComboBox<ComboItem> biWeeklyCombo = view.getBiWeeklySamplePanel().getBatchesComboBox();

            analyticsCombo.removeAllItems();

            for(FishBatch b: batches){
                ComboItem item = new ComboItem(b.getId(), b.getPondName());
                dailyLogsCombo.addItem(item);
                analyticsCombo.addItem(item);
                biWeeklyCombo.addItem(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        


    }

    private void saveDailyLog(){
        DailyFeedLog logData = view.getDailyLogsPanel().getLogData();

        if(logData != null){
            try {
                boolean alreadyLogged = logDAO.logExistsForDate(logData.getBatchId(), LocalDate.now());
                if(!alreadyLogged){
                    logDAO.addLog(logData);
                    view.getDailyLogsPanel().clearField();
                    view.getDailyLogsPanel().loadRecentLogs();
                    refreshBatchComboBoxes();
                } else {
                    JOptionPane.showMessageDialog(view, "Batch already logged for today!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Failed to save daily log" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }

    private void saveAddBatch(){
        FishBatch batch = view.getAddBatchPanel().getBatchData();

        if(batch != null){
            try {
                batchDAO.addBatch(batch);
                view.getAddBatchPanel().clearField();
                view.getDashboardPanel().loadActiveBatches(); 
                refreshBatchComboBoxes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Failed to add batch" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveRecord(){
        SamplingRecord record = view.getBiWeeklySamplePanel().getSamplingRecordData();

        if(recordDAO != null){
            try {
                recordDAO.addSample(record);
                view.getBiWeeklySamplePanel().clearField();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Failed to save daily log" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void harvestBatch(FishBatch batch){
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to mark " + batch.getPondName() + " as Harvested?",
        "Confirm Harvest", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                batchDAO.updateStatus(batch.getId()); 
                
                JOptionPane.showMessageDialog(view, "Batch successfully harvested!");
                
                view.getDashboardPanel().loadActiveBatches(); 
                refreshBatchComboBoxes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Failed to harvest batch: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBatch(FishBatch batch){
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete " + batch.getPondName() + "\nAll saved logs will be deleted",
        "Delete Batch", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                batchDAO.deleteBatch(batch.getId()); 
                
                JOptionPane.showMessageDialog(view, "Batch successfully deleted!");
                
                view.getDashboardPanel().loadActiveBatches(); 
                refreshBatchComboBoxes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Failed to harvest batch: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDialogForLog(DailyFeedLog log){
        JPanel editPanel = new JPanel(new java.awt.GridLayout(0,2,10,10));

        JTextField feedField = new JTextField(String.valueOf(log.getFeedGivenKg()));
        JTextField costField = new JTextField(String.valueOf(log.getFeedCostPerKg()));
        JTextField tempField = new JTextField(String.valueOf(log.getWaterTemp()));
        JTextField mortalityField = new JTextField(String.valueOf(log.getMortality())); 

        editPanel.add(new JLabel("Amount given (kg): "));
        editPanel.add(feedField);
        editPanel.add(new JLabel("Feed cost per kg: "));
        editPanel.add(costField);
        editPanel.add(new JLabel("Water temp: "));
        editPanel.add(tempField);
        editPanel.add(new JLabel("Mortality: "));
        editPanel.add(mortalityField);

        int result = JOptionPane.showConfirmDialog(view, editPanel, "Edit Log: " + log.getBatchName() + " (" + log.getLogDate() + ")", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            try {
                double newFeed = Double.parseDouble(feedField.getText());
                double newCost = Double.parseDouble(costField.getText());
                double newTemp = Double.parseDouble(tempField.getText());
                int newMortality = Integer.parseInt(mortalityField.getText());

                log.setFeedGivenKg(newFeed);
                log.setFeedCostPerKg(newCost);
                log.setWaterTemp(newTemp);
                log.setMortality(newMortality);

                try {
                    logDAO.updateLog(log); 
                    view.getDailyLogsPanel().loadRecentLogs(); 
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Failed to update log: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Please enter a valid number", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        }
    } 

    private void showEditDialogForSample(SamplingRecord record){
        JPanel editPanel = new JPanel(new java.awt.GridLayout(0,2,10,10));

        JTextField avgWeightField = new JTextField(String.valueOf(record.getAvgWeightSample()));

        editPanel.add(new JLabel("Avg Weight per Sample: "));
        editPanel.add(avgWeightField);

        try {
            FishBatch batch = batchDAO.getBatchById(record.getBatchId());
            int result = JOptionPane.showConfirmDialog(view, editPanel, "Edit Record: " + batch.getPondName() + " (" + record.getSampleDate() + ")", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if(result == JOptionPane.OK_OPTION){
                double newAvgWeight = Double.parseDouble(avgWeightField.getText());
                record.setAverageBodyWeight(newAvgWeight);

                recordDAO.updateRecord(record);
                view.getBiWeeklySamplePanel().loadSamplingHistory();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Failed to harvest batch: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void attemptExit() {
        List<String> missingLogs = logDAO.getMissingLogsToday();

        if (!missingLogs.isEmpty()) {
            
            int choice = this.view.showReminderPopUp(missingLogs);
            
            if (choice == JOptionPane.YES_OPTION) {
                view.closeApplication();
            }
            
        } else {
            view.closeApplication();
        }
    }

}
