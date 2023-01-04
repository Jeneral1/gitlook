@file:OptIn(ExperimentalMaterial3Api::class)

package com.efs.git_look.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.efs.git_look.R
import com.efs.git_look.viewModel.UserSearchVM
import kotlinx.coroutines.launch

@Composable
fun ViewUserScreen(
    userSearchVm: UserSearchVM,
    userId: String?,
    onBackClick: () -> Unit = {},
    onRepoItemClick: (String, String) -> Unit = { _: String, _: String -> },
){
    val scope = rememberCoroutineScope()
    val isLoading by remember{ userSearchVm.isUserLoading }
    val isRLoading by remember{ userSearchVm.isRepoLoading }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                modifier = Modifier.padding(10.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Image(modifier = Modifier.size(40.dp)
                            ,painter = painterResource(id = R.drawable.back), contentDescription = "Back btn")
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { containerPadding ->

        Box(
            modifier = Modifier
                .padding(containerPadding)
        ) {
            LaunchedEffect(key1 = userId){
                userSearchVm.getUser("https://api.github.com/users/$userId")
            }

            if(isLoading){
                Loading()
            }else{

                //Avatar or placeholder
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(15.dp, 0.dp)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.2f)
                            .border(
                                width = 5.dp,
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surface
                            ),

                        ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .crossfade(true)
                                    .data(userSearchVm.user?.avatar_url)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.octocat),
                                filterQuality = FilterQuality.Medium
                            ),
                            contentDescription = "person avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .fillMaxHeight(.2f)
                        )
                    }


                    Spacer(modifier = Modifier.height(15.dp))
                    //Name or login
                    Text(
                        text = (if (userSearchVm.user?.name != null && userSearchVm.user?.name!!.isNotEmpty())
                            userSearchVm.user?.name else userSearchVm.user?.login).toString(),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    // Row 2
                    Text(
                        text = userSearchVm.user?.bio ?: "No bio set",
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    // Row 3

                    Row {
                        //Avatar or placeholder
                        Image(painter = painterResource(id = R.drawable.profile),
                            contentDescription = "follow icon",
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        //Name or login
                        val follow = if (userSearchVm.user?.followers == 1) "${userSearchVm.user?.followers} Follower"
                        else "${userSearchVm.user?.followers} Followers"
                        Text(
                            text = follow,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            Modifier
                                .size(5.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    shape = CircleShape
                                )
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        //Name or login
                        Text(
                            text = "${userSearchVm.user?.following} Following",
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    // Row 3

                    Row {
                        //Avatar or placeholder
                        Image(
                            painter = painterResource(id = R.drawable.map_pin),
                            contentDescription = "pin icon",
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        //Name or login
                        Text(
                            text = userSearchVm.user?.location?: "No location set",
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .align(Alignment.CenterVertically)
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    ScrollableTabRow(
                        selectedTabIndex = 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(0.dp, 0.dp),
                        edgePadding = 0.dp,
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(
                                    currentTabPosition = tabPositions[0]
                                ),
                                color = MaterialTheme.colorScheme.onSecondary

                            )
                        },
                        divider = { Divider(color = MaterialTheme.colorScheme.outlineVariant) }
                    ) {
                        Tab(selected = true,
                            text = {
                                Text(text = "Repositories",
                                    fontSize= 17.sp,
                                    fontWeight = FontWeight.Medium) },
                            onClick = {},
                            enabled = false,
                            selectedContentColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    Spacer(modifier = Modifier.height(20.dp))

                    LaunchedEffect(key1 = userId){
                        scope.launch {
                            userSearchVm.getUserRepos(userSearchVm.userRepoUrl)
                        }
                    }

                    if(isRLoading){
                        Loading()
                    }else{
                        Box {
                            LazyColumn(Modifier.fillMaxHeight()) {
                                items(userSearchVm.userRepos) { repos ->
                                    RepoItemCard(repository = repos, onItemClick = onRepoItemClick)
                                }
                            }
                        }

                    }


                }
            }

        }

    }
}


