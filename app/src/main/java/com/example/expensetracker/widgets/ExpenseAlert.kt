package com.example.expensetracker.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.ui.theme.Secondary
import com.example.expensetracker.ui.theme.Zinc

@Composable
fun ExpenseAlert(title:String,desc:String,onConfirm:()->Unit,onDismiss:()->Unit){
    AlertDialog(
        icon = { Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Zinc
        )
        },
        title={ ExpenseTextView(
            text = title)},
        text ={ ExpenseTextView(
            text = desc,
            fontSize = 16.sp)} ,
        containerColor = Color(Secondary.value),
        onDismissRequest = {onDismiss()},
        confirmButton = {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    colors = ButtonDefaults.buttonColors(Zinc),
                    onClick = {
                        onConfirm()
                    })
                {
                    ExpenseTextView(text = "Delete")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Zinc),
                    onClick = {onDismiss()}) {
                    ExpenseTextView(text = "Cancel")
                }
            }
        })
}