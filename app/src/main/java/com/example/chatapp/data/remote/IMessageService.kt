package com.example.chatapp.data.remote

import com.example.chatapp.domain.model.Message

interface IMessageService {
    suspend fun getAllMessage(): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.100.6:8082"
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }
}
