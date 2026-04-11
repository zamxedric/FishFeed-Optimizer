package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FishBatch {
    private int id;
    private String pondName;
    private LocalDate stockDate;
    private int initFishCount;
    private double targetWeight; 
    private String status;    // "Active", "Harvested", "Terminated"
    private double avgWeightPerSample;
    private double currentWeight;

    // Default Constructor
    public FishBatch() {}

    //Creating
    public FishBatch(String pondName, LocalDate stockDate, int initFishCount, double avgWeightPerSample, double targetWeight, String status) {
        this.pondName = pondName;
        this.stockDate = stockDate;
        this.initFishCount = initFishCount;
        this.avgWeightPerSample = avgWeightPerSample;
        this.targetWeight = targetWeight;
        this.status = "Active";
    }

    //Reading
    public FishBatch(int id, String pondName, LocalDate stockDate, int initFishCount, double avgWeightPerSample, double targetWeight, String status) {
        this.id = id;
        this.pondName = pondName;
        this.stockDate = stockDate;
        this.initFishCount = initFishCount;
        this.avgWeightPerSample = avgWeightPerSample;
        this.targetWeight = targetWeight;
        this.status = status;
    }

    public int getEstimatedSurvivors(LocalDate atDate, int recordedMortality) {
        long daysActive = ChronoUnit.DAYS.between(stockDate, atDate);
        if (daysActive < 0) {
            daysActive = 0;
        }

        int knownSurvivors = initFishCount - recordedMortality;
        double dailyBufferRate = 0.001; 
        double bufferMultiplier = Math.pow(1 - dailyBufferRate, daysActive);
        
        int estimatedTotal = (int) (knownSurvivors * bufferMultiplier);
        int floorCount = (int) (initFishCount * 0.70);
        
        return Math.max(estimatedTotal, floorCount);
    }

    public double estimateBiomass(double avgWeightGrams, LocalDate atDate, int recordedMortality) {
        int survivors = getEstimatedSurvivors(atDate, recordedMortality);
        return (survivors * avgWeightGrams) / 1000.0; 
    }

    public double getTargetFeedingRatePercentage() {
        long ageInDays = ChronoUnit.DAYS.between(stockDate, LocalDate.now());

        if (ageInDays <= 30) {
            return 0.08; 
        } else if (ageInDays <= 60) {
            return 0.05;
        } else if (ageInDays <= 90) {
            return 0.03; 
        } else {
            return 0.02;
        }
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getPondName() { 
        return pondName; 
    }
    public void setPondName(String pondName) { 
        this.pondName = pondName; 
    }

    public LocalDate getStockDate() { 
        return stockDate; 
    }
    public void setStockDate(LocalDate stockDate) { 
        this.stockDate = stockDate; 
    }

    public int getInitFishCount() { 
        return initFishCount; 
    }
    public void setInitFishCount(int initFishCount) { 
        this.initFishCount = initFishCount; 
    }

    public double getTargetWeight() { 
        return targetWeight; 
    }

    public void setTargetWeight(double targetWeight) { 
        this.targetWeight = targetWeight; 
    }

    public double getAvgWeightPerSample() {
        return avgWeightPerSample;
    }

    public void setAvgWeightPerSample(double avgWeightPerSample){
        this.avgWeightPerSample = avgWeightPerSample;
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    @Override
    public String toString() {
        return "FishBatch [id=" + id + ", pondName=" + pondName + ", stockDate=" + stockDate + ", initFishCount="
                + initFishCount + ", targetWeight=" + targetWeight + ", status=" + status + ", avgWeightPerSample="
                + avgWeightPerSample + "]";
    }

    
}