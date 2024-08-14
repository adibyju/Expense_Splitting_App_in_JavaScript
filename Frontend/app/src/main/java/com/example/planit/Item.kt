package com.example.planit

data class Item(val id: Int, val name:String, var isChecked: Boolean = false, var description: String = "", var expense: Double = 0.0)
