package com.websolverpro.dagger2basics.datasource

import dagger.Module
import dagger.Provides
import javax.inject.Inject

data class User @Inject constructor (val  userId: Int)

@Module
class SingleUser () {

    private var theUser = User(77)

    @Provides
    fun getAllUsers(): User {
        return theUser
    }
}

//data class User @Inject constructor(val  userId: Int, val id: Int, val title: String, val completed: Boolean)

//{
//    "userId": 1,
//    "id": 1,
//    "title": "delectus aut autem",
//    "completed": false
//}