package model;

import java.time.LocalDate;

public class DailyFeedLog {
    private int id;
    private int batchId;
    private String batchName;
    private LocalDate logDate;
    private double feedGivenKg;
    private double feedCostPerKg;
    private double waterTemp;
    private int mortality;

    //Creation Constructor
    public DailyFeedLog(int batchId, String batchName, LocalDate logDate, double feedGivenKg, double feedCostPerKg, double waterTemp, int mortality){
        this.batchId = batchId;
        this.batchName = batchName;
        this.logDate = logDate;
        this.feedGivenKg = feedGivenKg;
        this.feedCostPerKg = feedCostPerKg;
        this.waterTemp = waterTemp;
        this.mortality = mortality;
    }

    //Reading Constructor
    public DailyFeedLog(int id, int batchId, String batchName, LocalDate logDate, double feedGivenKg, double feedCostPerKg, double waterTemp, int mortality){
        this(batchId, batchName, logDate, feedGivenKg, feedCostPerKg, waterTemp, mortality);
        this.id = id;
    }

    public DailyFeedLog(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getBatchId(){
        return batchId;
    }

    public String getBatchName(){
        return batchName;
    }

    public void setBatchName(String batchName){
        this.batchName = batchName;
    }

    public void setBatchId(int batchId){
        this.batchId = batchId;
    }

    public LocalDate getLogDate(){
        return logDate;
    }

    public void setLogDate(LocalDate logDate){
        this.logDate = logDate;
    }

    public double getFeedGivenKg(){
        return feedGivenKg;
    }

    public void setFeedGivenKg(double feedGivenKg){
        this.feedGivenKg = feedGivenKg;
    }

    public double getFeedCostPerKg(){
        return feedCostPerKg;
    }

    public void setFeedCostPerKg(double feedCostPerKg){
        this.feedCostPerKg = feedCostPerKg;
    }

    public double getWaterTemp(){
        return waterTemp;
    }

    public void setWaterTemp(double waterTemp){
        this.waterTemp = waterTemp;
    }

    public double getDailyTotalCost() {
        return this.feedGivenKg * this.feedCostPerKg;
    }

    public void setMortality(int mortality){
        this.mortality = mortality;
    }

    public int getMortality(){
        return mortality;
    }
}
