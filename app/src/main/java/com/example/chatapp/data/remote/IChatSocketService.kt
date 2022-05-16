package com.example.chatapp.data.remote

import com.example.chatapp.domain.model.Message
import com.example.chatapp.util.Resources
import kotlinx.coroutines.flow.Flow

interface IChatSocketService {
    suspend fun initSession(username: String): Resources<Unit>
    suspend fun sendMessage(message: String)
    fun observeMessages(): Flow<Message>
    suspend fun closeSession()
    companion object {
        const val BASE_URL = "ws://192.168.100.6:8082"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}