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
       AutoMigration(1,2)
   ]
)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}
```

* Sometime error can happen if everything is not done at a time correctly. In that case revert the version and comment all the changes, then build. After build success, un-comment new changes on every places (`@Entity` and `@Database`) and run build all together.