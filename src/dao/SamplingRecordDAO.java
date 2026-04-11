package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.SamplingRecord;

public class SamplingRecordDAO {

    public boolean addSample(SamplingRecord record)throws SQLException{
        String sql = "INSERT INTO sampling_records(batch_id, sample_date, avg_weight_sample) VALUES (?,?,?)";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, record.getBatchId());
            stm.setObject(2, record.getSampleDate());
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

    private SamplingRecord mapRowSamplingRecord(ResultSet rs)throws SQLException{
        return new SamplingRecord(
            rs.getInt("sample_id"),
            rs.getInt("batch_id"),
            rs.getObject("sample_date", LocalDate.class),
            rs.getDouble("avg_weight_sample")
        );
    }
}
