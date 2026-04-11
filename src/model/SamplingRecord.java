package model;

import java.time.LocalDate;

public class SamplingRecord {
    private int id;
    private int batchId;
    private LocalDate sampleDate;
    private double avgWeightSample;

    //Create
    public SamplingRecord(int batchId, LocalDate sampleDate, double avgWeightSample){
        this.batchId = batchId;
        this.sampleDate = sampleDate;
        this.avgWeightSample = avgWeightSample;
    }

    //Read
    public SamplingRecord(int id, int batchId, LocalDate sampleDate, double avgWeightSample){
        this(batchId, sampleDate, avgWeightSample);
        this.id = id;
    }

    public SamplingRecord(){}

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getBatchId(){
        return batchId;
    }

    public void setBatchId(int batchId){
        this.batchId = batchId;
    }

    public LocalDate getSampleDate(){
        return sampleDate;
    }

    public void setSampleDate(LocalDate sampleDate){
        this.sampleDate = sampleDate;
    }

    public double getAvgWeightSample(){
        return avgWeightSample;
    }

    public void setAverageBodyWeight(double avgWeightSample) {
        this.avgWeightSample = avgWeightSample;
    }

    @Override
    public String toString() {
        return "SamplingRecord [id=" + id + ", batchId=" + batchId + ", sampleDate=" + sampleDate + ", avgWeightSample="
                + avgWeightSample + "]";
    }
}
