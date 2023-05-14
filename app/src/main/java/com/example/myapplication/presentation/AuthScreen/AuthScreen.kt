package com.example.myapplication.presentation.AuthScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W100
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.presentation.AuthScreen.models.AuthScreenState
import com.example.myapplication.themeColors

//отрисовка экрана авторизации
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    authScreenViewModel: AuthScreenViewModel = hiltViewModel()
) {
    val state by authScreenViewModel.state.collectAsState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .padding(
                start = 15.dp,
                end = 15.dp
            ),
        backgroundColor = themeColors.background,
        topBar = { TopBar(state) { authScreenViewModel.backToPhoneEnter() } }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LocalLayoutDirection.current),
                    end = it.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = it.calculateBottomPadding()
                )
        ) {
            item {
                AsyncImage(
                    model = R.drawable.auth_image,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)

                )
            }

            item {
                AnimatedContent(
                    targetState = state
                ) { screenState ->
                    when(screenState) {
                        AuthScreenState.EnterCodeState -> EnterCodeState()
                        AuthScreenState.EnterPhoneState -> EnterPhoneState(authScreenViewModel)
                    }
                }


            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnterPhoneState(
    authScreenViewModel:AuthScreenViewModel
) {
    var phoneText by rememberSaveable() {
        mutableStateOf("")
    }

    var isAgree by rememberSaveable {
        mutableStateOf(false)
    }


    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.phone),
            color = themeColors.secondaryFontColor,
            modifier = Modifier.padding(top = 10.dp)
        )

        OutlinedTextField(
            value = phoneText,
            onValueChange = {
                if(it.length > 13) return@OutlinedTextField
                if(!it.drop(1).isDigitsOnly()) return@OutlinedTextField

                phoneText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
        )

        FlowRow(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isAgree,
                onCheckedChange = { isAgree = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = themeColors.primary,
                    uncheckedColor = themeColors.primary,
                )
            )

            Text(text = stringResource(R.string.I_aggree), fontSize = 9.sp, fontWeight = W100)

            Text(text = stringResource(R.string.pc), color = Color.Blue,
                modifier = Modifier.clickable {  },fontSize = 9.sp, fontWeight = W100)

            Text(text = stringResource(R.string.and),fontSize = 9.sp, fontWeight = W100)

            Text(text = stringResource(R.string.uc)
                ,color = Color.Blue, modifier = Modifier.clickable {  },fontSize = 9.sp, fontWeight = W100)
        }

        Button(
            onClick = { authScreenViewModel.sendCode(phoneText) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 15.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = themeColors.primary,
                disabledBackgroundColor = Color.Gray
            ),
            enabled = isAgree && phoneText.length == 13 && phoneText.contains("+375")
        ) {
            Text(
                text = stringResource(R.string.Contine),
                color = Color.White,
                fontWeight = W900,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun EnterCodeState() {
    var code by rememberSaveable() {
        mutableStateOf("")
    }


    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.code),
            color = themeColors.secondaryFontColor,
            modifier = Modifier.padding(top = 10.dp)
        )

        OutlinedTextField(
            value = code,
            onValueChange = {
                if(!it.isDigitsOnly() && it.length > 6) return@OutlinedTextField

                code = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        )

        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 15.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = themeColors.primary,
                disabledBackgroundColor = Color.Gray
            ),
            enabled = code.isNotEmpty()
        ) {
            Text(
                text = stringResource(R.string.sing),
                color = Color.White,
                fontWeight = W900,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
private fun TopBar(state: AuthScreenState,onBack:() -> Unit) {


    if(state is AuthScreenState.EnterCodeState) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), contentAlignment = Alignment.CenterStart) {
            Icon(painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "",
                modifier = Modifier.size(25.dp).clickable {
                    onBack()
                }
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(R.string.sing_in),
            fontWeight = W900,
            fontSize = 18.sp,
            color = themeColors.primaryFontColor
        )
    }
}
