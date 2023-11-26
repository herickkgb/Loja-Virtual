package com.herick.lojavirtual.activities.detalhesProduto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herick.lojavirtual.R
import com.herick.lojavirtual.databinding.ActivityDetalhesProdutoBinding

class DetalhesProduto : AppCompatActivity() {
    private lateinit var binding: ActivityDetalhesProdutoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}