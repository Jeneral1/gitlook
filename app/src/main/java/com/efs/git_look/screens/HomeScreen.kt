package com.efs.git_look.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.efs.git_look.BottomNav
import com.efs.git_look.R
import com.efs.git_look.viewModel.RepositorySearchVM
import com.efs.git_look.viewModel.UserSearchVM

/**
 * Home screen
 *
 * @param userSearchVM
 * @param repoSearchVM
 * @param onUserItemClick
 * @param onRepoItemClick
 * @receiver
 * @receiver
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    userSearchVM: UserSearchVM,
    repoSearchVM: RepositorySearchVM,
    onUserItemClick: (String) -> Unit = {},
    onRepoItemClick: (String, String) -> Unit = { s: String, s1: String -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Home",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = { Icon(painter = painterResource(id = R.drawable.notification), contentDescription = "Notification Icon") }
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNav(navController = navController) },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { it ->

        //val searchQuery by userSearchVM.queryy.collectAsState()
        //val searchQuery by remember { userSearchVM.query}
        var tabIndex by remember { mutableStateOf(0) }
        var queryChanged by remember{ mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(10.dp, 5.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        textAlign = TextAlign.Center
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                ),
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.search_normal),
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer)
                },
                singleLine = true,
                maxLines = 1,
                value = userSearchVM.query.value,
                onValueChange = {
                    userSearchVM.query.value = it
                    repoSearchVM.query.value = it
                    /*if (it.isEmpty()) isSearching=false*/
                    queryChanged = true
                    userSearchVM.isQuerying.value = false
                    repoSearchVM.isQuerying.value = false
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        if(tabIndex == 0){
                            userSearchVM.refresh()
                            userSearchVM.isQuerying.value = true
                            /*isSearching = true*/
                        }
                        if(tabIndex==1){
                            repoSearchVM.refresh()
                            repoSearchVM.isQuerying.value = true
                        }
                        
                    }
                )
            )
            
            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp, 0.dp),
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(
                            currentTabPosition = tabPositions[tabIndex]
                        ),
                        color = MaterialTheme.colorScheme.onSecondary

                    )
                },
                divider = {}
            ) {
                Tab(selected = tabIndex==0,
                    text = { 
                        Text(text = "Users",
                            fontSize= 17.sp,
                            fontWeight = FontWeight.Medium) },
                    onClick = {
                        tabIndex = 0
                    /*TODO*/
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Tab(selected = tabIndex==1,
                    text = { 
                        Text(text = "Repositories",
                            fontSize= 17.sp,
                            fontWeight = FontWeight.Medium) },
                    onClick = {
                        tabIndex = 1
                    /*TODO*/
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onSecondary,
                    unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Page to open when tab is selected
            when (tabIndex) {
                0 -> {
                    ResultUserListView(
                        viewModel = userSearchVM,
                        onItemClick = onUserItemClick
                    )
                }
                1 -> {
                    ResultRepoListView(
                        viewModel = repoSearchVM,
                        onItemClick = onRepoItemClick
                    )
                }
                else -> NoSearchView()
            }
        }
    }

}


