package com.example.GestionDesCours

data class Remarque(
    var date: String? = null,
    var title: String? = null
) {
    constructor() : this(null, null)
}
