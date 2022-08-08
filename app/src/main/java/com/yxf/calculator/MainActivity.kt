package com.yxf.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yxf.calculator.ui.theme.Background
import com.yxf.calculator.ui.theme.DarkGrey
import com.yxf.calculator.ui.theme.LightGrey
import com.yxf.calculator.ui.theme.Orange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calculator()
        }
    }
}


val buttons = arrayOf(
    arrayOf("AC" to LightGrey,"+/-" to LightGrey,"%" to LightGrey,"/" to Orange),
    arrayOf("7" to DarkGrey,"8" to DarkGrey,"9" to DarkGrey,"x" to Orange),
    arrayOf("4" to DarkGrey,"5" to DarkGrey,"6" to DarkGrey,"-" to Orange),
    arrayOf("1" to DarkGrey,"2" to DarkGrey,"3" to DarkGrey,"+" to Orange),
    arrayOf("0" to DarkGrey,"Backspace" to DarkGrey,"=" to Orange),
)

data class CalculatorState(val number1 : Long = 0, val number2 : Long = 0, val result : Long = 0, val opt : String? = null)

@Preview(showSystemUi = true)
@Composable
fun Calculator(){

    var state by remember{
        mutableStateOf(CalculatorState())
    }
    Column(Modifier.background(Background).padding(horizontal = 10.dp)){
        Box(
            Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
            ){
            Text(text = state.result.toString(),
                fontSize = 100.sp,
                color = Color.White)

        }
        Column(Modifier.fillMaxSize()){
            buttons.forEach {
                Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)){
                    it.forEach{
                        CalculartorButton(
                            Modifier
                                .weight(if (it.first == "0")2f else 1f)
                                .aspectRatio(if (it.first == "0")2f else 1f)

                                .background(it.second),it.first){
                            state = calculate(state, it.first)
                        }

                    }
                }
            }


        }


    }
}

fun calculate(currentState: CalculatorState, input: String): CalculatorState {

    return when(input){

        in "0".."9" -> when(currentState.result){
            0L -> when(currentState.opt){
                null -> currentState.copy(number1 = input.toLong(), result = input.toLong())
                else -> currentState.copy(number2 = input.toLong(), result = input.toLong())
            }
            else ->when(currentState.opt){
                null -> currentState.copy(number1 = "${currentState.number1}$input".toLong(), result = "${currentState.number1}$input".toLong())
                else -> currentState.copy(number2 = "${currentState.number2}$input".toLong(), result = "${currentState.number2}$input".toLong())
            }
        }


        in arrayOf("+","-","x","/") -> currentState.copy(opt = input)

        "="-> when(currentState.opt){
            "+"->currentState.copy(result = currentState.number1 + currentState.number2, number1 = currentState.number1 + currentState.number2, opt = null)
            "-"->currentState.copy(result = currentState.number1 - currentState.number2, number1 = currentState.number1 - currentState.number2, opt = null)
            "x"->currentState.copy(result = currentState.number1 * currentState.number2, number1 = currentState.number1 * currentState.number2, opt = null)
            "/"->currentState.copy(result = currentState.number1 / currentState.number2, number1 = currentState.number1 / currentState.number2, opt = null)
            else -> currentState
        }
        "AC" -> currentState.copy(number1 = 0, number2 = 0, result = 0, opt = null)
        "+/-" -> currentState.copy(result = currentState.result.inv()+1, opt = null)
        "Backspace" -> when(currentState.result.toString().length){
            1 -> currentState.copy(result = 0,
                number2 = if(currentState.number2 == currentState.result) 0 else currentState.number2,
                number1 = if (currentState.number1 == currentState.result) 0 else currentState.number1)
            else -> currentState.copy(result = currentState.result.toString().substring(0,currentState.result.toString().length-1).toLong(),
                number2 = if(currentState.number2 == currentState.result) currentState.result.toString().substring(0,currentState.result.toString().length-1).toLong() else currentState.number2,
                number1 = if(currentState.number1 == currentState.result) currentState.result.toString().substring(0,currentState.result.toString().length-1).toLong() else currentState.number1
                )
        }

        else -> currentState
    }


}


@Composable
fun CalculartorButton(modifier: Modifier, symble : String,onClick : ()->Unit){
    Box(modifier = Modifier
        .clip(CircleShape)
        .then(modifier)
        .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center, ){
        Text(text = symble, fontSize = 40.sp,color = Color.White)
    }
}