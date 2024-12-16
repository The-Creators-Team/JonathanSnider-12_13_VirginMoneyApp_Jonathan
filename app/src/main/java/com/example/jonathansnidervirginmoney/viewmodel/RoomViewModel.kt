package com.example.jonathansnidervirginmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathansnidervirginmoney.data.repository.RepositoryImpl
import com.example.jonathansnidervirginmoney.data.responsestate.RoomsResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class RoomViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val _rooms: MutableLiveData<RoomsResponseState> by lazy {
        MutableLiveData<RoomsResponseState>()
    }
    val rooms: LiveData<RoomsResponseState> = _rooms

    fun getRooms() {
        //loading state before coroutine is launched

        try {
            _rooms.postValue(RoomsResponseState.Loading)

            viewModelScope.launch() {
                val result = repositoryImpl.getRooms()
                if (result.isEmpty()) {
                    _rooms.postValue(RoomsResponseState.Fail("Failed to retrieve from the API"))
                } else {
                    _rooms.value = RoomsResponseState.Success(result)
                }
            }
        } catch (e: SocketTimeoutException) {
            _rooms.postValue(RoomsResponseState.Fail(e.message.toString()))
        } catch (e: Exception) {
            _rooms.postValue(RoomsResponseState.Fail(e.message.toString()))
        }
    }
}