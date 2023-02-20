## Overview:
All about Data Persistance in Andriod.

### SQL Refresh:
- table: the type of information. Its the template of the data
- column: properties are represented by columns
- row: each table entriy/data, each row has data corresponding to each column. Rows define the actual data inside a table.
- primary key: Its a unique identifier that is unique to each row in a table. Like ids. Primary keys make it possible to have the relationships in a relational database.
- "=" single equal is for comparison
- "!=" -> not equal operator
- where: is used to filter results based on one or more columns
- AND / OR : boolean operator to add more than one condition
### Database Inspector and SQL query in Android Studio:
- View > Tool Windows > App Inspection the select database tab
Then select the table and run sql query by "open new query tab"
```sql
SELECT name FROM park
WHERE type = "national_park" limit 7
```
```sql
SELECT name FROM park
WHERE type != "recreation_area"
AND area_acres > 100000
```

### Common SQL Functions:
* Aggregate functions:
 - SUM(column_name) => calculate the sum of all values/rows in the column_name
 - COUNT(column_name) => return number of rows in that column
* Non-aggregate function:
 - MAX(column_name) => return the largest selected-column's row value
 - MIN(column_name) => return the smallest selected-column's row value
 - DISTINCT(column_name) => return all the unique values/row of the column
 ### SQL Query Ordering:
 - ORDER BY clause at the end of the query, ASC/DESC can sit after it
 - GROUP BY clause goes before ORDER BY (if any)
 - WHERE goes Before GROUP BY

```sql
SELECT type, COUNT(*) FROM park
GROUP BY type
ORDER BY type
```
* Write a SQL query to the top 5 park names along with their visitor count that had the most visitors, in descending order.
```sql
SELECT name, park_visitors from park
ORDER BY park_visitors DESC
LIMIT 5
```
### INSERT INTO | UPDATE | DELETE FROM :
```sql
-- INSERT 
INSERT INTO table_name
VALUES (column1, column2, ...)

-- UPDATE
UPDATE table_name
SET column1 = ...,
column2 = ...,
...
WHERE column_name = ...

-- DELETE
DELETE FROM table_name
WHERE column_name = ...
```
### ROOM ORM and Kotlin Flow:
- ROOM : An easy way to use a database in an Android app is with a library called Room.
- Flow: It can asynchronously return blocking-computed-collection-values multiple times, where A suspending function (also sequence) asynchronously returns collection's computed values only once
```kotlin
fun simple(): Flow<Int> = flow { // flow builder
    for (i in 1..3) {
        delay(100) // pretend some computation/network-call
        emit(i) // emit to return value
    }
}

fun main() = runBlocking<Unit> {
    launch {
        for (k in 1..3) {
            println("I'm not blocked $k")
            delay(100)
        }
    }
    // Collect the flow
    simple().collect { value -> println(value) } 
}

// results
// I'm not blocked 1
// 1
// I'm not blocked 2
// 2
// I'm not blocked 3
// 3
```
### Room Configs:
* add room version in the project level (global) variable
 - room_version = '2.4.2'
* add room dependencies in module's build.gradle
```kotlin
implementation "androidx.room:room-runtime:$room_version"
kapt "androidx.room:room-compiler:$room_version"

// optional - Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:$room_version"
```
* add plugin to use kapt in module's plugin objecet
 - id 'kotlin-kapt'

 ### Room Intro:
- Entity: each table is represented by a class (model class or entities) or more specifically a data class.
- Room queries are not case sensitive
- the data class should be annoted with @Entity or @Entity(tableName="table_name")
- project's files should be organized by separate packages for each Entity. DAO Interface and Entity can be on same package.
- sample entity class
```kotlin
@Entity
data class Schedule(
   @PrimaryKey val id: Int,
   @NonNull @ColumnInfo(name = "stop_name") val stopName: String,
   @NonNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
)
```
### DAO (Data Access Object) and Room Integration:
It's a iterface that provides access to the data. Functions of DAO provide CRUD operations of the database (often using SQL commands)
```kotlin
@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
    fun getAll(): List<Schedule>
}
```