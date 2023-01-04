@file:OptIn(ExperimentalMaterial3Api::class)

package com.efs.git_look.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.efs.git_look.R
import com.efs.git_look.model.Repository
import com.efs.git_look.viewModel.RepositorySearchVM
import kotlinx.coroutines.flow.Flow
import java.io.IOException

@Composable
fun ResultRepoListView(
    viewModel: RepositorySearchVM,
    onItemClick: (String, String) -> Unit = { _: String, _: String -> },
){
    val isSearching by remember{ viewModel.isQuerying }
    if (isSearching){
        val listState = rememberLazyListState()
        val repoList: Flow<PagingData<Repository>> = viewModel.repositories
        val repoListItems: LazyPagingItems<Repository> = repoList.collectAsLazyPagingItems()

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
                    append("${repoListItems.itemCount} results")
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
                items(repoListItems) { item ->
                    item?.let {
                        RepoItemCard(repository = it, onItemClick = onItemClick)
                    }
                }

                repoListItems.apply {
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


@Composable
fun RepoItemCard(
    repository: Repository,
    onItemClick: (String, String) -> Unit
){
    ElevatedCard(
        onClick = { onItemClick(
                repository.full_name.substringBefore("/"),
            repository.full_name.substringAfter("/"))
                  },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp),
        ) {
            Column {
                //first row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Name or full_name
                    Text(
                        text = if (repository.name != null && repository.name.isNotEmpty())
                            repository.name else repository.full_name,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .align(Alignment.CenterVertically)
                    )
                    //Stars
                    val starsText = buildAnnotatedString {
                        appendInlineContent("starId")
                        append(" ${repository.stargazers_count ?: 0}")
                    }
                    val star = mapOf(
                        "starId" to InlineTextContent(
                            Placeholder(18.sp, 18.sp, PlaceholderVerticalAlign.TextCenter)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.star),
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    )
                    Text(
                        text = starsText,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        inlineContent = star
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Row 2
                Text(
                    text = repository.description ?: "No description",
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Row 3
                LanguageCards(repository.languages)
                Spacer(modifier = Modifier.height(8.dp))
                // Row 4
                val d = repository.updated_at?.take(10)
                Text(
                    text = "Updated $d",
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

@Composable
fun LanguageCards(languageMap: Map<String, Int>?){
    val langList = mutableListOf<String>()
    languageMap?.forEach {
        langList.add(it.key)
    }
    LazyRow (modifier = Modifier.fillMaxWidth()){
        items(langList) { language ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp, 3.dp)
            ) {
                Text(
                    text = language,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}