package com.bhushan.chingariassignment.view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhushan.chingariassignment.R
import com.bhushan.chingariassignment.data.WeatherData
import com.bhushan.chingariassignment.utils.getHour
import com.bhushan.chingariassignment.utils.getHumidity
import com.bhushan.chingariassignment.utils.getSpeed
import com.bhushan.chingariassignment.utils.getTemp

class WeatherDataAdapter(
        private val weatherDataList: ArrayList<WeatherData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WeatherDataItemViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_weather_data_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weatherData = weatherDataList[position]
        val itemViewHolder = holder as WeatherDataItemViewHolder
        itemViewHolder.hour.text = getHour(weatherData.dt)
        itemViewHolder.humidity.text = getHumidity(holder.itemView.context, weatherData.main?.humidity?:0.0)
        itemViewHolder.temp.text = getTemp(holder.itemView.context, weatherData.main?.temp?:0.0)
        itemViewHolder.windSpeed.text = getSpeed(holder.itemView.context, weatherData.wind?.speed?:0.0)
    }

    override fun getItemCount(): Int {
        return weatherDataList.size
    }
}

class WeatherDataItemViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    val temp: TextView = itemView.findViewById(R.id.temp)
    val windSpeed: TextView = itemView.findViewById(R.id.windSpeed)
    val humidity: TextView = itemView.findViewById(R.id.humidity)
    val hour: TextView = itemView.findViewById(R.id.hour)
}