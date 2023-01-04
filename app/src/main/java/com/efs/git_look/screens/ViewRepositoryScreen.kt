@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.efs.git_look.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efs.git_look.R
import com.efs.git_look.ui.theme.*
import com.efs.git_look.viewModel.RepositorySearchVM
import java.util.*

@Composable
fun ViewRepositoryScreen(
    repoSearchVM: RepositorySearchVM,
    ownerId: String?,
    repoName: String?,
    onBackClick: ()-> Unit = {}
){

    val isRLoading by remember{ repoSearchVM.isRepoLoading }

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
                actions = {
                    //Stars
                    val starsText = buildAnnotatedString {
                        appendInlineContent("starId")
                        append(" ${repoSearchVM.repository?.stargazers_count}")
                    }
                    val star = mapOf(
                        "starId" to InlineTextContent(
                            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter)
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
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        inlineContent = star
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
    ) { containerPadding ->

        Box(modifier = Modifier
            .padding(containerPadding)) {
            LaunchedEffect(key1 = "$ownerId/$repoName"){
                repoSearchVM.getRepository("https://api.github.com/repos/$ownerId/$repoName")
            }

            if(isRLoading){
                Loading()
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(15.dp)
                ) {
                    //first Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        //Name or full_name
                        val name = if (repoSearchVM.repository?.name != null && repoSearchVM.repository?.name!!.isNotEmpty())
                            repoSearchVM.repository!!.name else repoSearchVM.repository?.full_name
                        Text(
                            text = name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
                                ?: "",
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .align(Alignment.CenterVertically)
                        )

                        //visibility
                        Box(
                            modifier = Modifier
                                .wrapContentWidth(Alignment.End)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(10.dp, 5.dp)
                        ) {
                            repoSearchVM.repository?.visibility?.let { it1 ->
                                Text(
                                    text = it1.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ENGLISH
                                        ) else it.toString()
                                    },
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.End,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.wrapContentWidth(Alignment.End),
                                    maxLines = 3,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))


                    //Second Row
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
                                Text(text = "About",
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

                    Text(text = "Description",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = repoSearchVM.repository?.description?:"",
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 10,
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    repoSearchVM.repository?.languages.let {
                        LanguageCards(languageMap = it)
                        //LanguageCards(languageMap = repoSearchVM.repository?.languages)
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    val releases = repoSearchVM.repository?.releases
                    Text(text = "Releases (${releases?.size?:0})",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    if(repoSearchVM.repository?.releases?.isNotEmpty() == true){
                        Text(text = repoSearchVM.repository?.releases?.get(0)?.name.toString(),
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 10,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = repoSearchVM.repository?.releases?.get(0)?.published_at.toString().take(10),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 10,
                        )
                    }


                    Spacer(modifier = Modifier.height(20.dp))


                    Text(text = "Languages",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(20.dp))


                    repoSearchVM.repository?.languages?.let { langMap ->

                        val total = langMap.values.sum()
                        val rest = total - langMap.values.take(3).sum()

                        val viableColors =  mutableListOf<Color>()
                        viableColors.add(c1)
                        viableColors.add(c2)
                        viableColors.add(c3)
                        viableColors.add(c4)
                        viableColors.add(c5)

                        val viableBackground =  mutableListOf<Color>()
                        viableBackground.add(b1)
                        viableBackground.add(b2)
                        viableBackground.add(b3)
                        viableBackground.add(b4)
                        viableBackground.add(b5)

                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp), contentDescription = "") {
                            val canvasWidth = size.width
                            val canvasHeight = size.height

                            var currentX = 0f
                            langMap.values.take(3).forEachIndexed { index, value ->
                                val width = (value * canvasWidth)/total

                                // Draw Rectangles
                                drawRoundRect(
                                    color = viableColors[index], topLeft = Offset(currentX, 0f), size = Size(
                                        width,
                                        canvasHeight
                                    ),
                                    cornerRadius = CornerRadius(0.1f,0f)
                                )

                                // Update start position of next rectangle
                                currentX += width
                            }

                            //Draw the rest
                            val width = (rest * canvasWidth)/total
                            drawRoundRect(
                                color = viableColors[4], topLeft = Offset(currentX, 0f), size = Size(
                                    width,
                                    canvasHeight
                                ),
                                cornerRadius = CornerRadius(0.1f,0f)
                            )

                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        val langList = mutableListOf<String>()
                        langMap.forEach { lang ->
                            langList.add(lang.key)
                        }
                        LazyRow (modifier = Modifier.fillMaxWidth()){
                            var index = 0
                            items(langList.take(3)) { language ->

                                val num = langMap[language]
                                val percent = String.format("%.1f", (num?.times(100f))?.div(total))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(Alignment.CenterVertically)
                                        .background(
                                            color = viableBackground[index],
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(10.dp, 3.dp)
                                ) {
                                    Text(
                                        text = "$language $percent%",
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Start,
                                        fontSize = 16.sp,
                                        color = viableColors[index],
                                        modifier = Modifier.fillMaxWidth(),
                                        maxLines = 1,
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                index++
                            }
                        }

                    }
                }
            }
        }
    }


}