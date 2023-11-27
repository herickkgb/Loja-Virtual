package com.herick.lojavirtual.activities.dialog
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.databinding.DialogPerfilUsuarioBinding
import com.herick.lojavirtual.model.DB

class DialogPerfilUsuario(private val activity: Activity) {

    private lateinit var dialog: AlertDialog
    private lateinit var binding: DialogPerfilUsuarioBinding
    private val db = DB()
    private var googleApiClient: GoogleApiClient? = null


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

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)

        if (googleSignInAccount != null) {
            // Usuário fez login com o Google
            val displayName = googleSignInAccount.displayName
            val email = googleSignInAccount.email
            val photoUrl = googleSignInAccount.photoUrl

            if (email != null && displayName != null && photoUrl != null) {
                binding.nomeUsuario.text = displayName
                binding.nomeEmail.text = email

                // Use o Glide para carregar a imagem
                Glide.with(activity)
                    .load(photoUrl)
                    .circleCrop()
                    .placeholder(R.drawable.circle_imagem_perfil) // Recurso de placeholder
                    .into(binding.containerCirculo)
            }
        } else {
            db.recuperarDadosUsuarioPerfil(nomeUsuario, emailUsuario)
        }

        binding.btDeslogar.setOnClickListener {
            // Verifica se há uma conta do Google logada
            val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)

            if (googleSignInAccount != null) {
                // Se uma conta do Google estiver logada, realiza o logout
                val googleSignInClient = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_SIGN_IN)
                googleSignInClient.signOut().addOnCompleteListener(activity) {
                    // Após fazer logout do Google, executa a ação de logout geral
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(activity, FormLogin::class.java)
                    activity.startActivity(intent)

                    activity.finish()
                }
            } else {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(activity, FormLogin::class.java)
                activity.startActivity(intent)


                activity.finish()
            }

            FirebaseAuth.getInstance().signOut()
        }

    }

}
