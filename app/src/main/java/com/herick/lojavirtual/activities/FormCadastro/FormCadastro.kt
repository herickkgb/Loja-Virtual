package com.herick.lojavirtual.activities.FormCadastro

import android.graphics.Color
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.databinding.ActivityFormCadastroBinding
import com.herick.lojavirtual.model.DB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FormCadastro : AppCompatActivity() {
    private lateinit var binding: ActivityFormCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        val db = DB()

        binding.btCadastrar.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()
            val nome = binding.editNome.text.toString()

            if (email.isEmpty() || senha.isEmpty() || nome.isEmpty()) {
                val snackbar =
                    Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            } else {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Cadastro feito com sucesso", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, FormLogin::class.java)
                            startActivity(intent)
                            finish()
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                db.salvarDadosUsuario(nome)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val snackbar = Snackbar.make(
                                    it,
                                    "Erro ao salvar dados do usuário.",
                                    Snackbar.LENGTH_SHORT
                                )
                                snackbar.setBackgroundTint(Color.RED)
                                snackbar.setTextColor(Color.WHITE)
                                snackbar.show()

                            }
                        }
                    }.addOnFailureListener { erroCadastro ->
                        val mensagemErro = when (erroCadastro) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres."
                            is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada."
                            is FirebaseNetworkException -> "Sem conexão com a Internet."
                            else -> "Erro ao cadastrar usuário, tente novamente mais tarde."
                        }

                        val snackbar =
                            Snackbar.make(it, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.show()
                    }
            }
        }
    }
}