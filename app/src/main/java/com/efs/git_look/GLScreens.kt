package com.efs.git_look

enum class GLScreens {
    MainScreen,
    ViewUser,
    ViewRepository;

    companion object{
        fun fromRoute(route: String?): GLScreens =
            when (route?.substringBefore("/")){
                MainScreen.name -> MainScreen
                ViewUser.name -> ViewUser
                ViewRepository.name -> ViewRepository
                null -> MainScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}