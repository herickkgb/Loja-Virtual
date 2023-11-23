package com.herick.lojavirtual.activities.FormLogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormCadastro.FormCadastro
import com.herick.lojavirtual.databinding.ActivityFormCadastroBinding
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getWindow().setStatusBarColor(Color.BLACK);

        binding.TxtCadastrese.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()
            if (email.isEmpty() || senha.isEmpty()) {
                val snakbar = Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snakbar.setBackgroundTint(Color.RED)
                snakbar.setTextColor(Color.WHITE)
                snakbar.show()
            } else {
                fazerLogin(email, senha)
            }
        }
    }

    private fun fazerLogin(email: String, senha: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, FormLogin::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                }
            }
    }
}