package com.herick.lojavirtual.activities.FormLogin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.herick.lojavirtual.activities.FormCadastro.FormCadastro
import com.herick.lojavirtual.activities.dialog.DialogCarregando
import com.herick.lojavirtual.activities.telaPrincipalDeProdutos.TelaPrincipalDeProdutos
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    private lateinit var binding: ActivityFormLoginBinding
    private val dialogCarregando = DialogCarregando(this)

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("830869344550-tihcd9jnddu1fmgg2qitcfr2ft36td92.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        window.statusBarColor = Color.BLACK

        binding.LinearGoogleLogin.setOnClickListener {
            loginGoogle(mGoogleSignInClient)
        }

        binding.btEntrar.setOnClickListener {
            val email = binding.editEmail.text.trim().toString()
            val senha = binding.editSenha.text.trim().toString()

            if (email.isEmpty() || senha.isEmpty()) {
                exibirSnackbar("Preencha todos os campos!", Color.RED)
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
                    }
                    .addOnFailureListener {
                        exibirSnackbar("Erro ao fazer login, verifique email e senha", Color.RED)
                    }
            }
        }

        binding.TxtCadastrese.setOnClickListener {
            startActivity(Intent(this, FormCadastro::class.java))
        }
    }

    private fun loginGoogle(mGoogleSignInClient: GoogleSignInClient) {
        val intent = mGoogleSignInClient.signInIntent
        abreActivity.launch(intent)
    }

    private val abreActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try {
                    val conta = task.getResult(ApiException::class.java)
                    loginComGoogle(conta?.idToken)
                } catch (exception: ApiException) {
                    Log.e("GoogleSignIn", "Erro: ${exception.statusCode}")
                }
            }
        }

    }

    private fun loginComGoogle(idToken: String?) {

        dialogCarregando.iniciarCarregamentoAlertDialog()
        Handler(Looper.getMainLooper()).postDelayed({

            val credencial = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credencial)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, TelaPrincipalDeProdutos::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        exibirSnackbar("Erro ao fazer login com Google", Color.RED)
                    }
                }

            dialogCarregando.liberarAlertDialog()
        }, 3000)

    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal() {
        val intent = Intent(this, TelaPrincipalDeProdutos::class.java)
        startActivity(intent)
        finish()
    }

    private fun exibirSnackbar(mensagem: String, cor: Int) {
        Snackbar.make(binding.root, mensagem, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(cor)
            setTextColor(Color.WHITE)
            show()
        }
    }
}
