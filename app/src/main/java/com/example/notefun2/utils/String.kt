package com.example.notefun2.utils

import java.util.*

/**
        Membuat huruf pertama dalam kalian menjadi huruf Besar,
 dan mengecilkan huruf lainnya.
 Contoh : contoh -> Contoh, CONTOH -> Contoh, cONTOH -> Contoh
 */

fun String.toLowerCaseExceptFirstChar(): String{
    if (length == 1) return this
    return lowercase(Locale.getDefault()).replaceFirstChar(Char::uppercase)
}