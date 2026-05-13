## Fish Feed Optimizer

**Fish Feed Optimizer** is a Decision Support System(DSS) designed for milkfish(*Bangus*) pond and cage owners in the Philippines.

Feed costs account for up to **60-70% of total production expenses** in aquaculture. AquaStat solves the problem of "blind feeding" by using data-driven insights to calculate Feed Conversion Ratios (FCR) and provide smart daily feed recommendations based on environmental factors.

## Features

* **Smart Ration Calculator**: Adjusts daily feed amounts based on water temperature and weather conditions to prevent waste.
* **Moving FCR Analytics**: Tracks efficiency between sampling periods rather than just the whole cycle, allowing for quicker corrections.
* **Profitability Visualization**: Integrated line charts using **JFreeChart** to identify the "Point of Diminishing Returns" (where feed cost exceeds weight gain value).

## Built With
* Java 17 - The main programming language
* Java Swing - Desktop GUI Framework
* SQLite - Database
* FlatLaf - Modern, professional "Look and Feel" for the UI.
* JFreeChart - For generating growth velocity and financial analytics.
* SVG Salamander - For high-quality, scalable vector icons in the dashboard.

## Getting Started
To get a local copy up and running, follow these steps.

### Prerequisites

* [JDK 17 or higher] (https://www.oracle.com/java/technologies/downloads/)
* An IDE (IntelliJ, Eclipse, or VS Code)
* Git
* External Libraries (JAR Files listed below)

### Required Libraries (Download Links)

Since this project does not use Maven/Gradle yet, you must manually add these `.jar` files to your project's Build Path:

1.  **FlatLaf (Core) v3.7**: [Download from Maven Central](https://repo1.maven.org/maven2/com/formdev/flatlaf/3.7/flatlaf-3.7.jar)
2.  **FlatLaf (Extras) v3.0**: [Download from Maven Central](https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/3.0/flatlaf-extras-3.0.jar)
3.  **JFreeChart v1.5.6**: [Download from Maven Central](https://repo1.maven.org/maven2/org/jfree/jfreechart/1.5.6/jfreechart-1.5.6.jar)
4.  **SQLite JDBC v3.51.2.0**: [Download from Maven Central](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.2.0/sqlite-jdbc-3.45.2.0.jar) (Note: Adjusted to latest stable version)
5.  **SVG Salamander v1.1.5.3**: [Download from Maven Central](https://repo1.maven.org/maven2/com/kitfox/svg/svg-salamander/1.1.5.3/svg-salamander-1.1.5.3.jar)

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/AquaStat-Bangus.git
    ```
2.  **Setup Libraries:**
    *   Create a folder named `lib` in your project root.
    *   Move the downloaded JARs into the `lib` folder.
    *   In your IDE, right-click the JARs and select **"Add as Library"** or **"Add to Build Path"**.
3.  **Run the Application:**
    *   Locate `Main.java` and run it. The SQLite database will be initialized automatically on the first launch.