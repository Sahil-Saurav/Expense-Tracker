package com.example.expensetracker.Data.model

import android.icu.text.CaseMap.Title
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val title:String,
    val amount:Double,
    val date:String,
    val category:String,
    val type:String
)
