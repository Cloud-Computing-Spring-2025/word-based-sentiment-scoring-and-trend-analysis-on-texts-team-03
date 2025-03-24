# multi-stage-sentiment-analysis-Mapreduce_hive
This is Multi-Stage Sentiment Analysis on Historical Literature application is developed with map reduce and hive

### Hadoop MapReduce Execution Commands with Descriptions

## **Task 1: Preprocessing MapReduce Job**

### **Objective:**
Clean and standardize the raw text data from multiple books while extracting only the essential metadata—specifically, the book ID, title, and year of publication.

### **Implementation Notes:**
- Scope: Use MapReduce implemented in Java.
- Dataset: Students must source their own dataset, and thorough documentation is required.

### **Instructions:**

#### **Mapper:**
- **Input:** Each line from the raw text or CSV file.
- **Data Extraction:** Parse each line to extract the book ID, title, and year of publication.
- **Text Processing:** Convert the text to lowercase, remove punctuation, and apply stop word removal using a predefined list.
- **Output:** Emit key-value pairs where the key is a composite such as `(bookID, year)` and the value is the cleaned text (or token) for further processing.

#### **Reducer:**
- **Input:** Receives the processed text along with its data from the Mapper.
- **Output:** Produce a cleaned dataset retaining only the essential data (book ID, title, publication year) and the preprocessed text, ready for the next stage.

---

## **Brief About Task 1 and What We Are Doing**

### **Overview:**
In this task, we process a dataset containing books, extracting only relevant metadata (Book ID, Title, and Year of Publication). The goal is to clean and standardize the data using the Hadoop MapReduce framework. The dataset may contain raw text with punctuation, stopwords, and unnecessary information, which needs to be filtered out before further analysis.

### **Key Steps Involved:**
1. **Data Extraction:**
   - Extracts the **Book ID** (ISBN13), **Title**, and **Year of Publication**.
2. **Data Cleaning in Mapper:**
   - Converts text to **lowercase**.
   - **Removes punctuation** and unwanted symbols.
   - **Removes stop words** (e.g., 'the', 'is', 'and', etc.).
   - Emits a key-value pair in the format: `<isbn13>|<year> -> cleaned_text`.
3. **Aggregation in Reducer:**
   - Combines all words per book.
   - Outputs a clean dataset that is structured and standardized.

---
## 📂 **Project Structure**
```
task1/
│── input/
│    └── dataset.csv
│── src/main/java/com/example/
│    ├── PreprocessingDriver.java
│    ├── PreprocessingMapper.java
│    ├── PreprocessingReducer.java
│── target/
│    ├── preprocessing-1.0-SNAPSHOT.jar
│── pom.xml
│── README.md
```
---

## **Expected Output Format:**
The cleaned and processed output will be in the following format:

```
<isbn13>|<published_year>    <title> :: <cleaned_text_from_description>
```

### **Example Output:**
#### ✅ **Case 1: Well-Formatted Entry with Cleaned Description**
**Input Data:**
```
9780002005883,Gilead,Marilynne Robinson,2004,"A NOVEL THAT READERS and critics have been eagerly anticipating for over a decade, Gilead is an astonishingly imagined story of remarkable lives."
```

**Expected Output:**
```
9780002005883|2004    Gilead :: novel readers critics eagerly anticipating decade gilead astonishingly imagined story remarkable lives
```

#### ✅ **Case 2: Entry with Missing Year**
**Input Data:**
```
9780002261982,Spider's Web,Charles Osborne;Agatha Christie,, "A full-length novel adapted from a play by Charles Osborne."
```

**Expected Output:**
```
9780002261982|unknown    Spider's Web :: full length novel adapted play charles osborne
```
📌 **Fix Applied:** If `published_year` is missing, `"unknown"` is used.

#### ✅ **Case 3: Entry with No Description**
**Input Data:**
```
9780006163831,The One Tree,Stephen R. Donaldson,1982,
```

**Expected Output:**
```
9780006163831|1982    The One Tree :: No description available
```
📌 **Fix Applied:** `"No description available"` is added when `description` is empty.

🚀 **Final Verdict:** Your MapReduce Task 1 will now correctly process and format the dataset, ensuring it is structured and cleaned for further sentiment analysis and trend tracking! 🚀

---

## **Hadoop MapReduce Execution Commands**

### **1. Start Hadoop Cluster (Run on Local Machine)**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
docker compose up -d
```
✅ **Starts all Hadoop services inside Docker.**

### **2. Build the Updated Code (Recompile JAR) (Run on Local Machine)**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task1
mvn clean package
```
✅ **Rebuilds the JAR file with the latest fixes.**

### **3. Verify the New JAR File (Run on Local Machine)**
```bash
ls -l /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task1/target/
```
✅ **Ensures the JAR file has been successfully generated.**

### **4. Copy the New JAR File to the Hadoop Container (Run on Local Machine)**
```bash
docker cp /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task1/target/preprocessing-1.0-SNAPSHOT.jar \
resourcemanager:/opt/hadoop/
```
✅ **Copies the compiled JAR file to the Hadoop container.**

### **5. Enter the Hadoop Docker Container (Run on Local Machine)**
```bash
docker exec -it resourcemanager /bin/bash
```
✅ **Opens a terminal session inside the Hadoop container.**

### **6. Remove Old Output and Dataset from HDFS (Run Inside Docker)**
```bash
hadoop fs -rm -r /input/dataset
hadoop fs -rm -r /output
```
✅ **Deletes old dataset and output files in Hadoop HDFS to ensure fresh data processing.**

### **7. Remove Old Output from the Docker Container (Run Inside Docker)**
```bash
rm -rf /opt/hadoop/output/
```
✅ **Ensures outdated files are removed from the container.**

### **8. Exit the Docker Container**
```bash
exit
```
✅ **Exits the terminal session inside the container.**

### **9. Remove Old Output from Local Machine (Run on Local Machine)**
```bash
rm -rf /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/output/
```
✅ **Deletes old output files from the local machine.**

### **10. Copy the Updated Dataset (`dataset.csv`) to the Hadoop Container (Run on Local Machine)**
```bash
docker cp /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/input/dataset.csv \
resourcemanager:/opt/hadoop/
```
✅ **11. Enter the Hadoop Docker Container Again**
```bash
docker exec -it resourcemanager /bin/bash
```
✅ **12. Upload the New Dataset (`dataset.csv`) to HDFS**
```bash
hadoop fs -mkdir -p /input/dataset
hadoop fs -put -f /opt/hadoop/dataset.csv /input/dataset
```
✅ **13. Run the MapReduce Job**
```bash
hadoop jar /opt/hadoop/preprocessing-1.0-SNAPSHOT.jar \
com.example.PreprocessingDriver /input/dataset /output
```
✅ **14. Verify the New Output in HDFS**
```bash
hadoop fs -cat /output/*
```
✅ **15. Copy New Output from HDFS to Docker Container**
```bash
rm -rf /opt/hadoop/output/
hdfs dfs -get /output /opt/hadoop/
exit
```
✅ **16. Copy the Final Output from Docker to Local Machine**
```bash
docker cp resourcemanager:/opt/hadoop/output/ \
/workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/output/
```
✅ **17. Verify That the Local Output Matches HDFS Output**
```bash
cat /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/output/*
```
✅ **18. Commit & Push the Updated Output to Git**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
git add .
git commit -m "Task-1 Completed by Sai Nikil Teja Swarna."
git push
```

🚀 **Now, your MapReduce Task 1 is fully documented, including all necessary commands!** 🚀


# Task 2: Word Frequency Analysis with Lemmatization

## 📌 **Objective**
The goal of Task 2 is to compute word frequencies by splitting each sentence into words and applying lemmatization to reduce words to their base forms. This step enhances the dataset by providing structured word frequency analysis per book and year.

---

## 📖 **Implementation Notes**
- **Scope:** Implemented using Hadoop MapReduce in Java.
- **NLP Library:** Stanford CoreNLP for lemmatization.
- **Dataset:** Uses the cleaned dataset from Task 1 as input.

---

## ⚙ **Workflow Overview**

### **1️⃣ Mapper**
- **Input:** Each line from the cleaned dataset.
- **Steps:**
  - Split each sentence into individual words.
  - Apply lemmatization using Stanford CoreNLP.
  - Emit key-value pairs where the key is a tuple `(bookID, lemma, year)` and the value is `1`.

### **2️⃣ Reducer**
- **Input:** Aggregates key-value pairs from the Mapper.
- **Steps:**
  - Sum the counts for each lemma.
  - Output a dataset listing each lemma with its frequency along with the associated book ID and year.

---

## 📂 **Project Structure**
```
task2/
│── input/
│    └── cleaned_dataset.csv
│── lib/
│    └── stanford-corenlp-4.5.0-models-english.jar
│── output/
│    └── task2_output/
│── src/main/java/com/example/
│    ├── WordFrequencyDriver.java
│    ├── WordFrequencyMapper.java
│    ├── WordFrequencyReducer.java
│── target/
│    ├── wordfrequency-1.0-SNAPSHOT.jar
│── pom.xml
│── README.md
```
---

## 🛠 **Setup & Execution Commands**

### **1 Start Hadoop Cluster**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
docker compose up -d
```
✅ **Starts all Hadoop services inside Docker.**

---

### **2 Build & Package the Code**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task2
mvn clean package
```
✅ **Generates `wordfrequency-1.0-SNAPSHOT.jar` inside the `target/` folder.**

---

### **3 Download Stanford NLP Model**
```bash
wget https://nlp.stanford.edu/software/stanford-corenlp-4.5.0-models-english.jar -P /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task2/lib/
```
✅ **Downloads Stanford NLP English model into the `lib/` folder.**

---

### **4 Copy JAR to Hadoop Container**
```bash
docker cp target/wordfrequency-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop/
```
✅ **Moves the JAR file inside the Hadoop container.**

---

### **5 Enter the Hadoop Docker Container**
```bash
docker exec -it resourcemanager /bin/bash
```
✅ **Opens a terminal session inside the Hadoop container.**

---

### **6 Remove Old Input & Output Data from HDFS**
```bash
hadoop fs -rm -r /input/dataset
hadoop fs -rm -r /output/task2_output
```
✅ **Deletes old dataset and output files from Hadoop HDFS.**

---

### **7 Upload Cleaned Dataset to HDFS**
```bash
hadoop fs -mkdir -p /input/dataset
hadoop fs -put -f /opt/hadoop/cleaned_dataset.csv /input/dataset
```
✅ **Ensures that the latest dataset is uploaded to HDFS.**

---

### **8 Run MapReduce Job**
```bash
hadoop jar /opt/hadoop/wordfrequency-1.0-SNAPSHOT.jar \
com.example.WordFrequencyDriver /input/dataset /output/task2_output
```
✅ **Processes the dataset and computes word frequencies.**

---

### **9 Retrieve Output from HDFS to Local Machine**
```bash
hadoop fs -get /output/task2_output /opt/hadoop/
exit
docker cp resourcemanager:/opt/hadoop/task2_output /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task2/output/
```
✅ **Moves the final cleaned output to your local `task2/output/` directory.**

---

### **10 Verify Output**
```bash
ls -l /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task2/output/
```
✅ **Ensures `_SUCCESS` and `part-r-00000` exist.**

---
✅ **11 Commit & Push the Updated Output to Git**
```bash
cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
git add .
git commit -m "Task-2 Completed by Bhargavi Potu."
git push
```
---
## 🎯 **Expected Output Format**
```
<bookID>|<lemma>|<year>    <frequency>
```
✅ Example:
```
9780002005883|novel|2004    12
9780002005883|reader|2004    8
9780002261982|detective|2000    15
```

---

## ✅ **Final Verification**
After completing all steps, check that:
- The **output is correctly structured**.
- The **code runs successfully on Hadoop**.
- The **processed data is available in `task2/output/`**.
- The **GitHub repository is updated with the required files**.

🚀 **Task 2 is now successfully completed!** 🚀





# Task 3: Sentiment Scoring

## 📌 **Objective**
The goal of Task 3 is to compute sentiment scores for each book by matching words (or their lemmatized forms) to sentiment values from the AFINN lexicon. This provides cumulative sentiment analysis per book and year.

---

## 📖 **Implementation Notes**
- **Scope:** Implemented using Hadoop MapReduce in Java.
- **Sentiment Lexicon:** AFINN-111.txt
- **Dataset:** Uses the output or cleaned dataset from Task 2 as input.

---

## ⚙ **Workflow Overview**

### **1️⃣ Mapper**
- **Input:** Each line from the cleaned dataset.
- **Steps:**
  - Extract `bookID` and `year`.
  - Tokenize the text description.
  - Match each token with the AFINN lexicon.
  - Emit key-value pairs where the key is `(bookID|year)` and the value is the sentiment score of the word.

### **2️⃣ Reducer**
- **Input:** Aggregates key-value pairs from the Mapper.
- **Steps:**
  - Sum the sentiment scores for each `(bookID|year)`.
  - Output a dataset listing each book and year with the cumulative sentiment score.

---

## 📂 **Project Structure**
```
task3/
│── input/
│    └── task3-input.txt
│── lib/
│    └── AFINN-111.txt
│── output/
│    └── task3_output/
│── src/main/java/com/example/
│    ├── SentimentDriver.java
│    ├── SentimentMapper.java
│    ├── SentimentReducer.java
│── target/
│    ├── SentimentScoring-1.0-SNAPSHOT.jar
│── pom.xml
│── README.md
```

---

## 🛠 **Setup & Execution Commands**

### **1 Start Hadoop Cluster**
```bash
'cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03'
'docker compose up -d'
```
✅ **Starts all Hadoop services inside Docker.**

---

### **2 Build & Package the Code**
```bash
'cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3'
'mvn clean package'
```
✅ **Generates `SentimentScoring-1.0-SNAPSHOT.jar` inside the `target/` folder.**

---

### ✅ **Note- Download AFINN Sentiment Lexicon**
```bash
'wget https://raw.githubusercontent.com/fnielsen/afinn/master/afinn/data/AFINN-111.txt -P /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/lib/'
```
---


### **3 Copy JAR, Lexicon, and Input to Hadoop Container**
```bash
'docker cp /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/target/SentimentScoring-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop/'
'docker cp /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/lib/AFINN-111.txt resourcemanager:/opt/hadoop/'
'docker cp /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/input/task3-input.txt resourcemanager:/opt/hadoop/'

```

---

### **4 Enter the Hadoop Docker Container**
```bash
'docker exec -it resourcemanager /bin/bash'
```
✅ **Opens a terminal session inside the Hadoop container.**

---

### **5 Remove Old Input & Output Data from HDFS**
```bash
'hadoop fs -rm -r /input/task3_dataset'
'hadoop fs -rm -r /output/task3_output'
```
✅ **Deletes old dataset and output files from Hadoop HDFS.**

---

### **6 Upload Dataset to HDFS**
```bash
'hadoop fs -mkdir -p /input/task3_dataset'
'hadoop fs -put -f /opt/hadoop/task3-input.txt /input/task3_dataset'
```

---

### **7 Run the MapReduce Sentiment Scoring Job**
```bash
'hadoop jar /opt/hadoop/SentimentScoring-1.0-SNAPSHOT.jar /input/task3_dataset /output/task3_output'
```
✅ **Processes the dataset and computes sentiment scores.**

---

### **8 Retrieve Output from HDFS to Local Machine**
```bash
'hadoop fs -get /output/task3_output /opt/hadoop/'
'exit'
'docker cp resourcemanager:/opt/hadoop/task3_output /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/output/'
```
✅ **Moves the final sentiment output to your local `task3/output/` directory.**

---

### **9 Verify Output**
```bash
'ls -l /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task3/output/'
```
✅ **Ensures `_SUCCESS` and `part-r-00000` exist.**

---

### ✅ **10 Commit & Push the Updated Output to Git**
```bash
'cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03'
'git add .'
'git commit -m "Task-3 Sentiment Scoring Completed by Sruthi Bandi."'
'git push'
```

---


## 🎯 **Expected Output Format**
```
<bookID>|<year>    <cumulative_sentiment_score>
```
✅ Example:
```
9780002005883|2004    5
9780002261982|2000    -3
9780006178736|1993    8
```

---

## ✅ **Final Verification**
After completing all steps, check that:
- The **output is correctly structured**.
- The **code runs successfully on Hadoop**.
- The **processed sentiment data is available in `task3/output/`**.
- The **GitHub repository is updated with the required files**.

---

🚀 **Task 3 Sentiment Scoring is now successfully completed!** 🚀

**Task 4: Trend Analysis & Aggregation**

📌 Objective
The goal of Task 4 is to analyze sentiment scores over time by aggregating them into broader time intervals (decades) to observe long-term trends and correlations with historical events.

📖 Implementation Notes
Scope: Implemented using Hadoop MapReduce in Java.

Input: The sentiment score output from Task 3.

Output: Aggregated sentiment scores by (BookID | Decade).

Time Binning: Maps each year to its decade (e.g., 2004 → 2000s).

⚙ Workflow Overview
1️⃣ Mapper
Input: Sentiment score lines in the format:
<bookID>|<year> <sentimentScore>

Steps:

Parse bookID, year, and sentimentScore.

Calculate the decade as (year / 10) * 10.

Emit key-value pair:
Key = <bookID> | <decade>,
Value = <sentimentScore>.

2️⃣ Reducer
Input: Aggregated scores for (bookID | decade).

Steps:

Sum all sentiment scores for each (bookID | decade).

Output the aggregated result.

**📂 Project Structure**

task4/
│── input/
│    └── task4_input.txt
│── output/
│    └── task4_output/
│── src/main/java/com/example/
│    ├── TrendAnalysisDriver.java
│    ├── TrendAnalysisMapper.java
│    ├── TrendAnalysisReducer.java
│── target/
│    ├── TrendAnalysisMapReduce-1.0.0.jar
│── pom.xml
│── README.md
🛠 Setup & Execution Commands
**1️⃣ Start Hadoop Cluster**

cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
docker compose up -d
✅ Starts all Hadoop services.

**2️⃣ Build & Package the Code**

cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task4
mvn clean package
✅ Generates TrendAnalysisMapReduce-1.0.0.jar.

**3️⃣ Copy JAR to Hadoop Container**

docker cp target/TrendAnalysisMapReduce-1.0.0.jar resourcemanager:/opt/hadoop/
✅ Moves the JAR into Hadoop.

**4️⃣ Upload Task 3 Output to HDFS**

docker exec -it resourcemanager /bin/bash
hadoop fs -mkdir -p /input/task4_input
hadoop fs -put -f /opt/hadoop/task4_input.txt /input/task4_input
**5️⃣ Run MapReduce Job**

hadoop jar /opt/hadoop/TrendAnalysisMapReduce-1.0.0.jar \
com.example.TrendAnalysisDriver /input/task4_input /output/task4_output
✅ Executes the job and performs trend analysis.

**6️⃣ Retrieve Output from HDFS**

hadoop fs -get /output/task4_output /opt/hadoop/
exit
docker cp resourcemanager:/opt/hadoop/task4_output /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task4/output/
✅ Pulls the result to your local output folder.

**7️⃣ Verify Output**
ls -l /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03/task4/output/

**Commit & Push the Updated Output to Git**

cd /workspaces/word-based-sentiment-scoring-and-trend-analysis-on-texts-team-03
git add .
git commit -m "Task-4 Completed by Prakathesh ."
git push
