package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Data.ExpenseDataBase
import com.example.expensetracker.Data.dao.ExpenseDao
import com.example.expensetracker.Data.model.ExpenseEntity

class AddExpenseViewModel(val dao: ExpenseDao): ViewModel() {

    suspend fun addExpense(expenseEntity: ExpenseEntity):Boolean{
        return try {
            dao.insertExpense(expenseEntity)
            true
        }catch (ex:Throwable){
            false
        }

    }
}

class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddExpenseViewModel::class.java)){
            val dao = ExpenseDataBase.getDataBase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}