package com.project.projectdemo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class User(
    @PrimaryKey
    @NotNull
    var email: String,
    var name: String ?= null,
    var age: Int ?= null,
    var gender: String ?= null,
    var city: String ?= null,
    var password: String ?= null,
    var created_at: String ?= null,
    var updated_at: String ?= null

)