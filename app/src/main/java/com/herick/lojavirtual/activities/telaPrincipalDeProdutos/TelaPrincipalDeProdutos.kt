package com.herick.lojavirtual.activities.telaPrincipalDeProdutos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.activities.detalhesProduto.DetalhesProduto
import com.herick.lojavirtual.activities.dialog.DialogPerfilUsuario
import com.herick.lojavirtual.activities.pedidos.Pedidos
import com.herick.lojavirtual.adapter.AdapterProduto
import com.herick.lojavirtual.databinding.ActivityTelaPrincipalDeProdutosBinding
import com.herick.lojavirtual.model.DB
import com.herick.lojavirtual.model.Produto

class TelaPrincipalDeProdutos : AppCompatActivity() {
    private lateinit var binding: ActivityTelaPrincipalDeProdutosBinding

    private lateinit var auth: FirebaseAuth
    lateinit var adapterProduto: AdapterProduto
    var lista_produto: MutableList<Produto> = mutableListOf()

    val dialogPerfil = DialogPerfilUsuario(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalDeProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val recycler_produtos = binding.recyclerProdutos
        recycler_produtos.layoutManager = GridLayoutManager(this, 2)
        recycler_produtos.setHasFixedSize(true)
        adapterProduto = AdapterProduto(this, lista_produto)
        recycler_produtos.adapter = adapterProduto

        val db = DB()
        db.obterListaDeProdutos(lista_produto, adapterProduto)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.perfil -> abrirDialogPerfilUsuario()
            R.id.pedidos -> abrirTelaPedidos()
            R.id.deslogar -> deslogarUsuario()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun abrirTelaPedidos() {
        val intent = Intent(this, Pedidos::class.java)
        startActivity(intent)
    }

    private fun abrirDialogPerfilUsuario() {
        val dialogPerfilUsuario = DialogPerfilUsuario(this)
        dialogPerfilUsuario.iniciarPerfilUsuario()
        dialogPerfilUsuario.recuperarDadosUsuarioBanco()
    }

    private fun deslogarUsuario() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

        if (googleSignInAccount != null) {
            val googleSignInClient =
                GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
            googleSignInClient.signOut().addOnCompleteListener(this) {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, FormLogin::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }

        FirebaseAuth.getInstance().signOut()
    }

}
