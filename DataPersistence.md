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