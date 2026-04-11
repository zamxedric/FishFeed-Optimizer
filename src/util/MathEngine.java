package util;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.FeedingLogDAO;
import dao.SamplingRecordDAO;
import model.FishBatch;
import model.SamplingRecord;

public class MathEngine {
    
    public double calculateRecommendedRation(FishBatch selectedBatch, double tempMultiplier, double weatherMultiplier) throws SQLException{
        double fcrCorrection = 1.0;
        SamplingRecordDAO recordDAO = new SamplingRecordDAO();
        FeedingLogDAO logDAO = new FeedingLogDAO();
        SamplingRecord record = new SamplingRecord();
        MathEngine latestFCR = new MathEngine();

        double currentFCR = latestFCR.getLatestMovingFCR(selectedBatch);
        if (currentFCR > 2.0) {
            fcrCorrection = 0.85; 
        } else if (currentFCR > 1.7) {
            fcrCorrection = 0.95;
        } else {
            fcrCorrection = 1.0; 
        }
        
        double biomass;
        double currentWeightInGrams;
        if(recordDAO.getLatestSample(selectedBatch.getId()) != null){
            record = recordDAO.getLatestSample(selectedBatch.getId());
            currentWeightInGrams = record.getAvgWeightSample();
        } else {
            currentWeightInGrams = selectedBatch.getAvgWeightPerSample();
        }

        biomass = selectedBatch.estimateBiomass(currentWeightInGrams, LocalDate.now(), logDAO.getTotalMortalityByBatch(selectedBatch.getId()));

        double targetRate = selectedBatch.getTargetFeedingRatePercentage();
        
        double baseRation = biomass * targetRate;
        double adjustedRation = baseRation * tempMultiplier * weatherMultiplier * fcrCorrection;

        return adjustedRation;
    }

    public double getLatestMovingFCR(FishBatch batch){
        final SamplingRecordDAO samplingDao = new SamplingRecordDAO();
        final FeedingLogDAO feedingDao = new FeedingLogDAO();
        try{
            List<SamplingRecord> samples = samplingDao.getSamplesByBatch(batch.getId());

            if(samples.isEmpty()){
                return -1.0;
            }

            SamplingRecord sampleB = samples.get(0);
            LocalDate dateB = sampleB.getSampleDate();
            int mortB = feedingDao.getTotalMortalityUpToDate(batch.getId(), dateB);
            double biomassB = batch.estimateBiomass(sampleB.getAvgWeightSample(), dateB, mortB);

            LocalDate dateA;
            double biomassA;

            if(samples.size() >= 2){

                SamplingRecord sampleA = samples.get(1);
                dateA = sampleA.getSampleDate();
                int mortA = feedingDao.getTotalMortalityUpToDate(batch.getId(), dateA);
                biomassA = batch.estimateBiomass(sampleA.getAvgWeightSample(), dateA, mortA);
            } else {
                dateA = batch.getStockDate();
                biomassA = batch.estimateBiomass(batch.getAvgWeightPerSample(), dateA,0);
            }

            double totalFeedConsumed =  feedingDao.getTotalFeedBetweenDates(batch.getId(), dateA, dateB);
            double biomassGained = biomassB - biomassA;

            if(biomassGained <= 0){
                return -1.0;
            }

            return totalFeedConsumed/biomassGained;

        } catch (SQLException e) {
            return -1.0;
        }
    }
    
    public double progressValue(double targetWeight, double avgWeightPerSample, double currentWeight){
        double progress = 0.0;
        if(targetWeight > avgWeightPerSample){
            progress = ((currentWeight - avgWeightPerSample) / (targetWeight - avgWeightPerSample)) * 100.0;
            progress = Math.max(0, Math.min(100, progress));
        }
        return progress;
    }

}
