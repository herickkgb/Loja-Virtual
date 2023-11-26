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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.dialog.DialogCarregando
import com.herick.lojavirtual.activities.telaPrincipalDeProdutos.TelaPrincipalDeProdutos
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding

    private lateinit var auth: FirebaseAuth


    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var gso: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFormLoginBinding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        auth = Firebase.auth

        window.statusBarColor = Color.BLACK

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("830869344550-tihcd9jnddu1fmgg2qitcfr2ft36td92.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val dialogCarregando = DialogCarregando(this)


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
                            Snackbar.make(
                                binding.root,
                                "Erro ao fazer login, verifique email e senha",
                                Snackbar.LENGTH_SHORT
                            )
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.show()
                    }
            }
        }

        binding.LinearGoogleLogin.setOnClickListener {
            signIn();
        }
    }

    private val signInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(RC_SIGN_IN, result.resultCode, result.data)
        }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInActivityResultLauncher.launch(signInIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("Google Sign-In", "Error: ${e.statusCode}")
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(this, TelaPrincipalDeProdutos::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@FormLogin, "Login Failed: ", Toast.LENGTH_SHORT).show()
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