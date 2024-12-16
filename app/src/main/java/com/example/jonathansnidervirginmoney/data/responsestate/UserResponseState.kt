package com.example.jonathansnidervirginmoney.data.responsestate

import com.example.jonathansnidervirginmoney.data.model.Users

sealed class UserResponseState {

    //loading
    object Loading: UserResponseState()

    //success
    //ideally, this success response should be generic for dealing with a variety of API calls,
    //but since we only have one API call for now we can refer to the model by name
    data class Success(val result: Users): UserResponseState()

    //failure
    data class Fail(val failureString: String): UserResponseState()
}