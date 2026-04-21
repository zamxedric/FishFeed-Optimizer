## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
Syntax for SQL table creation (Sampling Records):
CREATE TABLE `sampling_records` (
  `sample_id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) NOT NULL,
  `sample_date` date NOT NULL,
  `avg_weight_sample` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`sample_id`),
  KEY `fk_batch_samples` (`batch_id`),
  CONSTRAINT `fk_batch_samples` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`) ON DELETE CASCADE
)

Syntax for SQL table creation (Daily Logs):
CREATE TABLE `daily_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) NOT NULL,
  `batch_name` varchar(255) DEFAULT NULL,
  `log_date` date NOT NULL,
  `feed_kg` decimal(8,2) DEFAULT NULL,
  `feed_cost_kg` decimal(5,2) DEFAULT NULL,
  `water_temp` decimal(4,2) DEFAULT NULL,
  `mortality` int(11) DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `fk_batch_logs` (`batch_id`),
  CONSTRAINT `fk_batch_logs` FOREIGN KEY (`batch_id`) REFERENCES `batches` (`batch_id`) ON DELETE CASCADE
) 

Syntax for SQL table creation (Batches):
CREATE TABLE `batches` (
  `batch_id` int(11) NOT NULL AUTO_INCREMENT,
  `pond_name` varchar(255) NOT NULL,
  `start_date` date NOT NULL,
  `initial_count` int(11) DEFAULT NULL,
  `avg_weight_per_sample` decimal(6,2) DEFAULT NULL,
  `target_weight` decimal(10,2) DEFAULT NULL,
  `status` varchar(255) DEFAULT 'ACTIVE',
  PRIMARY KEY (`batch_id`)
) 
