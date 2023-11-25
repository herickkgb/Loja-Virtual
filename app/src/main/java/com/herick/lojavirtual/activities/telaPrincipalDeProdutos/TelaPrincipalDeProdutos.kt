package com.herick.lojavirtual.activities.telaPrincipalDeProdutos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.activities.dialog.DialogPerfilUsuario
import com.herick.lojavirtual.adapter.AdapterProduto
import com.herick.lojavirtual.databinding.ActivityTelaPrincipalDeProdutosBinding
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
        adapterProduto = AdapterProduto(this,lista_produto)
        recycler_produtos.adapter = adapterProduto
        itensLista()
    }

    fun itensLista(){
        val produto1 = Produto(R.drawable.logo,"Sapato de couro","R$:150,00")
        lista_produto.add(produto1)

        val produto2 = Produto(R.drawable.logo,"Sapato de couro","R$:150,00")
        lista_produto.add(produto2)

        val produto3 = Produto(R.drawable.logo,"Sapato de couro","R$:150,00")
        lista_produto.add(produto3)

        val produto4 = Produto(R.drawable.logo,"Sapato de couro","R$:150,00")
        lista_produto.add(produto4)

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
        val dialogPerfilUsuario = DialogPerfilUsuario(this)
        dialogPerfilUsuario.iniciarPerfilUsuario()
        dialogPerfilUsuario.recuperarDadosUsuarioBanco()
    }

    private fun deslogarUsuario() {
        auth.signOut()

        Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
        finish()
    }
}
