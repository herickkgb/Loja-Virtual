package com.herick.lojavirtual.activities.pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.herick.lojavirtual.R
import com.herick.lojavirtual.adapter.AdapterPedido
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding
import com.herick.lojavirtual.databinding.ActivityPedidosBinding
import com.herick.lojavirtual.model.DB
import com.herick.lojavirtual.model.Pedido

class Pedidos : AppCompatActivity() {
    private lateinit var binding: ActivityPedidosBinding
    lateinit var adapterPedidos: AdapterPedido
    var listaPedidos: MutableList<Pedido> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recicler_pedidos = binding.reciclerPedidos
        recicler_pedidos.layoutManager = LinearLayoutManager(this)
        recicler_pedidos.setHasFixedSize(true)

        adapterPedidos = AdapterPedido(this, listaPedidos)
        recicler_pedidos.adapter = adapterPedidos

        val db = DB()
        db.obterListaPedidos(listaPedidos, adapterPedidos)
    }
}