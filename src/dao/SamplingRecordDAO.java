package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.FishBatch;
import model.SamplingRecord;

public class SamplingRecordDAO {

    public boolean addSample(SamplingRecord record)throws SQLException{
        String sql = "INSERT INTO sampling_records(batch_id, sample_date, avg_weight_sample) VALUES (?,?,?)";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, record.getBatchId());
            stm.setString(2, record.getSampleDate().toString());
            stm.setDouble(3, record.getAvgWeightSample());

            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public SamplingRecord getLatestSample(int batchId)throws SQLException{
        String sql = "SELECT * FROM sampling_records WHERE batch_id = ? ORDER BY sample_date DESC LIMIT 1";
        SamplingRecord record = null;
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    record = mapRowSamplingRecord(rs);
                }
            }
            return record;
        }
    }

    public List<SamplingRecord> getSamplesByBatch(int batchId)throws SQLException{
        String sql = "SELECT * FROM sampling_records WHERE batch_id = ? ORDER BY sample_date DESC";
        List<SamplingRecord> records = new ArrayList<>();
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);

            try(ResultSet rs = stm.executeQuery()){
                while(rs.next()){
                    SamplingRecord record = mapRowSamplingRecord(rs);
                    records.add(record);
                }
                return records;
            }
        }
    }

    public List<SamplingRecord> getSamplingRecords()throws SQLException{
        String sql = "SELECT * FROM sampling_records ORDER BY sample_date DESC LIMIT 10";
        List<SamplingRecord> records = new ArrayList<>();
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                SamplingRecord record = mapRowSamplingRecord(rs);
                records.add(record);
            }
            return records;
        }   
    }

    public List<SamplingRecord> searchRecords(String search) throws SQLException{
        String sql = "SELECT batches.pond_name, sampling_records.* " +
                     "FROM sampling_records " + "JOIN batches ON sampling_records.batch_id = batches.batch_id " +
                     "WHERE batches.pond_name LIKE ?";
            List<SamplingRecord> records = new ArrayList<>();
            try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
                stm.setString(1, "%" + search + "%");

                try(ResultSet rs = stm.executeQuery()){
                    while(rs.next()){
                        SamplingRecord record = mapRowSamplingRecord(rs);
                        records.add(record);
                    }
                    return records;
                }
            }
    }

    public List<model.FishBatch> getBatchesDueSampling()throws SQLException{
        List<model.FishBatch> dueBatches = new ArrayList<>();
        String sql = "SELECT b.*, MAX(s.sample_date) as last_sample " +
                     "FROM batches b " +
                     "LEFT JOIN sampling_records s ON b.batch_id = s.batch_id " +
                     "WHERE b.status = 'Active' COLLATE NOCASE " +
                     "GROUP BY b.batch_id " +
                     "HAVING (last_sample IS NULL AND julianday('now') - julianday(b.start_date) >= 14) " +
                     "   OR (last_sample IS NOT NULL AND julianday('now') - julianday(last_sample) >= 14)";

        try(Connection conn = DBConnection.getConnection(); PreparedStatement stm = conn.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                model.FishBatch batch = new FishBatch(
                    rs.getInt("batch_id"),
                    rs.getString("pond_name"),
                    LocalDate.parse(rs.getString("stock_date")),
                    rs.getInt("initial_count"),
                    rs.getDouble("avg_weight_sample"),
                    rs.getDouble("target_weight"),
                    rs.getString("status")
                );
                dueBatches.add(batch);
            }
        }
        return dueBatches;               
    }

    public boolean updateRecord(SamplingRecord record)throws SQLException{
        String sql = "UPDATE sampling_records SET avg_weight_sample = ? WHERE sample_id = ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setDouble(1, record.getAvgWeightSample());
            stm.setInt(2, record.getId());

            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private SamplingRecord mapRowSamplingRecord(ResultSet rs)throws SQLException{
        return new SamplingRecord(
            rs.getInt("sample_id"),
            rs.getInt("batch_id"),
            LocalDate.parse(rs.getString("sample_date")),
            rs.getDouble("avg_weight_sample")
        );
    }
}
