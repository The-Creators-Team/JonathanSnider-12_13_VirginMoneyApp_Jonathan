package com.example.jonathansnidervirginmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathansnidervirginmoney.data.repository.RepositoryImpl
import com.example.jonathansnidervirginmoney.data.responsestate.UserResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val _users: MutableLiveData<UserResponseState> by lazy {
        MutableLiveData<UserResponseState>()
    }

    val users: LiveData<UserResponseState> =_users

    fun getUsers(){
        try {
            _users.postValue(UserResponseState.Loading)

            //adding the Dispatchers.IO argument means this coroutine will run on the IO
            //thread, which most API and database calls should
            viewModelScope.launch(Dispatchers.IO) {
                val result = repositoryImpl.getUsers()
                if (result.isEmpty()) {
                    _users.postValue(UserResponseState.Fail("Failed to retrieve from the API"))
                } else {
                    _users.value = UserResponseState.Success(result)
                }
            }
        } catch (e: SocketTimeoutException) {
            _users.postValue(UserResponseState.Fail(e.message.toString()))
        } catch (e: Exception) {
            _users.postValue(UserResponseState.Fail(e.message.toString()))
        }
    }

}