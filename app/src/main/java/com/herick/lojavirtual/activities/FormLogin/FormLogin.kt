package com.herick.lojavirtual.activities.FormLogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herick.lojavirtual.activities.FormCadastro.FormCadastro
import com.herick.lojavirtual.activities.dialog.DialogCarregando
import com.herick.lojavirtual.activities.telaPrincipalDeProdutos.TelaPrincipalDeProdutos
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFormLoginBinding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        auth = Firebase.auth

        window.statusBarColor = Color.BLACK

        val dialogCarregando = DialogCarregando(this)

        binding.TxtCadastrese.setOnClickListener {
            startActivity(Intent(this, FormCadastro::class.java))
        }

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()

            if (email.isEmpty() || senha.isEmpty()) {
                Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_SHORT).apply {
                    setBackgroundTint(Color.RED)
                    setTextColor(Color.WHITE)
                    show()
                }
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            dialogCarregando.iniciarCarregamentoAlertDialog()

                            Handler(Looper.getMainLooper()).postDelayed({

                                abrirTelaPrincipal()
                                dialogCarregando.liberarAlertDialog()
                            }, 3000)
                        }
                    }.addOnFailureListener {

                        val snackbar =
                            Snackbar.make( binding.root,
                                "Erro ao fazer login, verifique email e senha",
                                Snackbar.LENGTH_SHORT
                            )
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.show()
                    }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal() {
        val intent = Intent(this, TelaPrincipalDeProdutos::class.java)
        startActivity(intent)
        finish()
    }


}