package com.efs.git_look.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efs.git_look.R

/**
 * No search view
 *
 */
@Composable
fun NoSearchView(){
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(50.dp)
    ) {

        Text(
            text = "What are you looking for?",
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(30.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.undraw_searching),
                contentDescription = "Image for No Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

/**
 * No result view
 *
 * @param query
 */
@Composable
fun NoResultView(query: String){
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(50.dp)
    ) {

        val text = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                append("Cannot find any search result for ")
            }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append(query)
            }

        }

        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(30.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.undraw_no_data),
                contentDescription = "Image for no data",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }

    Spacer(modifier = Modifier.height(50.dp))
}

/**
 * Error view
 *
 * @param error
 */
@Composable
fun ErrorView(error: AnnotatedString){
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(50.dp)
    ) {

        Text(
            text = error,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(30.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.undraw_no_network),
                contentDescription = "Image for no data",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

/**
 * Composable function to show loading progress effect
 *
 * @param modifier declares the behaviour of the composable children of this composable
 * */
@Composable
fun Loading(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(10.dp),
        Alignment.TopCenter
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onTertiary
        )
    }

}