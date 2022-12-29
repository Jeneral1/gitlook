package com.efs.git_look.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.efs.git_look.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier=Modifier) {
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
                actions = { Icon(painter = painterResource(id = com.efs.git_look.R.drawable.notification), contentDescription = "Notification Icon") }
            )
        }
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(it)
        ) {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(10.dp, 5.dp),
                value = "",
                onValueChange = {
                                /*TODO:  */
                                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        textAlign = TextAlign.Center
                    )
                },
                shape = RoundedCornerShape(10.dp),
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
                    Icon(painter = painterResource(id = R.drawable.search_normal), contentDescription = "Search icon")
                },
                singleLine = true,
                maxLines = 1
            )
        }
    }

}