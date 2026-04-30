package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

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

        this.view.getDailyLogsPanel().getSaveButton().addActionListener(e -> saveDailyLog());
        this.view.getAddBatchPanel().getSaveButton().addActionListener(e -> saveAddBatch());
        this.view.getBiWeeklySamplePanel().getSaveButton().addActionListener(e -> saveRecord());
        this.view.getDashboardPanel().setOnHarvest(this::harvestBatch);
        this.view.getDashboardPanel().setOnDelete(this::deleteBatch);
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
