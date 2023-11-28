package com.herick.lojavirtual.activities.detalhesProduto

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.herick.lojavirtual.activities.pagamento.Pagamento
import com.herick.lojavirtual.databinding.ActivityDetalhesProdutoBinding

class DetalhesProduto : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesProdutoBinding
    private var tamanho_calcado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        var preco = intent.getStringExtra("precoProduto")
        var nome = intent.getStringExtra("nomeProduto")

        binding.btPrecoProduto.text = preco
        binding.dtNomeProduto.text = nome

        val urlImagem: String = intent.getStringExtra("fotoProduto") ?: ""
        Glide.with(this)
            .load(urlImagem)
            .into(binding.imgDtFotoProduto)

        binding.btfinalizarPedido.setOnClickListener {
            tamanho_calcado = when {
                binding.tamanho38.isChecked -> "38"
                binding.tamanho39.isChecked -> "39"
                binding.tamanho40.isChecked -> "40"
                binding.tamanho41.isChecked -> "41"
                binding.tamanho42.isChecked -> "42"
                else -> {
                    Snackbar.make(it, "Selecione um tamanho", Snackbar.LENGTH_SHORT).apply {
                        setBackgroundTint(Color.RED)
                        show()
                    }
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, Pagamento::class.java)
            intent.putExtra("tamanho_calcado", tamanho_calcado)
            intent.putExtra("nome", nome)
            intent.putExtra("preco", preco)
            startActivity(intent)
        }
    }
}
