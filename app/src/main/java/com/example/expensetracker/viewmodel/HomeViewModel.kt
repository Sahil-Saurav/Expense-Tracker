package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.Data.ExpenseDataBase
import com.example.expensetracker.Data.dao.ExpenseDao
import com.example.expensetracker.Data.model.ExpenseEntity
import com.example.expensetracker.R
import com.example.expensetracker.Utils
import kotlinx.coroutines.launch

class HomeViewModel(val dao:ExpenseDao):ViewModel() {
    val expenses = dao.getAllExpense()

    fun getBalance(list: List<ExpenseEntity>):String{
        var total = 0.0
        list.forEach {
            if(it.type=="Income"){
                total += it.amount
            }else{
                total -= it.amount
            }
        }
        return "₹ ${Utils.toDecimal(total)}"
    }

    fun getTotalExpense(list: List<ExpenseEntity>):String{
        var total = 0.0
        list.forEach {
            if(it.type=="Expense"){
                total += it.amount
            }
        }
        return "₹ ${Utils.toDecimal(total)}"
    }

    fun getTotalIncome(list: List<ExpenseEntity>):String{
        var total = 0.0
        list.forEach {
            if(it.type=="Income"){
                total += it.amount
            }
        }
        return "₹ ${Utils.toDecimal(total)}"
    }

    fun deleteExpense(expenseEntity: ExpenseEntity){
        viewModelScope.launch {
            dao.deleteExpense(expenseEntity)
        }
    }
    @Composable
    fun getItemIcon(item:ExpenseEntity):Int{
        if(item.category== stringResource(id = R.string.upi)){
            return R.drawable.ic_upi
        }else if(item.category==stringResource(id = R.string.netflix)){
            return R.drawable.netflix
        }else if(item.category==stringResource(id = R.string.youtube)){
            return R.drawable.youtube
        }else if (item.category== stringResource(id = R.string.coffee)){
            return R.drawable.ic_coffee
        }else {
            return R.drawable.ic_other
        }
    }

}

/*We can skip this Fuck'all Long Shit if we have created a repository which could have accessed Dao directly and That repository can be used in viewModel*/

class HomeViewModelFactory(private val context:Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDataBase.getDataBase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

