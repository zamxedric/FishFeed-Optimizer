DROP TABLE IF EXISTS batches;
DROP TABLE IF EXISTS daily_logs;
DROP TABLE IF EXISTS sampling_records;

-- 1. Batches Table
CREATE TABLE batches (
    batch_id INTEGER PRIMARY KEY AUTOINCREMENT,
    pond_name TEXT NOT NULL,
    start_date TEXT NOT NULL, 
    initial_count INTEGER DEFAULT NULL,
    avg_weight_per_sample REAL DEFAULT NULL,
    target_weight REAL DEFAULT NULL,
    status TEXT DEFAULT 'ACTIVE'
);

-- 2. Daily Logs Table
CREATE TABLE daily_logs (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    batch_id INTEGER NOT NULL,
    log_date TEXT NOT NULL,
    feed_kg REAL DEFAULT NULL,
    feed_cost_kg REAL DEFAULT NULL,
    water_temp REAL DEFAULT NULL,
    mortality INTEGER DEFAULT NULL,
    CONSTRAINT fk_batch_logs 
        FOREIGN KEY (batch_id) 
        REFERENCES batches (batch_id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_batch_logs ON daily_logs (batch_id);

-- 3. Sampling Records Table
CREATE TABLE sampling_records (
    sample_id INTEGER PRIMARY KEY AUTOINCREMENT,
    batch_id INTEGER NOT NULL,
    sample_date TEXT NOT NULL,
    avg_weight_sample REAL DEFAULT NULL,
    CONSTRAINT fk_batch_samples
        FOREIGN KEY (batch_id) 
        REFERENCES batches (batch_id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_batch_samples ON sampling_records (batch_id);