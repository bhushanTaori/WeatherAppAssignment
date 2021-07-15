package com.bhushan.chingariassignment

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.utils.PermissionUtils
import com.bhushan.chingariassignment.utils.dbUtility.DatabaseHelper
import com.bhushan.chingariassignment.utils.networkUtility.Status
import com.bhushan.chingariassignment.utils.observeOnce
import com.bhushan.chingariassignment.view.WeatherDataAdapter
import com.bhushan.chingariassignment.viewModel.WeatherDataViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val weatherDataViewModel by viewModel<WeatherDataViewModel>()
    private lateinit var weatherDataAdapter: WeatherDataAdapter
    private var weatherDataList: ArrayList<WeatherData> = ArrayList()

    companion object {
        const val REQUEST_ID_PERMISSION = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        noDataText.visibility = View.VISIBLE
        DatabaseHelper.init(this)
        weatherDataAdapter = WeatherDataAdapter(weatherDataList)
        bindRecyclerView()
    }

    private fun bindRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        weatherDataSectionRV.isNestedScrollingEnabled = false
        weatherDataSectionRV.layoutManager = linearLayoutManager
        weatherDataSectionRV.adapter = weatherDataAdapter
    }

    /**
     * Below operations are taken care,
     * 1. Hit data/2.5/weather api & storing WeatherData inside Room db where
     * dt (Time of data calculation) is primary key of db entity.
     *
     * 2. Retrieve listOf WeatherData from room db sorting with dt
     *   in ascending order & populate on UI.
     *
     * 3. Error occurred then show failure toast & displays old room db list on UI.
     */
    private fun loadData(latVal: Double, longVal: Double) {
        loadingBar?.visibility = View.VISIBLE
        weatherDataViewModel.getWeatherData(latVal, longVal)
            .observeOnce(this, {
                if (it.status == Status.SUCCESS && !it.data.isNullOrEmpty()) {
                    loadingBar.visibility = View.GONE
                    weatherDataList.clear()
                    weatherDataList.addAll(it.data)
                    weatherDataAdapter.notifyDataSetChanged()
                    showNoDataTextIfApplicable()
                    SharedPrefHelper.addCurrentTemp(this, weatherDataList.last().main?.temp?:0.0)
                    updateWidget()
                } else if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                    weatherDataList.clear()
                    weatherDataList.addAll(DatabaseHelper.getWeatherDataList()?: Collections.emptyList())
                    weatherDataAdapter.notifyDataSetChanged()
                    showNoDataTextIfApplicable()
                    Toast.makeText(this, resources.getString(R.string.failed_data_load, it.message), Toast.LENGTH_LONG)
                        .show()
                    loadingBar.visibility = View.GONE
                }
            })
    }

    private fun showNoDataTextIfApplicable() {
        if (weatherDataList.isEmpty()) {
            noDataText.visibility = View.VISIBLE
        } else {
            noDataText.visibility = View.GONE
        }
    }

    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(this, TemperatureWidget::class.java))
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    REQUEST_ID_PERMISSION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 20 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(20000).setFastestInterval(20000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        loadData(location.latitude, location.longitude)
                    }
                }
            },
            Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}