package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.ui.theme.Secondary
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.viewmodel.HomeViewModel
import com.example.expensetracker.viewmodel.HomeViewModelFactory
import com.example.expensetracker.widgets.ExpenseAlert
import com.example.expensetracker.widgets.ExpenseTextView

@Composable
fun HomeScreen(navController: NavController){
    
    val viewModel:HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    var resetDialogOpen by remember {
        mutableStateOf(false)
    }
    val state = viewModel.expenses.collectAsState(initial = emptyList())
    Surface(
        modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar,add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar2), contentDescription = null,

                modifier = Modifier
                    .constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Column {
                    //ExpenseTextView(text = "Hello", fontSize = 16.sp, color = Color.White)
                    ExpenseTextView(
                        text = "Expense Tracker",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    MenuDropDown(
                        onClick = {resetDialogOpen = true})
                }
            }

            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)
            CardItem(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },balance,income,expenses)
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },list= state.value,viewModel)
            Image(
                painter = painterResource(id = R.drawable.ic_floating),
                contentDescription = "add",
                alignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(add) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(32.dp)
                    .clickable { navController.navigate("add") })

        }
        if (resetDialogOpen){
            ExpenseAlert(title = "Reset Tracker!!",
                desc = "Your All The Data Will Be Reset",
                onConfirm = {
                    state.value.forEach { item->
                        viewModel.deleteExpense(item)
                    }
                    resetDialogOpen = false
                },
                onDismiss = {resetDialogOpen = false}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDropDown(onClick: () -> Unit){
    var expand by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expand,
        onExpandedChange = {expand = !expand}) {
        Image(
            painter = painterResource(id = R.drawable.dots),
            contentDescription = null,
            modifier = Modifier
                .menuAnchor())
        ExposedDropdownMenu(
            expanded = expand,
            onDismissRequest = {expand = false},
            modifier = Modifier
                .size(width = 150.dp, height = 60.dp)
                .padding(2.dp)
                .background(Secondary)){
            DropdownMenuItem(
                text = { ExpenseTextView(
                    text = stringResource(id = R.string.delete),
                    fontSize = 16.sp)},
                colors = MenuDefaults.itemColors(Zinc),
                modifier = Modifier.background(Color.Transparent),
                onClick = {
                    onClick()
                    expand = false
            })
        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expenses: String){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(20.dp)
        .clip(RoundedCornerShape(16.dp))
        .height(200.dp)
        .background(Zinc)
        .padding(16.dp)
    ) {
       Box (modifier= Modifier
           .fillMaxWidth()
           .weight(1f)){
            Column() {
                ExpenseTextView(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                ExpenseTextView(
                    text = balance,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold)

            }
            /*Image(
                painter = painterResource(id = R.drawable.dots),
                contentDescription = "Menu",
                modifier=Modifier.align(Alignment.CenterEnd)
            )*/
        }
        Box(modifier= Modifier
            .fillMaxWidth()
            .weight(1f),
            ){
                CardRowItem(
                    modifier = Modifier.align(Alignment.CenterStart),
                    title = "Income",
                    amount = income,
                    image = R.drawable.ic_income)
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expenses ,
                image = R.drawable.ic_expense)
        }
    }
}

@Composable
fun TransactionList(modifier:Modifier,list: List<ExpenseEntity>,viewModel: HomeViewModel){
    var expenseToBeDeleted by remember {
        mutableStateOf<ExpenseEntity?>(null)
    }
    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    LazyColumn(modifier=modifier.padding(horizontal = 16.dp)) {
            item {
                Box(Modifier.fillMaxWidth()){
                    ExpenseTextView(text = "Recent transaction", fontSize = 20.sp,)
                    ExpenseTextView(
                        text ="Amount",
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            items(list, key = {item->item.id?:0}){item->
                TransactionItem(
                    title = item.title,
                    amount = Utils.toDecimal(item.amount),
                    icon = viewModel.getItemIcon(item),
                    date = item.date,
                    color = if(item.type=="Income") Color.Green else Color.Red,
                    onClick = {
                        expenseToBeDeleted=item
                        isDialogOpen=true}
                )
            }
        }
    if (isDialogOpen){
        ExpenseAlert(title = "Delete",
            desc = "Expense Will Be Permanently Deleted!!",
            onConfirm = {
                expenseToBeDeleted?.let{ viewModel.deleteExpense(it)
                    expenseToBeDeleted = null
                    isDialogOpen=false}
            },
            onDismiss = {isDialogOpen = false}
        )
    }

}

@Composable
fun TransactionItem(title:String,amount:String,icon:Int,date:String,color: Color,onClick:()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .clickable {
            onClick()
        }){
        Row(){
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                ExpenseTextView(text = date, fontSize = 12.sp)
            }
        }
        ExpenseTextView(
            text = "₹${amount}",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color,
            fontWeight = FontWeight.SemiBold
            )
    }
}

@Composable
fun CardRowItem(modifier:Modifier,title:String,amount:String,image:Int){
    Column(modifier=modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, fontSize = 16.sp, color = Color.White)
        }
        ExpenseTextView(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen(rememberNavController())
}

/*AlertDialog(
            icon = { Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Zinc)
            },
            title={ ExpenseTextView(
                text = "Delete")},
            text ={ ExpenseTextView(
                text = "Expense Will Be Permanently Deleted!!",
                fontSize = 16.sp)} ,
            containerColor = Color(Secondary.value),
            onDismissRequest = {isDialogOpen=false},
            confirmButton = {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        colors = ButtonDefaults.buttonColors(Zinc),
                        onClick = {
                                expenseToBeDeleted?.let{ viewModel.deleteExpense(it)
                                expenseToBeDeleted = null
                                isDialogOpen=false}
                        })
                    {
                        ExpenseTextView(text = "Delete")
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(Zinc),
                        onClick = {isDialogOpen=false}) {
                        ExpenseTextView(text = "Cancel")
                    }
                }
            })*/