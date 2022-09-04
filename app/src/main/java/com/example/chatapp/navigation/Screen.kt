package com.example.chatapp.navigation

const val CHAT_URI = "https://ariefahmadalfian.com"
const val CHAT_ARGS = "chat_args"
const val CHAT_USERNAME = "chat_username"

sealed class Screen(val route: String) {
    object HomeScreen: Screen(route = "home_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object ChatScreen: Screen(route = "chat_screen/{$CHAT_USERNAME}")
    object Details: Screen(route = "details/{$CHAT_ARGS}"){
        fun passArgument(message: String) = "details/$message"
    }
}
