package com.example.expensetracker

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.AddExpenseViewModel
import com.example.expensetracker.viewmodel.AddExpenseViewModelFactory
import com.example.expensetracker.widgets.ExpenseTextView
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController){
    val context = LocalContext.current
    val
            viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar2), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                Image(
                    painter = painterResource(id = R.drawable.ic_backbutton),
                    contentDescription =null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { navController.navigateUp() })
                ExpenseTextView(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center))
                /*Image(
                    painter = painterResource(id = R.drawable.dots),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd))*/
            }
            DataForm(modifier = Modifier
                .padding(16.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onAddExpenseClick = {
                    scope.launch {
                        if(it.title.isNotEmpty()){
                            viewModel.addExpense(it)
                            navController.navigate("home")
                        }else{
                            Toast.makeText(context,"Fill All the Entries!",Toast.LENGTH_SHORT).show()
                        }
                    }
            })
        }
    }
}

@Composable
fun DataForm(modifier: Modifier,onAddExpenseClick:(model:ExpenseEntity)->Unit){
    var name by remember {
        mutableStateOf("")
    }
    var amount by remember {
        mutableStateOf("0.00")
    }
    val date = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }
    val datePickerVisibility = remember {
        mutableStateOf(false)
    }
    val category = remember {
        mutableStateOf("UPI")
    }
    val type = remember {
        mutableStateOf("Expense")
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        ExpenseTextView(text = "Title:", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(value = name, onValueChange = {name=it}, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = "Amount:", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = {amount=it},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        Spacer(modifier = Modifier.size(4.dp))
        //Date
        ExpenseTextView(text = "Date:", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.size(4.dp))
        OutlinedTextField(
            value = Utils.dateFormatter(date.value),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerVisibility.value = true },
            enabled = false,
            trailingIcon = { androidx.compose.material3.Icon(
                imageVector = Icons.Filled.DateRange,
                tint = Zinc,
                contentDescription =null)})
        //Category
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = "Category:", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(listOf(stringResource(id = R.string.upi),stringResource(id = R.string.netflix),stringResource(id = R.string.youtube),
            stringResource(id = R.string.coffee),
            stringResource(id = R.string.other)),
            onItemSelect = {category.value=it})
        //Type
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = "Type:", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(listOf("Expense","Income"), onItemSelect = {type.value=it})
        Spacer(modifier = Modifier.size(4.dp))
        Button(
            onClick = {
                      val model = ExpenseEntity(
                          null,
                          name,
                          amount.toDoubleOrNull()?:0.0,
                          Utils.dateFormatter(date.value),
                          category.value,
                          type.value)
                    onAddExpenseClick(model)
            },
            colors = ButtonDefaults.buttonColors(Zinc),
            modifier = Modifier
                .fillMaxWidth()) {
            ExpenseTextView(
                text = "Add Expense",
                fontSize = 14.sp,
                color = Color.White)
        }
    }
    if (datePickerVisibility.value){
        ExpenseDatePicker(
            onDateSelected = {
                date.value=it
                datePickerVisibility.value = false
            },
            onDismiss = {datePickerVisibility.value = false})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePicker(
    onDateSelected:(date:Long)->Unit,
    onDismiss:()->Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    
    DatePickerDialog(onDismissRequest = {onDismiss()},
        confirmButton = { TextButton(onClick = {onDateSelected(selectedDate)}) {
            ExpenseTextView(text = "Confirm")
        } 
     },
        dismissButton = { TextButton(onClick = {onDateSelected(selectedDate)}) {
            ExpenseTextView(text = "Cancel")
        }
     }
    ) {
        DatePicker(
            state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(list:List<String>,onItemSelect:(String)->Unit){
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf<String>(list[0])
    }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value=!expanded.value}) {
        TextField(
            value = selectedItem.value, onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = { ExpandedIcon(expanded = expanded.value)},
            enabled = false,
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {expanded.value=false},
            modifier = Modifier.verticalScroll(rememberScrollState())) {
            list.forEach { 
                DropdownMenuItem(text = { ExpenseTextView(text = it)}, onClick = {
                    selectedItem.value=it
                    onItemSelect(selectedItem.value)
                    expanded.value=false
                })
            }
        }
    }
}

@Composable
fun ExpandedIcon(expanded:Boolean){
    Icon(imageVector =if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription =null,
        tint = Zinc)
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpense(rememberNavController())
}