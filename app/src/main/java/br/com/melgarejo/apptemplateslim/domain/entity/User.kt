package br.com.melgarejo.apptemplateslim.domain.entity

import java.io.Serializable

data class User(
    var id: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var token: String? = null,
    var avatarUrl: String? = null,
    var avatarThumbUrl: String? = null
) : Serializable
