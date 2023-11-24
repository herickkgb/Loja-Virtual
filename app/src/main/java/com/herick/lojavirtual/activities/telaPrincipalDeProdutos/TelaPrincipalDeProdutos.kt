package com.herick.lojavirtual.activities.telaPrincipalDeProdutos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.databinding.ActivityTelaPrincipalDeProdutosBinding

class TelaPrincipalDeProdutos : AppCompatActivity() {
    private lateinit var binding: ActivityTelaPrincipalDeProdutosBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalDeProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.perfil -> abrirDialogPerfilUsuario()
            R.id.pedidos -> Log.i("erro", "ero")
            R.id.deslogar -> deslogarUsuario()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun abrirDialogPerfilUsuario() {
        TODO("Not yet implemented")
    }

    private fun deslogarUsuario() {
        auth.signOut()

        Toast.makeText(this,"Deslogado com sucesso!",Toast.LENGTH_SHORT).show()

        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
        finish()
    }
}
