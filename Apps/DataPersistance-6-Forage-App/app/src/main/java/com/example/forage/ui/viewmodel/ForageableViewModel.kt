/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.ui.viewmodel

import android.app.Activity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.CoroutineContext

/**
 * Shared [ViewModel] to provide data to the [ForageableListFragment], [ForageableDetailFragment],
 * and [AddForageableFragment] and allow for interaction the the [ForageableDao]
 */

// TODO: pass a ForageableDao value as a parameter to the view model constructor
class ForageableViewModel(
    // Pass dao here
    val forageableDao: ForageableDao
): ViewModel() {

    // TODO: create a property to set to a list of all forageables from the DAO
    val getForageableList: LiveData<List<Forageable>> = forageableDao.getForageables().asLiveData(viewModelScope.coroutineContext)

    // TODO : create method that takes id: Long as a parameter and retrieve a Forageable from the
    //  database by id via the DAO.
    fun getForageableById(id: Long): LiveData<Forageable> {
        return forageableDao.getForageable(id).asLiveData(viewModelScope.coroutineContext)
    }

    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )

    // TODO: launch a coroutine and call the DAO method to add a Forageable to the database within it
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.insert(forageable)
        }

    }

    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        val forageable = Forageable(
            id = id,
            name = name,
            address = address,
            inSeason = inSeason,
            notes = notes
        )
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.update(forageable)
        }
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}

// TODO: create a view model factory that takes a ForageableDao as a property and
//  creates a ForageableViewModel

class ForageableViewModelFactory(val forageableDao: ForageableDao): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // if modelClass is a ViewModel, instantiate it with the dao or throw IOException
        if (modelClass.isAssignableFrom(ForageableViewModel::class.java)) {
            return ForageableViewModel(forageableDao) as T
        }

        throw IOException("It's not a ViewModel Class")
    }


}
