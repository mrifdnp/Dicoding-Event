package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventsResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcomingEvents()
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventsResponse = response.body()
                    if (eventsResponse != null && !eventsResponse.error) {
                        _upcomingEvents.value = eventsResponse.listEvents.take(5)
                    } else {
                        _errorMessage.value = "Failed to load upcoming events."
                    }
                } else {
                    _errorMessage.value = "Response not successful: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun fetchFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getPastEvents()
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventsResponse = response.body()
                    if (eventsResponse != null && !eventsResponse.error) {
                        _finishedEvents.value = eventsResponse.listEvents.take(5)
                    } else {
                        _errorMessage.value = "Failed to load finished events."
                    }
                } else {
                    _errorMessage.value = "Response not successful: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }
}
