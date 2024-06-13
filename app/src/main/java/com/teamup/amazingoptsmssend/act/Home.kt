package com.teamup.amazingoptsmssend.act

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.teamup.amazingoptsmssend.Control.Screen
import com.teamup.amazingoptsmssend.R
import com.teamup.amazingoptsmssend.api.BooleanSharedPreferences
import com.teamup.amazingoptsmssend.api.MyShared
import com.teamup.amazingoptsmssend.api.MySharedPreferences
import com.teamup.amazingoptsmssend.api.getSenderSet
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(context: Context, navController: NavHostController) {
    val Viewnow = rememberUseCaseState(visible = false, onCloseRequest = {})
    var dataText by  remember { mutableStateOf("") }
    CoreDialog(
        state = Viewnow,
        selection = CoreSelection(
            withButtonView = false,
            positiveButton = SelectionButton(
                "",
                IconSource(R.drawable.ic_launcher_background.toInt())
                ,  ButtonStyle.OUTLINED
            ),
            onPositiveClick = {

            }
        ),
        onPositiveValid = true,
        body = {

            Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {



                Text(
                    modifier = Modifier.padding(5.dp),
                    text = dataText,
                    style = TextStyle(
                        fontSize = (15.sp),
                        color = Color(0xFF000000),
                    ),
                    textAlign = TextAlign.End
                )

            }

        },
    )

    val mySharedPreferences = MySharedPreferences(context)
    val booleanSharedPreferences = BooleanSharedPreferences(context)

    var data = remember {
        mutableStateOf(mySharedPreferences.getList("key"))
    }
    var isSelect = remember {
        mutableStateOf(false)
    }
    GlobalScope.launch {
        while (true){
            delay(2000)
            data.value=  mySharedPreferences.getList("key")
        }

    }
    var text = remember {
        mutableStateOf("")
    }

    val screenWidth: Dp = LocalDensity.current.run {
        with(LocalContext.current.resources.displayMetrics) {
            widthPixels / density
        }
    }.dp



    var Sender = remember {
        mutableStateOf(getSenderSet(context).toList())
    }


    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF000000))) {

        item {
            OutlinedTextField(
                value =text.value,
                onValueChange = { text.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = Color(0xFF353535)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    containerColor = Color(0xFF353535),
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                ),
                placeholder = {
//                            Text(text = "Enter the name of the provider you want to receive messages from", color = Color.White)
                    Text(text = "Example : PayPal", color = Color(0x4BFFFFFF))
                }
            )
        }


        if(Sender.value.isNotEmpty() && isSelect.value ) {
                itemsIndexed(Sender.value){index, item ->

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                if (mySharedPreferences.containsValue("key", item)) {
                                    mySharedPreferences.removeFromList("key", item)
                                } else {
                                    mySharedPreferences.addToList("key", item)
                                    isSelect.value = !isSelect.value
                                }

                            }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

                            mySharedPreferences.containsValue("key",item)
                            Text(text = item.toString()
                                , color = Color.White, modifier = Modifier
                                    .padding(5.dp), style = TextStyle(fontSize = 17.sp)
                            )

                            if (mySharedPreferences.containsValue("key", item)){
                                AsyncImage(model = R.drawable.check, contentDescription = "", modifier = Modifier
                                    .padding(10.dp)
                                    .size(25.dp)
                                    .clickable {
                                        mySharedPreferences.removeFromList("key", item)

                                    })
                            }else{
                                AsyncImage(model = R.drawable.check2, contentDescription = "", modifier = Modifier
                                    .padding(10.dp)
                                    .size(25.dp)
                                    .clickable {
                                        mySharedPreferences.addToList("key", item)
                                        isSelect.value = !isSelect.value
                                    })

                            }

                        }


                }
            }
        item {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {

                    isSelect.value = !isSelect.value

                }
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF7986CB)
                ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                Text(text = if (isSelect.value) "Back" else "Choose from messages on the phone"
                    , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                )
            }
        }
        item {
            itemTop(img = R.drawable.speech_bubble, screenWidth = screenWidth , text = "Enter the name of the provider you want to receive messages from☝️")
        }


        item {

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {


                    mySharedPreferences.addToList("key", text.value)
                }
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF00C853)
                ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                Text(text = "addition?"
                    , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                )
            }
        }


        item {
            var switchState by remember { mutableStateOf(false) }
            var switchStateNew by remember { mutableStateOf(booleanSharedPreferences.getBoolean("24")) }


            LaunchedEffect(Unit){
                switchState = switchStateNew
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(
                        width = 3.dp,
                        Color(0xFF393B52),
                        shape = RoundedCornerShape(10.dp)
                    )
                    , horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text =  "last 24 hours only",
                        style = TextStyle(
                            fontSize = (20.sp),
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                    Switch(
                        checked = switchState,
                        onCheckedChange = {its ->

                            booleanSharedPreferences.saveBoolean("24",its)

                            switchState  =its
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0x0FF00D087),
                            checkedTrackColor = Color.White
                        ), modifier = Modifier.padding(10.dp)
                    )



                }
        }
        }


        itemsIndexed( data.value) {index, item ->
            var isSettings = remember {
                mutableStateOf(false)
            }
            var flter = remember {
                mutableStateOf("")
            }
            var switchState by remember { mutableStateOf(false) }
            var switchStateNew by remember { mutableStateOf(booleanSharedPreferences.getBoolean(item)) }


            LaunchedEffect(Unit){
                switchState = switchStateNew
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF353535)
                )) {
                Row(modifier = Modifier


                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = item
                            , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                        )
                        AsyncImage(model = if (switchState) R.drawable.green_circle else R.drawable.circle, contentDescription = "", modifier = Modifier
                            .padding(1.dp)
                            .size(10.dp)
                          )
                    }

                    Row() {
                        AsyncImage(model = R.drawable.settings, contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp)
                            .clickable {
                                isSettings.value = !isSettings.value
                            })
                        AsyncImage(model = R.drawable.new_email, contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp)
                            .clickable {
                                navController.navigate("${Screen.VSMS.idScreen}/${item.toString()}")
                            })
                        AsyncImage(model = R.drawable.dd, contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp)
                            .clickable {
                                mySharedPreferences.removeFromList("key", item)
                            })

                    }




                }


                AnimatedVisibility (isSettings.value ){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .border(
                                width = 3.dp,
                                Color(0xFF393B52),
                                shape = RoundedCornerShape(10.dp)
                            )
                            , horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

                          Text(
                                modifier = Modifier.padding(10.dp),
                                text =  "Run",
                                style = TextStyle(
                                    fontSize = (20.sp),
                                    color = Color.White
                                ),
                                textAlign = TextAlign.Center
                            )
                            Switch(
                                checked = switchState,
                                onCheckedChange = {its ->

                                    booleanSharedPreferences.saveBoolean(item,its)

                                    switchState  =its
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0x0FF00D087),
                                    checkedTrackColor = Color.White
                                ), modifier = Modifier.padding(10.dp)
                            )



                        }
                        OutlinedTextField(
                            value =flter.value,
                            onValueChange = { flter.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .background(color = Color(0xFF353535)),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                containerColor = Color(0xFF353535),
                                cursorColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color(0xFF00B8D4)
                            ),trailingIcon = {
                                AsyncImage(model = R.drawable.question, contentDescription = "", modifier = Modifier
                                    .padding(10.dp)
                                    .size(25.dp)
                                    .clickable {
                                        dataText =
                                            "This is the filter, where you can only receive messages that match the filter you set. The word must be present exactly as typed within the message for it to work. Please copy a common part from all the messages you receive from this sender and paste it here."
                                        Viewnow.show()

                                    })
                            },
                            placeholder = {
//                            Text(text = "Enter the name of the provider you want to receive messages from", color = Color.White)
                                Text(text = "Example : verification code", color = Color(0x4BFFFFFF))
                            }
                        )
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable {
                                val myShared = MyShared(context, item)
                                myShared.addValue(flter.value)
                            }
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF0091EA)
                            ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "add filter in $item?"
                                , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                            )
                        }

                        var dataflter = remember {
                            mutableStateOf(MyShared(context, item).getValueList())
                        }
                        GlobalScope.launch {
                            while (true){
                                delay(2000)
                                dataflter.value=  MyShared(context, item).getValueList()
                            }

                        }
                        LazyRow(){
                            itemsIndexed(dataflter.value){index, it ->

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .background(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF353535)
                                    )
                                    , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = it
                                        , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                                    )
                                    AsyncImage(model = R.drawable.dd, contentDescription = "", modifier = Modifier
                                        .padding(10.dp)
                                        .size(25.dp)
                                        .clickable {
                                            MyShared(context, item).removeValue(it)
                                        })

                                }
                            }
                        }


                    }


                }

            }



        }






    }
}