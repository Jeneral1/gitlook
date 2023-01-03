@file:OptIn(ExperimentalMaterial3Api::class)

package com.efs.git_look.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.efs.git_look.R
import com.efs.git_look.model.User
import com.efs.git_look.viewModel.UserSearchVM
import kotlinx.coroutines.flow.Flow
import java.io.IOException

/**
 * Result user list view
 *
 * @param viewModel
 * @param onItemClick
 * @receiver
 */
@Composable
fun ResultUserListView(
    viewModel: UserSearchVM,
    onItemClick: (String) -> Unit = {},
){
    val isSearching by remember{ viewModel.isQuerying }
    if (isSearching){
        val listState = rememberLazyListState()
        val userList: Flow<PagingData<User>> = viewModel.user
        val userListItems: LazyPagingItems<User> = userList.collectAsLazyPagingItems()

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)
        ) {

            val text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                    append("Showing ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append("${userListItems.itemCount} results")
                }
                /*withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)){
                    append(" out of ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)){
                    append("$totalResult results ")
                }*/
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                    append(" for ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                    append(viewModel.query.value)
                }
            }

            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(state = listState) {
                items(userListItems) { item ->
                    item?.let {
                        UserItemCard(user = it, onItemClick = onItemClick)
                    }
                }

                userListItems.apply {
                    when {
                        loadState.prepend is LoadState.Loading -> {
                            item { Loading() }
                        }
                        loadState.prepend is LoadState.Error
                                || loadState.refresh is LoadState.Error -> {
                            item {
                                val errorText = buildAnnotatedString {
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                                        append("Error! ")
                                    }
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                        if ((loadState.refresh as LoadState.Error).error is IOException)
                                            append("Check connection")
                                        else
                                            append("Something went wrong")
                                    }

                                }
                                ErrorView(
                                    error = errorText
                                )
                            }
                        }
                        loadState.refresh is LoadState.Loading -> {
                            item { Loading() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { Loading() }
                        }
                        /*loadState.prepend is LoadState.NotLoading -> {
                            item { NoSearchView() }
                        }*/
                        itemCount == 0 -> {
                            item { NoResultView(query = viewModel.query.value) }
                        }
                        /*else -> {
                            item{ NoSearchView() }
                        }*/
                    }
                }
            }

        }
    }else{
        NoSearchView()
    }
}

/**
 * User item card
 *
 * @param user
 * @param onItemClick
 * @receiver
 */
@Composable
fun UserItemCard(
    user: User,
    onItemClick: (String) -> Unit
){
    ElevatedCard(
        onClick = { onItemClick(user.url) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {
            Column {
                //first row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        //Avatar or placeholder
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .crossfade(true)
                                    .data(user.avatar_url)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.octocat),
                                filterQuality = FilterQuality.Medium
                            ),
                            contentDescription = "person avatar",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        //Name or login
                        Text(
                            text = if (user.name != null && user.name.isNotEmpty())
                                user.name else user.login,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    //Followers
                    val follow = if (user.followers == 1) "${user.followers} follower"
                    else "${user.followers} followers"
                    Text(
                        text = follow,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Row 2
                Text(
                    text = user.bio ?: "No bio set",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Row 3
                RandomStackCards()
                Spacer(modifier = Modifier.height(8.dp))
                // Row 4
                val d = user.updated_at?.take(10)
                Text(
                    text = user.location ?: "Updated $d",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Random stack cards
 *
 * @param number
 */
@Composable
fun RandomStackCards(number: Int = (2..4).random()){
    val stackList = listOf("frontend","backend", "full stack", "java", "html/css", "kotlin", ".net", "c++")
    LazyRow {
        items(number) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp, 3.dp)
            ) {
                Text(
                    text = stackList.random(),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}