package com.herick.lojavirtual.activities.pagamento

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.herick.lojavirtual.R
import com.herick.lojavirtual.databinding.ActivityFormPagamentoBinding

class FormPagamento : AppCompatActivity() {
    private lateinit var binding: ActivityFormPagamentoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPagamentoBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_form_pagamento)
        var cidadeEstado = binding.editCidadeEstado.text
        var bairro = binding.editBairro.text
        var celular = binding.editCelular.text
        var ruaNumero = binding.edirRuaNumero.text

        binding.btfinalizarPedido.setOnClickListener {
            if (cidadeEstado.isEmpty() || bairro.isEmpty() || celular.isEmpty() || ruaNumero.isEmpty()
            ) {
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}