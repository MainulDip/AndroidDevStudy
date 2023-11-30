## Overview:
All about Data Persistance in Android.

### SQL Refresh:
- table: the type of information. Its the template of the data
- column: properties are represented by columns
- row: each table entity/data, each row has data corresponding to each column. Rows define the actual data inside a table.
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
* Aggregate functions :
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
### Room Dependency Configs:
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
- Entity: each table is represented by a class (model class or `entities`) or more specifically a data class.
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
### Room Model + DAO + ViewModel + AppData:
* Entity (`model` as data class): For each table there should be a Room Model (entity)

* DAO (Interface): For each entity (Room Model) there should be an DAO (Data Access Object). Each DAO specifies all the sql query to access the room database. Usually 1 DAO From each screen.

* ViewModel: For each DAO, there should be an viewmodel, which should be instantiated with lifecycle aware feature (inheriting `ViewModelProvider.Factory`). Also we can inject other object (ie, `repository to access DAO`) using the this Factory Pattern.

* AppDatabase (abstract): the AppDatabase class is for creating the Database based on Entity (Room Model) and pre-populate the database with data. It will also instantiate the DAO using Singleton pattern so there will be only one instance of DAO to access from

* Application class Inherit: From a Custom class that is inherited form Application() to get the applicationContext, initialized the AppDatabase's singleton using lazy


* Manifest.xml Entry: To Use the database when application starts, enlist the `custom Application class` (that is inherited form Application) by android:name attribute inside <Application>
### Implementation Steps of the Room ORM:

* Initial State
    - Model/Entity(Table): Define the Model and make it Entity by the @Entity anotation, also anotate the class initializer properties

    - DAO: Define the DAO. We will access the database through this

    - Database Builder: Define an abstract class inheriting from RoomDatabase, an abstract method that returns the DAO and a companion object (final/non-abstract) that will return a Singleton room database. From a method with context as parameter, initialize the database Room's databaseBuilder method. We will pass the context parameter and this abstract class in databaseBuilder's parameter.

    - ViewModel: Define the viewModel class with the DAO as constructor parameter, the members of this class will access data using the DAO directly.

    - Factory ViewModel: Define a viewModelFactory with DAO as constructor parameter, inherit ViewModelProvider.Factory (to make it lifecycle aware) and override it's member by returning the initialized viewModel class passing the DAO as parameter.

* Get DB using Application or Repository Pattern:
    - Application : Define a class that inherit Application() so we can get the real applicationContext. Inside this class define a property with lazy{} delegate which returns the Singleton of the database passing the applicationContext.  Aslo Include this class in Manifest.xml 's Application's android:name attribute. So that, we can call it from activity?.application as Something from UIs when initilizing viewModel's Factory.

    - Repository Pattern : this way, the DAO (injected through AppDatabse's Singleton Instance) is retrieved through a Repository Class' Singleton Instance. The context needs to be passed from the UI through the ViewModel's Factory Initialization.
```kotlin
// viewmodel
class PlantListViewModel internal constructor(
    plantRepository: PlantRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() { }

// ViewModel Factory
class PlantListViewModelFactory(
    private val repository: PlantRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return PlantListViewModel(repository, handle) as T
    }
}

// a utils fn to get repository
object InjectorUtils {
    fun providePlantListViewModelFactory(fragment: Fragment): PlantListViewModelFactory {
        return PlantListViewModelFactory(
            PlantRepository.getInstance( AppDatabase.getInstance(fragment.requireContext().applicationContext).plantDao()),
            fragment
            )
    }
}

// UI's viewmodel initialization
class PlantListFragment : Fragment() {

    private val viewModel: PlantListViewModel by viewModels {
        InjectorUtils.providePlantListViewModelFactory(this)
    }
    // ....
}

// See Sunflower App
```

* Hooking Part
    - ( Using Application ) From UI (Activity/Fragment), Initialize the viewModel by instantiating the viewModelFactory inside activityViewModels lambda, pass the DAO by calling the abstract method through the delegated lazy property inside the class that inherited from the Application class.

    - ( Using Repository Pattern ) 
 

### @Volatile on property:
Marks the JVM backing field of the annotated property as volatile, meaning that writes to this field are immediately made visible to other threads.

### synchronized (lock: Any, block: () -> R): R | on method:
This function (like java)  protects the code block from concurrent execution by multiple threads by the monitor of the instance (or, for static methods, the class or object/companion) on which the method is defined
### Room Migration:
* migration object with a migration strategy is required for when the schema changes
* A migration object is an object that defines how to take all rows with the old schema and convert them to rows in the new schema, so that no data is lost.
* fallbackToDestructiveMigration() migration strategy will destroy and rebuild the database, which means that the previous data is lost

* Article: https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929



### Caching | Repository Pattern:
The repository pattern is a design pattern that isolates the data layer from the rest of the app (UI and ViewModels). A repository can resolve conflicts between data sources (such as persistent models, web services, and caches) and centralize changes to this data. 

The Repository class implements the logic for deciding whether to fetch data from a network or use results that are cached in a local database.

### Android Storage Solutions:
* Media Files: app's internal private storage directory [https://developer.android.com/training/data-storage/app-specific]. It is cleared when app is uninstalled.

* Retrofit Network Results Caching: Retrofit can be configured to store a copy of every network result locally.

* DataStore | Key/Value : Good solution for a small number of keys and simple values (Structured Data), such as app settings. Not for large and complex structured data.

* SQLlite : Recommended solution for complex structured queryable data. Android provide SQLlite support out of the box and has the Room ORM. It is very light weight, doesn't need a server, and can handle a single request at-a-time. Requests are handled one by one (queued) and can support only one user. All those things make it a good solution for a single-user device's database solution for structured data like mobile or desktop app. But not suitable for web servers.


### Caching Operation By Dispatchers.IO:
Databases on Android are stored on the file system, or disk, and in order to save they must perform a disk I/O operation. Disk I/O, or reading and writing to disk, is slow and always blocks the current thread until the operation is complete. 

Because of this, it is ideal to run the disk I/O in the I/O dispatcher. This dispatcher is designed to offload blocking I/O tasks to a shared pool of threads using withContext(Dispatchers.IO) { ... }.

### refresh strategy:
A database refresh is a process of updating or refreshing the local database to keep it in sync with data from the network. Like the module that requests data from the repository is responsible for refreshing the local data.

### Shared Preferences & DataStore (Preferences & Protobuf):
`Shared Preference` is replaced by `Preference DataStore`. Though, behind the scene PD is Using SP.

To Create and fetch Shared Preference `getSharedPreferences()` and `getPreferences()` can be used (create if-not exists, or fetch is its there). And to populate and get the data use
```kotlin
// create Shared Preference is not exists, or get the handle if its there.
private val sharedPreferences = context.getSharedPreferences("Name", Context.MODE_PRIVATE)
// good practice to name it with Application's ID prefix (com.example.myapp.PREFERENCE_FILE_KEY)

// populate with data
with(sharedPreferences.edit()) {
    putString(key, value)
    apply()
}

// fetch data
sharedPreferences.getString(key, "")!!
```

DataStore is ideal for small, simple datasets, such as storing login details, the dark mode setting, font size, and so on. But not suitable for complex datasets, such as an online grocery store inventory list, or a student database. There are two types of DataStore
 * Preferences DataStore: accesses and stores data based on keys, without defining a schema (database model) upfront.
 * Proto DataStore: defines the schema using Protocol buffers. It lets persist strongly typed data. Protobufs are faster, smaller, simpler, and less ambiguous than XML and other similar data formats. Proto DataStore is type safe and efficient but requires configuration and setup



### Preferences DataStore Implementation:
Add build.gradle (Module) dependencies
```kotlin
implementation "androidx.datastore:datastore-preferences:1.0.0"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
```
### Sample Preference DataStore Class:
```kotlin
private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as receiver.
private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class SettingsDataStore(context: Context) {

    private val IS_LINEAR_LAYOUT_MANAGER : Preferences.Key<Boolean> = booleanPreferencesKey("is_linear_layout_manager")

    /**
     * when called, it will create the preferences DataStore
     * if failed for any reason, it will throw IOException
     */
    val preferenceFlow: Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // storing the preferences datastore key with default value
            // On the first run of the app, we will use LinearLayoutManager by default
            preferences[IS_LINEAR_LAYOUT_MANAGER] ?: true
        }

    suspend fun saveLayoutToPreferencesStore(isLinearLayoutManager: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayoutManager
        }
    }
}
```
Initialized from the UI
```kotlin
// Initialize SettingsDataStore
SettingsDataStore = SettingsDataStore(requireContext())

// convert to livedata, set observer, inject the stored value into class property and redraw the menu
SettingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { value ->
    isLinearLayoutManager = value
    chooseLayout()

    /**
        * Redraw the menu
        * invalidateOptionsMenu() will call the onCreateOptionsMenu again
        * */
    activity?.invalidateOptionsMenu()
}

/////////////////////////////////////////////////////////////////////
// Update the preferences value
lifecycleScope.launch {
    SettingsDataStore.saveLayoutToPreferencesStore(isLinearLayoutManager, requireContext())
}
```


### Storing Multitple Values In Preferences DataStores