### Room Setup:
https://developer.android.com/jetpack/androidx/releases/room

### Room Schema Generation for future migration:
First Configure Compiler Options in module's build.gradle. After setting @Database(...,exportSchema = true,...), the exported schema can be found in the specified location with `Project` view (not by `Android` View) inside Android Studio.
```kotlin
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }
}
```

Another option with kapt
```kotlin
android {
    //....    
   defaultConfig {
      //....
      kapt {
         arguments {
             arg("room.schemaLocation", "$projectDir/schemas".toString())
         
        }
      }
   }
}
```

Another Options with ksp (Kotlin Symbol Processing) || (Faster and Latest)
```kotlin
android {
    ...
    defaultConfig {
        ...
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}
```

Then with the @Database annotation add the `exportSchema = true` in params
```kotlin
@Database(
    entities = [User::class],
    exportSchema = true,
    version = 1,
//    autoMigrations = [
//        AutoMigration(1,2)
//    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}
```
### Auto Migration:
https://developer.android.com/training/data-storage/room/migrating-db-versions#automated
Suppose we added a new field to out table name `created`. To use Room's Auto Migration feature, we need to provide some more info with it, ex: `@ColumnInfo(name = "created", defaultValue = "0")`
```kotlin
/**
* Old Entity and Database
*/
@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val userName: String,
)

@Database(
    entities = [User::class],
    exportSchema = true,
    version = 1,
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}

/**
* New Entity and Database
*/
@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val userName: String,
   @ColumnInfo(name = "created", defaultValue = "0")
   val created: Long = System.currentTimeMillis()
)

@Database(
    entities = [User::class],
    exportSchema = true,
    version = 2,
   autoMigrations = [
       AutoMigration(1,2),
    //    AutoMigration(2,3, UserDatabase.Migration2To3::class)
   ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao

    // @RenameColumn("User", "created", "createdAt")
    // class Migration2To3: AutoMigrationSpec
}
```

* Sometime error can happen if everything is not done at a time correctly. In that case revert the version and comment all the changes, then build. After build success, un-comment new changes on every places (`@Entity` and `@Database`) and run build all together.
### Auto Migration for Renaming and Deleting Table/Field:
For renaming and deleting a table/field `@DeleteTable` or `@RenameTable` or `@DeleteColumn` or `@RenameColumn` need to be supplied with a AutomigrationSpec class

```kotlin
/**
* Changing/Renaming a Column from created to createdAt
*/
@Database(
    entities = [User::class],
    exportSchema = true,
    version = 3,
   autoMigrations = [
       AutoMigration(1,2),
       AutoMigration(2,3, UserDatabase.Migration2To3::class)
   ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao

    @RenameColumn("User", "created", "createdAt")
    class Migration2To3: AutoMigrationSpec
}
```

### Manual Migration
When the auto migration will not work (ie, adding a new @Entity or changing RelationShip, etc), we need to do manual Migration.

1. After making the changes (or adding new table/relations) using @Entity, first the up the version number of the @Database
2. Then add the RAW SQL query to do the necessary Database changes inside anywhere by inheriting an object from the `Migration(from,to)` class and implement the member. And add that object reference to the Room.databaseBuilder(...)

```kotlin
/**
* Adding a new Entity
*/
@Entity
data class School(
    @PrimaryKey(autoGenerate = false)
    val name: String
)

/**
* Changing Database Version In-order to Migrate
* and Defining an Companion Object with a property that Inject RAW
* SQL Query with @ annotation
*/

@Database(
    entities = [User::class, School::class],
    exportSchema = true,
    version = 4, // 3 to 4 for manual migration
    autoMigrations = [
        AutoMigration(1,2), // for auto migration
        AutoMigration(2,3, UserDatabase.Migration2To3::class) // for auto migration
    ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao

    /**
     * Defining AutoMigrationSpec
     */
    @RenameColumn("User", "created", "createdAt")
    class Migration2To3: AutoMigrationSpec

    /**
     * Defining Manual MMigration
     */
    companion object {
        val migration3To4 = object : Migration(3,4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS school (name CHAR NOT NULL PRIMARY KEY)")
            }
        }
    }
}


/**
* Adding Manual Migration Step Into Room.databaseBuilder
*/


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ....
        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            "user.db"
        ).addMigrations(UserDatabase.migration3To4).build()

//         lifecycleScope.launch {
// //            db.dao.getUsers().forEach(::println)
//             db.dao.getUsers().forEach {
//                 Log.d("Room-User", "$it")
//             }

//             db.dao.getSchools().forEach {
//                 Log.d("Room-School", "$it")
//             }
//         }
    }
}
```
### Testing Migration:
We have to do Instrumental Test, because we need the application context for Room.databaseBuilder.

Also for testing, we have to heavily depend on the Exported Schema. So make sure they are there.
Docs https://developer.android.com/training/data-storage/room/migrating-db-versions#single-migration-test

```kotlin
android {
    // ....
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
}
```
Example migration test for automated and all migration, including manual
```kotlin
private const val DB_NAME = "Test-DB"

@RunWith(AndroidJUnit4::class)
class UserMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        UserDatabase::class.java,
        listOf<AutoMigrationSpec>(UserDatabase.Migration2To3()),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migration1To2_containsCorrectData() {
        var db = helper.createDatabase(DB_NAME, 1).apply {
            execSQL("INSERT INTO user VALUES('test@test.com','UserName7')")
            close()
        }

        db = helper.runMigrationsAndValidate(DB_NAME, 2, true)

        db.query("SELECT * FROM user").apply {
            assertThat(moveToFirst()).isTrue()
            assertThat(getLong(getColumnIndex("created"))).isEqualTo(0)
        }
    }

    /**
     * Testing all migrations
     */
    @Test
    fun testAllMigrations() {
        helper.createDatabase(DB_NAME, 1).apply { close() }

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            UserDatabase::class.java,
            DB_NAME
        ).addMigrations(UserDatabase.migration3To4).build().apply {
            openHelper.writableDatabase.close()
        }
    }
}
```