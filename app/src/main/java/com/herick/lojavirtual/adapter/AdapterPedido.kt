package com.herick.lojavirtual.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.herick.lojavirtual.databinding.PedidoItemBinding
import com.herick.lojavirtual.model.Pedido

class AdapterPedido(val context: Context, val lista_pedidos: MutableList<Pedido>) :
    RecyclerView.Adapter<AdapterPedido.PedidoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val item_lista = PedidoItemBinding.inflate((LayoutInflater.from(context)), parent, false)
        return PedidoViewHolder(item_lista)
    }

    override fun getItemCount() = lista_pedidos.size

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.txt_endereco.text = lista_pedidos[position].endereco
        holder.txt_celular.text = lista_pedidos[position].celular
        holder.txt_nome_produto.text = lista_pedidos[position].produto
        holder.txt_preco_produto.text = lista_pedidos[position].preco
        holder.txt_tamanho_produto.text = lista_pedidos[position].tamanho_calcado
        holder.txt_status_entrega.text = lista_pedidos[position].status_entrega
        holder.txt_status_pagamento.text = lista_pedidos[position].status_pagamento
    }

    inner class PedidoViewHolder(binding: PedidoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val txt_endereco = binding.txtEndereco
        val txt_celular = binding.txtCelular
        val txt_nome_produto = binding.txtNomeProduto
        val txt_preco_produto = binding.txtPrecoProduto
        val txt_tamanho_produto = binding.txtTamanhoProduto
        val txt_status_entrega = binding.statusEntrega
        val txt_status_pagamento = binding.statusPagamento
    }
}