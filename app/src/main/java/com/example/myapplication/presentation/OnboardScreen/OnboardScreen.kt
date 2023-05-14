package com.example.myapplication.presentation.OnboardScreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.presentation.OnboardScreen.models.OnboardCardModel
import com.example.myapplication.themeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ResourceType")
@Composable
fun OnboardScreen(
    navController: NavController,
    onboardScreenViewModel: OnboardScreenViewModel = hiltViewModel()
) {

    val models = remember {
        listOf(
            OnboardCardModel(
                image = R.drawable.move,
                title = R.string.Hi,
                text = R.string.first
            ),
            OnboardCardModel(
                image = R.drawable.gps,
                title = R.string.title_secord,
                text = R.string.second
            ),
            OnboardCardModel(
                image = R.drawable._84_hypster_barista,
                title = R.string.make_order,
                text = R.string.theree
            ),
            OnboardCardModel(
                image = R.drawable._42_reading_book,
                title = R.string.take_order,
                text = R.string.title
            ),
        )
    }

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Scaffold(
        Modifier
            .fillMaxSize(),
        backgroundColor = themeColors.background,
        topBar = {
            TopBar() { onboardScreenViewModel.leaveFromScreen(navController) }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LocalLayoutDirection.current),
                    end = it.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = it.calculateBottomPadding()
                )
        ) {
            HorizontalPager(
                pageCount = models.size,
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.pointerInput(Unit) {
                    this.detectHorizontalDragGestures { change, dragAmount ->

                        scope.launch { nextPage(scope,pagerState,models.size - 1)
                        { onboardScreenViewModel.leaveFromScreen(navController) } }
                    }
                }
            ) {currentPage ->
                val currentModel = models[currentPage]
               Column(Modifier.fillMaxSize()) {
                   AsyncImage(
                       model = currentModel.image,
                       contentDescription = "",
                       modifier = Modifier
                           .fillMaxWidth()
                           .fillMaxHeight(0.5f)
                   )

                   Column(
                       Modifier
                           .fillMaxWidth()
                           .padding(top = 10.dp),
                       horizontalAlignment = Alignment.CenterHorizontally,
                       verticalArrangement = Arrangement.spacedBy(10.dp)
                   ) {
                       Text(
                           text = stringResource(id = currentModel.title),
                           fontWeight = FontWeight.W800,
                           fontSize = 18.sp,
                           color = themeColors.primaryFontColor
                       )

                       Text(
                           text = stringResource(id = currentModel.text),
                           fontWeight = FontWeight.W800,
                           fontSize = 16.sp,
                           color = themeColors.secondaryFontColor,
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(start = 10.dp, end = 10.dp),
                           textAlign = TextAlign.Center
                       )

                       PositionWidget(currentPage,models.size)
                       
                       Button(
                           onClick = { nextPage(scope,pagerState,models.size - 1)
                           { onboardScreenViewModel.leaveFromScreen(navController) }  },
                           shape = RoundedCornerShape(10.dp),
                           colors = ButtonDefaults.buttonColors(
                               backgroundColor = themeColors.primary
                           ),
                           modifier = Modifier
                               .fillMaxHeight(0.4f)
                               .fillMaxWidth(0.4f)
                               .padding(top = 25.dp)
                       ) {
                           Text(
                               text = "Дальше",
                               color = Color.White,
                               fontWeight = FontWeight.W700
                           )
                       }
                   }
               }
            }
        }
    }

}

@Composable
private fun PositionWidget(currentPage:Int,allPages:Int) {
    val color = themeColors.primary

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp,Alignment.CenterHorizontally)
    ) {
        repeat(allPages) { currentIndex ->
            val alpha = animateFloatAsState(
                targetValue = if(currentPage == currentIndex) 1f else 0.4f
            )
            Canvas(modifier = Modifier.size(10.dp),
                onDraw = {
                    drawCircle(
                        color.copy(alpha.value)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun nextPage(scope:CoroutineScope, pagerState: PagerState, maxPage:Int, onMaxPage:() -> Unit) {
    if(pagerState.currentPage == maxPage) {
        onMaxPage()
    } else {
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
    }
}


@Composable
private fun TopBar(leaveFromScreen:() -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.Skip),
            fontSize = 14.sp,
            color = themeColors.primary,
            modifier = Modifier.clickable { leaveFromScreen() }
        )
    }
}