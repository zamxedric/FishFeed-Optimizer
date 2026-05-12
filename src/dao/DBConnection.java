package dao;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.Properties;
import java.util.prefs.Preferences;

public class DBConnection {
    private static final String KEY_NAME = "db_key_v2";
    private static final String APP_DATA_DIR = getAppDataDirectory();
    private static final String DB_DIR = Paths.get(APP_DATA_DIR, "database").toString();
    private static final String DB_FILE_PATH = Paths.get(DB_DIR, "FishFeedOptimizer.db").toString();
    private static final String DB_URL = "jdbc:sqlite:" + new File(DB_FILE_PATH).toURI().getPath();
    
    private static boolean isInitialized = false;
    
    private DBConnection() {}

    private static String getAppDataDirectory() {
        String os = System.getProperty("os.name").toUpperCase();
        String userHome = System.getProperty("user.home");
        
        if (os.contains("WIN")) {
            String appData = System.getenv("APPDATA");
            if (appData == null || appData.isEmpty()) {
                appData = Paths.get(userHome, "AppData", "Roaming").toString();
            }
            return Paths.get(appData, "FishFeedOptimizer").toString();
        } else if (os.contains("MAC")) {
            return Paths.get(userHome, "Library", "Application Support", "FishFeedOptimizer").toString();
        } else {
            return Paths.get(userHome, ".FishFeedOptimizer").toString();
        }
    }

    // public static Connection getConnection() throws SQLException{
    //     Connection connection = null;
    //     connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishfeed_optimizer", "root", "");

    //     return connection;
    // }

    public static synchronized Connection getConnection() throws SQLException {
        try {
            Path dbDir = Paths.get(DB_DIR);
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
            }

            if (!isInitialized) {
                backupDatabase();
            }

            String dbPassword = getOrGeneratePassword();
            System.out.println(dbPassword);
            Properties props = new Properties();
            props.setProperty("password", dbPassword); 
            props.setProperty("cipher", "sqlcipher");
            
            Connection connection = DriverManager.getConnection(DB_URL, props);

            try (Statement stmt = connection.createStatement()) {
                //stmt.execute("PRAGMA key = '" + dbPassword.replace("'", "''") + "';");
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            if (!isInitialized) {
                initializeDatabase(connection);
                isInitialized = true;
            }

            return connection;

        } catch (Exception e) {
            throw new SQLException("Failed to establish database connection.", e);
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        String sqlContent = null;

        try {
            try (InputStream is = DBConnection.class.getResourceAsStream("/schema.sql")) {
                if (is != null) {
                    sqlContent = new String(is.readAllBytes());
                }
            }

            if (sqlContent == null) {
                Path localPath = Paths.get("src", "resources", "schema.sql");
                if (Files.exists(localPath)) {
                    sqlContent = new String(Files.readAllBytes(localPath));
                }
            }

            if (sqlContent != null) {
                String[] sqlStatements = sqlContent.split(";");
                try (Statement stmt = connection.createStatement()) {
                    for (String query : sqlStatements) {
                        if (!query.trim().isEmpty()) {
                            stmt.execute(query.trim());
                        }
                    }
                }
            } else {
                System.err.println("CRITICAL ERROR: schema.sql could not be found in the classpath or the 'src/resources' folder. Tables were not created.");
            }
        } catch (Exception e) {
            throw new SQLException("Couldn't initialize database schema", e);
        }
    }

    private static String getOrGeneratePassword() {
        Preferences prefs = Preferences.userNodeForPackage(DBConnection.class);
        String existingPassword = prefs.get(KEY_NAME, null);
        
        if (existingPassword != null && !existingPassword.trim().isEmpty()) {
            return existingPassword;
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 20; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        String newPassword = sb.toString();
        prefs.put(KEY_NAME, newPassword);
        
        try {
            prefs.flush();
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to securely save database key.", e);
        }
        return newPassword;
    } 

    private static void backupDatabase() {
        try {
            Path pathSource = Paths.get(DB_FILE_PATH);
            if (!Files.exists(pathSource)) {
                return; 
            }

            Path backupDir = Paths.get(APP_DATA_DIR, "Backups");
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            String dateSuffix = LocalDate.now().toString();
            Path backupFile = backupDir.resolve("FishFeedOptimizer_backup_" + dateSuffix + ".db");

            Files.copy(pathSource, backupFile, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (Exception e) {
            System.err.println("Failed to backup database: " + e.getMessage());
        }
    }
}