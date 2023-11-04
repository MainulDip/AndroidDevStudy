package com.websolverpro.dagger2basics.datasource

import javax.inject.Inject

data class User @Inject constructor (val  userId: Int)
//data class User @Inject constructor(val  userId: Int, val id: Int, val title: String, val completed: Boolean)

//{
//    "userId": 1,
//    "id": 1,
//    "title": "delectus aut autem",
//    "completed": false
//}