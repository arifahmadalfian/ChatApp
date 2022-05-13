package com.example.chatapp.data.remote

import com.example.chatapp.data.remote.dto.MessageDto
import com.example.chatapp.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*

class MessageService(
    private val client: HttpClient
) : IMessageService {

    override suspend fun getAllMessage(): List<Message> {
        return try {
            client.get<List<MessageDto>>(IMessageService.Endpoints.GetAllMessages.url)
                .map { it.toMessage() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}