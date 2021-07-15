package com.bhushan.chingariassignment.network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.data.WeatherMainData
import com.bhushan.chingariassignment.data.WeatherWindData
import com.bhushan.chingariassignment.utils.dbUtility.DatabaseHelper
import com.bhushan.chingariassignment.utils.networkUtility.AppExecutors
import com.bhushan.chingariassignment.utils.networkUtility.NetworkBoundResource
import com.bhushan.chingariassignment.utils.networkUtility.Resource
import com.bhushan.chingariassignment.utils.networkUtility.Status
import com.bhushan.chingariassignment.viewModel.WeatherDataViewModel
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WeatherDataRepositoryTest  {

    @Mock
    private lateinit var appExecutors: AppExecutors

    @Mock
    private lateinit var weatherService: WeatherService

    @Mock
    private lateinit var mockWeatherDataViewModel: WeatherDataViewModel

    @Mock
    private lateinit var liveData: MediatorLiveData<Resource<List<WeatherData>>>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testWeatherDataRepository() {
        val weatherDataRepository = WeatherDataRepository(appExecutors, weatherService)
        Assert.assertFalse(weatherDataRepository==null)

        val weatherDataViewModel = WeatherDataViewModel(weatherDataRepository)
        Assert.assertFalse(weatherDataViewModel==null)

        `when`(mockWeatherDataViewModel.getWeatherData(36.5, 35.5)).thenReturn(liveData)
        val resultLiveData: LiveData<Resource<List<WeatherData>>> = weatherDataViewModel.getWeatherData(36.5, 35.5)
        Assert.assertThat(resultLiveData.value, `is`(liveData.value))

        val weatherMainData = WeatherMainData(22.0, 223.0)
        val weatherWindData = WeatherWindData(225.0)
        val weatherData1 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134767, id = 1)
        val weatherData2 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134667, id = 2)
        val weatherData3 = WeatherData(main = weatherMainData, wind = weatherWindData, dt = 134567, id = 3)
        val list:List<WeatherData> = listOf(weatherData1, weatherData2, weatherData3)
        liveData.value = Resource(Status.SUCCESS, list, "", 200)
        `when`(mockWeatherDataViewModel.getWeatherData(36.5, 35.5)).thenReturn(liveData)
        Assert.assertThat(resultLiveData.value?.data, `is`(liveData.value?.data))
    }

}