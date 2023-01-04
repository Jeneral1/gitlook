package com.efs.git_look

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.efs.git_look.screens.*
import com.efs.git_look.ui.theme.GitLookTheme
import com.efs.git_look.viewModel.RepositorySearchVM
import com.efs.git_look.viewModel.UserSearchVM

/**
 * Application starts from [MainActivity] which inherits [ComponentActivity]
 *
 * objects of two @see[viewModels] which are view models for the two pages of the application
 *
 * [onCreate] function marks the begin of the activity. this function is override when the
 * application starts with MainActivity
 * */
class MainActivity : ComponentActivity() {
    private val userSearchVm: UserSearchVM by viewModels()
    private val repoSearchVM: RepositorySearchVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitLookTheme {
                MainScreen(userSearchVm, repoSearchVM)
            }
        }
    }
}

@Composable
fun MainScreen(
    userSearchVM: UserSearchVM,
    repoSearchVM: RepositorySearchVM,
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        NavGraph(navController = navController, userSearchVM, repoSearchVM)
    }
}

/**
 * Bottom nav item
 *
 * @property screen_route
 * @property icon
 * @property label
 * @constructor Create empty Bottom nav item
 */
sealed class BottomNavItem(val screen_route: String, val icon: Int, @StringRes val label: Int){
    object Home : BottomNavItem("home", R.drawable.home, R.string.home)
    object Analytics : BottomNavItem("analytics", R.drawable.user, R.string.analytics)
    object Settings : BottomNavItem("settings", R.drawable.setting, R.string.settings)
}


/**
 * Nav graph
 *
 * @param navController
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    userSearchVM: UserSearchVM,
    repoSearchVM: RepositorySearchVM,
){
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screen_route,
    ){
        composable(BottomNavItem.Home.screen_route){
            HomeScreen(
                navController,
                userSearchVM,
                repoSearchVM,
                onRepoItemClick = { ownerId, repoName ->
                    navController.navigate(
                        "${GLScreens.ViewRepository.name}/$ownerId/$repoName"
                    ){
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onUserItemClick = { userId ->
                    navController.navigate(
                        "${GLScreens.ViewUser.name}/$userId"
                    ){
                        launchSingleTop = true
                        restoreState = true
                    }

                }
            )
        }
        composable(BottomNavItem.Analytics.screen_route){
            AnalyticsScreen(navController)
        }
        composable(BottomNavItem.Settings.screen_route){
            SettingsScreen(navController)
        }

        composable(
            "${GLScreens.ViewRepository.name}/{ownerId}/{repoName}",
            arguments = listOf(
                navArgument("ownerId"){type = NavType.StringType},
                navArgument("repoName"){type = NavType.StringType}
            )
        ){
            val ownerId = it.arguments?.getString("ownerId")
            val repoName = it.arguments?.getString("repoName")
            ViewRepositoryScreen(
                repoSearchVM, ownerId = ownerId,
                repoName = repoName,
                onBackClick = {
                    if (navController.previousBackStackEntry != null) navController.navigateUp()
                }
            )
        }

        composable(
            "${GLScreens.ViewUser.name}/{userId}",
            arguments = listOf(
                navArgument("userId"){type = NavType.StringType},
            )
        ){
            val userId = it.arguments?.getString("userId")
            ViewUserScreen(
                userSearchVM, userId = userId,
                onBackClick = {
                    if (navController.previousBackStackEntry != null) navController.navigateUp()
                },
                onRepoItemClick = { ownerId, repoName ->
                    navController.navigate(
                        "${GLScreens.ViewRepository.name}/$ownerId/$repoName"
                    ){
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}


/**
 * Bottom navigation
 *
 * @param navController
 */
@Composable
fun BottomNav(navController: NavHostController){
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Analytics,
        BottomNavItem.Settings
    )

    val bottomShape = AbsoluteRoundedCornerShape(30.dp,30.dp,0.dp,0.dp)
    Surface(
        shadowElevation = (8).dp,
        tonalElevation = 30.dp,
        shape = bottomShape,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight(.1f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = bottomShape
                ),
            contentAlignment = Alignment.Center
        ) {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                elevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination

                items.forEach { item ->

                    val isSelected =
                        currentRoute?.hierarchy?.any { it.route == item.screen_route } == true
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = "",
                                tint = setSelectedColor(isSelected)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(id = item.label),
                                color = setSelectedColor(isSelected)
                            )
                        },
                        selected = isSelected,
                        alwaysShowLabel = true,
                        onClick = {
                            navController.navigate(item.screen_route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }

            }
        }
    }

}

@Composable
fun setSelectedColor(itemSelected: Boolean = false): Color {
    return if (itemSelected)
        MaterialTheme.colorScheme.onSecondary
    else
        MaterialTheme.colorScheme.onSecondaryContainer
}