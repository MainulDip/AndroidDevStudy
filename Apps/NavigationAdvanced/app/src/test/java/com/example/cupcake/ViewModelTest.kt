package com.example.cupcake

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class ViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quantity_twelve_cupcakes() {
        /**
         * LiveData objects need to be observed in order for changes to be emitted. A simple way of doing this is by using the observeForever method.
         * */
        val viewModel = OrderViewModel()
        viewModel.quantity.observeForever {}
        viewModel.setQuantity(12)

        assertEquals(12, viewModel.quantity.value)
    }
}