package com.herick.lojavirtual.activities.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.databinding.DialogPerfilUsuarioBinding
import com.herick.lojavirtual.model.DB

class DialogPerfilUsuario(private val activity: Activity) {

    lateinit var dialog: AlertDialog
    lateinit var binding: DialogPerfilUsuarioBinding


    fun iniciarPerfilUsuario() {
        val builder = AlertDialog.Builder(activity)
        binding = DialogPerfilUsuarioBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
    }

    fun recuperarDadosUsuarioBanco() {
        val nomeUsuario = binding.nomeUsuario
        val emailUsuario = binding.nomeEmail
        val db = DB()

        db.recuperarDadosUsuarioPerfil(nomeUsuario, emailUsuario)

        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, FormLogin::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

}