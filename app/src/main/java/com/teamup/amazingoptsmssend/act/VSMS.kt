package com.teamup.amazingoptsmssend.act

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.teamup.amazingoptsmssend.R
import com.teamup.amazingoptsmssend.api.SharedPreferencesHelper
import com.teamup.amazingoptsmssend.api.getSMSListLastHour
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun vsms(index: String?, context: Context) {
    var data = remember {
        mutableStateOf(getSMSListLastHour(index!!,context))
    }

    val helper = SharedPreferencesHelper(context)

    LazyColumn(modifier = Modifier
        .background(color = Color.Black)
        .fillMaxSize()){


        if (data.value.isNotEmpty()) itemsIndexed(data.value){index1, item ->
            val data = remember {
                mutableStateOf(helper.containsValue(index!!, item))
            }

            GlobalScope.launch {
                while (true){
                    delay(2000)
                    data.value = helper.containsValue(index!!, item)
                }
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = if (data.value) Color(
                        0xFF00C853
                    ) else Color(0xFF353535)
                )
            , horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                Text(text = item
                    , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                )

                if (data.value) Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFF353535))
                , horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "sent succesfully"
                        , color = Color.White, modifier = Modifier.padding(5.dp), style = TextStyle(fontSize = 17.sp)
                    )

                    Row() {
                        AsyncImage(model = R.drawable.refresh, contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp).clickable {
                                helper.removeFromList(index!!, item)
                            }
                        )
                        AsyncImage(model = R.drawable.check, contentDescription = "", modifier = Modifier
                            .padding(10.dp)
                            .size(25.dp)
                        )
                    }

                }
            }

        }
    }

}