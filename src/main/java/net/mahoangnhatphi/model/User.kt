package net.mahoangnhatphi.model

data class User(val name: String, val email: String, val id: String, val userDetails: UserDetails?)

data class UserDetails(val dateOfBirth: String, val salary: String)
