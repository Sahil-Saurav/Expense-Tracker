package com.example.expensetracker.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expensetracker.Data.dao.ExpenseDao
import com.example.expensetracker.Data.model.ExpenseEntity



@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase:RoomDatabase(){
    abstract fun expenseDao():ExpenseDao

    companion object{
        const val DATABASE_NAME =   "expense_database.db"

        @JvmStatic
        fun getDataBase(context: Context):ExpenseDataBase{
            return Room.databaseBuilder(
                context,
                ExpenseDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}