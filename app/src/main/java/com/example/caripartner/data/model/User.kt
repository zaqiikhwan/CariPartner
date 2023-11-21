package com.example.caripartner.data.model

data class User(val email:String? = null, val name:String?=null, val isVerified:Boolean?=null, val ktm:String?=null, val profil:String?=null, val password:String?=null, val preferences: List<String> = emptyList(), val bookmark: List<String> = emptyList(), val cancel: List<String> = emptyList())
