package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.DailyFeedLog;


public class FeedingLogDAO {

    public boolean addLog(DailyFeedLog log) throws SQLException {
    String sql = "INSERT INTO daily_logs(batch_id, batch_name, log_date, feed_kg, feed_cost_kg, water_temp, mortality) VALUES (?,?,?,?,?,?,?)";
    try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
        stm.setInt(1, log.getBatchId());
        stm.setString(2, log.getBatchName());
        stm.setObject(3, log.getLogDate());
        stm.setDouble(4, log.getFeedGivenKg());
        stm.setDouble(5, log.getFeedCostPerKg());
        stm.setDouble(6, log.getWaterTemp());
        stm.setInt(7, log.getMortality());
        return stm.executeUpdate() > 0;
    }
}

    public List<DailyFeedLog> getLogByBatch(int batchId)throws SQLException{
        String sql = "SELECT * FROM daily_logs WHERE batch_id = ?";
        List<DailyFeedLog> logs = new ArrayList<>();

        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);

            try(ResultSet rs = stm.executeQuery()){
                while(rs.next()){

                DailyFeedLog log = mapRowDailyFeedLog(rs);
                logs.add(log);
                }
                return logs;
            }
        }
    }

    public List<DailyFeedLog> getAllLogsByBatch(int batchId)throws SQLException{
        String sql = "SELECT * FROM daily_logs WHERE batch_id = ?";
        List<DailyFeedLog> logs = new ArrayList<>();
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                DailyFeedLog log = mapRowDailyFeedLog(rs);
                logs.add(log);
            }
            return logs;
        }
    }

    public List<DailyFeedLog> getRecentLogs()throws SQLException{
        String sql = "SELECT * FROM daily_logs ORDER BY log_date DESC LIMIT 10";
        List<DailyFeedLog> logs = new ArrayList<>();
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                DailyFeedLog log = mapRowDailyFeedLog(rs);
                logs.add(log);
            }
            return logs;
        }
    }

    public int getTotalMortalityByBatch(int batch_id)throws SQLException{
        String sql = "SELECT SUM(mortality) FROM daily_logs WHERE batch_id = ?";

        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batch_id);

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTotalMortalityUpToDate(int batchId, LocalDate date) throws SQLException {
        String sql = "SELECT SUM(mortality) FROM daily_logs WHERE batch_id = ? AND log_date <= ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);
            stm.setObject(2, date);
            try(ResultSet rs = stm.executeQuery()){
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public boolean logExistsForDate(int batchId, LocalDate date) throws SQLException {
        String sql = "SELECT 1 FROM daily_logs WHERE batch_id = ? AND log_date = ?";
    
        try (Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)) {
        
            stm.setInt(1, batchId);
            stm.setObject(2, date);

            try (ResultSet rs = stm.executeQuery()) {
                return rs.next(); 
            }
        }
    }

    public List<String> getMissingLogsToday(){
        String sql = "SELECT pond_name FROM batches WHERE batch_id NOT IN " +
                     "(SELECT batch_id FROM daily_logs WHERE DATE(log_date) = CURDATE())";
        List<String> batches = new ArrayList<>();

        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()){
            while(rs.next()){
                batches.add(rs.getString("pond_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batches;
    }

    public double getTotalCostByBatch(int batchId) throws SQLException{
        String sql = "SELECT COUNT(feed_cost_kg) AS avg_feed_price, SUM(feed_cost_kg) AS total_cost FROM daily_logs WHERE batch_id = ?";
        double totalCost = 0.0;
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    double tempTotalCost = rs.getDouble("total_cost");
                    int totalNumberPrice =  rs.getInt("avg_feed_price");
                    totalCost = tempTotalCost/totalNumberPrice; 
                }
            }
            return totalCost;
        }
    }

    public double getTotalFeedByBatch(int batchId) throws SQLException{
        String sql = "SELECT SUM(feed_kg) AS total_feed FROM daily_logs WHERE batch_id = ?";
        double totalFeed = 0.0;
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    totalFeed = rs.getDouble("total_feed");
                }
            }
            return totalFeed;
        }
    }

    public double getTotalFeedBetweenDates(int batchId, LocalDate start, LocalDate end)throws SQLException{
        String sql = "SELECT SUM(feed_kg) AS total_in_between_feed FROM daily_logs WHERE batch_id = ? AND log_date BETWEEN ? AND ?";
        double totalInBetWeenFeed = 0;
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, batchId);
            stm.setObject(2, java.sql.Date.valueOf(start));
            stm.setObject( 3, java.sql.Date.valueOf(end));

            try(ResultSet rs = stm.executeQuery()){
                if(rs.next()){
                    totalInBetWeenFeed = rs.getDouble("total_in_between_feed");
                }
            }
            return totalInBetWeenFeed;
        }
    }

    //Update
    public boolean updateLog(DailyFeedLog log)throws SQLException{
        String sql = "UPDATE daily_logs SET batch_name = ?, log_date = ?, feed_kg = ?, water_temp = ? WHERE log_id = ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setString(1, log.getBatchName());
            stm.setObject(2, log.getLogDate());
            stm.setDouble(3, log.getFeedGivenKg());
            stm.setDouble(4, log.getWaterTemp());
            stm.setInt(5, log.getId());

            int rowsAffected = stm.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public boolean deleteLog(DailyFeedLog log)throws SQLException{
        String sql = "DELETE FROM daily_logs WHERE log_id = ?";
        try(Connection con = DBConnection.getConnection(); PreparedStatement stm = con.prepareStatement(sql)){
            stm.setInt(1, log.getId());

            int rowsAffected = stm.executeUpdate();

            return rowsAffected > 0;
        }
    }

    private DailyFeedLog mapRowDailyFeedLog(ResultSet rs)throws SQLException{
        return new DailyFeedLog(
            rs.getInt("log_id"),
            rs.getInt("batch_id"),
            rs.getString("batch_name"),
            rs.getObject("log_date", LocalDate.class),
            rs.getDouble("feed_kg"),
            rs.getDouble("feed_cost_kg"),
            rs.getDouble("water_temp"),
            rs.getInt("mortality")
        );
    }

}
