package com.herick.lojavirtual.activities.detalhesProduto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.herick.lojavirtual.R
import com.herick.lojavirtual.activities.FormLogin.FormLogin
import com.herick.lojavirtual.activities.pagamento.FormPagamento
import com.herick.lojavirtual.databinding.ActivityDetalhesProdutoBinding

class DetalhesProduto : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesProdutoBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        binding.btPrecoProduto.text = intent.getStringExtra("precoProduto")
        binding.dtNomeProduto.text = intent.getStringExtra("nomeProduto")

        val urlImagem: String = intent.getStringExtra("fotoProduto") ?: ""

        Glide.with(this)
            .load(urlImagem)
            .into(binding.imgDtFotoProduto)




        clickButtonFinalizarPedido(binding)
    }

    private fun clickButtonFinalizarPedido(binding: ActivityDetalhesProdutoBinding) {
        binding.btfinalizarPedido.setOnClickListener {
            val intent = Intent(this, FormPagamento::class.java)
            startActivity(intent)
        }
    }
}