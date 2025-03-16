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
git commit -m "Updated Task 1 output with dataset.csv"
git push
```

🚀 **Now, your MapReduce Task 1 is fully documented, including all necessary commands!** 🚀



