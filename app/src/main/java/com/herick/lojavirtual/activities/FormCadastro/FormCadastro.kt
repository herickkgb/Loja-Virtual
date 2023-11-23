package com.herick.lojavirtual.activities.FormCadastro

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.databinding.ActivityFormCadastroBinding
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormCadastro : AppCompatActivity() {
    lateinit var binding: ActivityFormCadastroBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWindow().setStatusBarColor(Color.BLACK);

        // Initialize Firebase Auth
        auth = Firebase.auth




        binding.btCadastrar.setOnClickListener {

            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()
            val nome = binding.editNome.text.toString()

            if (email.isEmpty() || senha.isEmpty() || nome.isEmpty()) {
                val snakbar = Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snakbar.setBackgroundTint(Color.RED)
                snakbar.setTextColor(Color.WHITE)
                snakbar.show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val snakbar = Snackbar.make(it, "Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT)
                            snakbar.setBackgroundTint(Color.BLUE)
                            snakbar.setTextColor(Color.WHITE)
                            snakbar.show()

                            val intent = Intent(this, FormLogin::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Erro ao criar conta!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }

    }
}
