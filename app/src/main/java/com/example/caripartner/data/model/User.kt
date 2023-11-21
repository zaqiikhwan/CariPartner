package com.example.caripartner.data.model

data class User(
    val email:String?= null,
    val name:String?=null,
    val isVerified:Boolean?=null,
    val ktm:String?=null,
    val password:String?=null,
    val major:String?=null,
    val field:String?=null,
    val isAvailable:Boolean?=null,

    val bookmark: List<String>? = null,
    val cancel: List<String>? = null,
    val preferences: List<String>? = null
    )
