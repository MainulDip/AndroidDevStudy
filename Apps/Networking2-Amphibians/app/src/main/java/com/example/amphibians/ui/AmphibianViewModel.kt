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
package com.example.amphibians.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.SingletonAmphibian
import kotlinx.coroutines.launch
import okhttp3.internal.http2.ErrorCode
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer.ThrowException

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    // TODO: Create properties to represent MutableLiveData and LiveData for the API status
    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status

    // TODO: Create properties to represent MutableLiveData and LiveData for a list of amphibian objects
    private val _amphibianList = MutableLiveData<List<Amphibian>>()
    val amphibianList: LiveData<List<Amphibian>> = _amphibianList

    // TODO: Create properties to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    private val _amphibian = MutableLiveData<Amphibian>()
    val amphibian: LiveData<Amphibian> = _amphibian

    init {
        pullAmphibians()
    }

    // TODO: Create a function that gets a list of amphibians from the api service and sets the
    //  status via a Coroutine
    private fun pullAmphibians(){
        _status.value = AmphibianApiStatus.LOADING
        viewModelScope.launch {
            try {
                val allAmphibian = SingletonAmphibian.getAmphibians.getAmphibiansList()
                Log.d("AmphibianViewModel", "pullAmphibians: ${allAmphibian}")
                _amphibianList.value = allAmphibian
                _status.value = AmphibianApiStatus.DONE
            } catch (e: Exception) {
                Log.d("Error", "pullAmphibians: $e")
                _status.value = AmphibianApiStatus.ERROR
            }
        }

    }

    fun onAmphibianClicked(amphibian: Amphibian) {
        // TODO: Set the amphibian object
        _amphibian.value = amphibian
    }
}
