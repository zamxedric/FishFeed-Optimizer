package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.FishBatch;

public class FishBatchDAO {
    public boolean addBatch(FishBatch batch)throws SQLException{
        String sql = "INSERT INTO batches(pond_name, start_date, initial_count, avg_weight_per_sample, target_weight) VALUES (?,?,?,?,?)";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setString(1, batch.getPondName());
            stm.setObject(2, batch.getStockDate());
            stm.setInt(3, batch.getInitFishCount());
            stm.setDouble(4, batch.getAvgWeightPerSample());
            stm.setDouble(5, batch.getTargetWeight());

            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public FishBatch getBatchById(int id)throws SQLException{
        String sql = "SELECT * FROM batches WHERE batch_id = ?";
        FishBatch batch = null;
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, id);

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    batch = mapRowFishBatch(rs);
                }
            }
            return batch;
        }
    }

    public List<FishBatch> getBatchNames() throws SQLException {
        List<FishBatch> activeBatches = new ArrayList<>();
        
        String query = "SELECT batch_id, pond_name FROM batches WHERE status = 'Active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                FishBatch batch = new FishBatch();
                batch.setId(rs.getInt("batch_id"));
                batch.setPondName(rs.getString("pond_name"));
                
                activeBatches.add(batch);
            }
        }
        return activeBatches;
    }

    public List<FishBatch> getActiveBatches()throws SQLException{
        String sql = "SELECT b.*," +
                     "(SELECT avg_weight_sample FROM sampling_records s " +
                     " WHERE s.batch_id = b.batch_id " +
                     " ORDER BY sample_date DESC LIMIT 1) AS latest_weight " +
                     "FROM batches b WHERE b.status = 'Active'";

        List<FishBatch> batches = new ArrayList<>();
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                FishBatch batch = mapRowFishBatch(rs);

                double latestWeight = rs.getDouble("latest_weight");

                if (rs.wasNull() || latestWeight == 0) {
                
                batch.setCurrentWeight(batch.getAvgWeightPerSample());
                } else {
                batch.setCurrentWeight(latestWeight);
                }


                batches.add(batch);
            }
            return batches;
        }
    }

    public boolean updateStatus(int id)throws SQLException{
        String sql = "UPDATE batches SET status = 'HARVESTED' WHERE batch_id = ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, id);

            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteBatch(int id)throws SQLException{
        String sql = "DELETE FROM batches WHERE batch_id = ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, id);

            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private FishBatch mapRowFishBatch(ResultSet rs)throws SQLException{
        return new FishBatch(
            rs.getInt("batch_id"),
            rs.getString("pond_name"),
            rs.getObject("start_date", LocalDate.class),
            rs.getInt("initial_count"),
            rs.getDouble("avg_weight_per_sample"),
            rs.getDouble("target_weight"),
            rs.getString("status")
        );
    }
}
