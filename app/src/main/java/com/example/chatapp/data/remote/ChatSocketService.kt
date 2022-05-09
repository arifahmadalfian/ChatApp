package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.dto.MessageDto
import com.example.chatapp.domain.model.Message
import com.example.chatapp.util.Resources
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketService(
    private val client: HttpClient
) : IChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(username: String): Resources<Unit> {
        return try {
            socket = client.webSocketSession {
                url(IChatSocketService.Endpoints.ChatSocket.url)
            }
            if (socket?.isActive == true) {
                Resources.Success(Unit)
            } else Resources.Error("Couldn't establish a connection.")
        } catch (e: Exception) {
            e.printStackTrace()
            Resources.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                } ?: flow {  }
        } catch (e: Exception) {
            e.printStackTrace()
            flow {  }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}