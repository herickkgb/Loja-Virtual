package com.herick.lojavirtual.activities.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog

class DialogPerfilUsuario(private val activity: Activity) {
    lateinit var dialog: AlertDialog

    fun iniciarPerfilUsuario() {
        val builder = AlertDialog.Builder(activity)
    }
}