import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herick.lojavirtual.activities.dialog.DialogCarregando
import com.herick.lojavirtual.activities.telaPrincipalDeProdutos.TelaPrincipalDeProdutos
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding

    private lateinit var auth: FirebaseAuth

    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFormLoginBinding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("830869344550-f6mst6bbev5c4t7f134sbktrouuiuef3.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        window.statusBarColor = Color.BLACK

        val dialogCarregando = DialogCarregando(this)

        binding.LinearGoogleLogin.setOnClickListener {
            Toast.makeText(this, "Login feito com sucesso", Toast.LENGTH_SHORT).show()
            loginGoogle()

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
    }

    private fun loginGoogle() {
        val intent = mGoogleSignInClient.signInIntent
        abreActivity.launch(intent)
    }

    var abreActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val conta = task.getResult(ApiException::class.java)
                loginComGoogle(conta.idToken)
            } catch (exception: ApiException) {

            }
        }
    }

    private fun loginComGoogle(idToken: String?) {
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credencial)
            .addOnCompleteListener(this) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login feito com sucesso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, TelaPrincipalDeProdutos::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao fazer login com Google", Toast.LENGTH_SHORT)
                        .show()
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