package com.project.projectdemo.util

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun toastFragment(context: Context?,message: String){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}