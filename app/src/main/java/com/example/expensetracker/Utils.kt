package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun dateFormatter(dateinmillis:Long):String{

        val dateformat = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateformat.format(dateinmillis)
    }

    fun toDecimal(decimal:Double):String{
        return String.format("%.2f",decimal)
    }
}