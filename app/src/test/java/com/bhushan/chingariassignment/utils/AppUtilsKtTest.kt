package com.bhushan.chingariassignment.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.utils.networkUtility.Resource
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.lang.AssertionError

class AppUtilsKtTest {
    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var observer: Observer<Resource<List<WeatherData>>>

    @Mock
    private lateinit var liveData: MediatorLiveData<Resource<List<WeatherData>>>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testKotlinUtil() {
        liveData.observeOnce(lifecycleOwner, observer)
        Assert.assertFalse(liveData.hasActiveObservers())
    }
}