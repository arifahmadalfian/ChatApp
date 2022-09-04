package com.example.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.chatapp.presentation.chat.ChatScreen
import com.example.chatapp.presentation.detail.DetailScreen
import com.example.chatapp.presentation.notification.NotificationScreen
import com.example.chatapp.presentation.username.UsernameScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.NotificationScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            UsernameScreen(onNavigate = navController::navigate)
        }
        composable(
            route = Screen.ChatScreen.route,
            arguments = listOf(
                navArgument(name = CHAT_USERNAME) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val username = it.arguments?.getString(CHAT_USERNAME)
            ChatScreen(username = username)
        }
        composable(Screen.NotificationScreen.route) {
            NotificationScreen(navController = navController)
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(name = CHAT_ARGS) { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "$CHAT_URI/$CHAT_ARGS={$CHAT_ARGS}" })
        ) {
            val argument = it.arguments
            argument?.getString(CHAT_ARGS)?.let { message ->
                DetailScreen(message = message)
            }
        }
    }
}