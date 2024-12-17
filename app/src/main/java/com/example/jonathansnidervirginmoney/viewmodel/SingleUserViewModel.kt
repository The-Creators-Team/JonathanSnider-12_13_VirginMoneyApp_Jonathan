package com.example.jonathansnidervirginmoney.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jonathansnidervirginmoney.data.model.UsersItemModel
import com.example.jonathansnidervirginmoney.data.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SingleUserViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val _user = MutableLiveData<UsersItemModel>()
    val user: LiveData<UsersItemModel> get()= _user

    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val response = repositoryImpl.getUserById(id)
                _user.postValue(response)
            } catch(e: Exception) {
                Log.e("SingleUserViewModel", "Exception: $e")
            }
        }
    }

}