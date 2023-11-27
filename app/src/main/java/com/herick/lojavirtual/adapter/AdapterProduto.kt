package com.herick.lojavirtual.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.herick.lojavirtual.activities.detalhesProduto.DetalhesProduto
import com.herick.lojavirtual.databinding.ProdutoItemBinding
import com.herick.lojavirtual.model.Produto

class AdapterProduto(
    private val context: Context,
    private val lista_produto: MutableList<Produto>
) :
    RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val item_lista = ProdutoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProdutoViewHolder(item_lista)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produtoSelecionado = lista_produto[position].foto
        Glide.with(context).load(lista_produto.get(position).foto).into(holder.fotoProduto)
        holder.nomeProduto.text = lista_produto[position].nome
        holder.precoProduto.text = "R$: ${lista_produto[position].preco}"

        holder.itemView.setOnClickListener {

            val intent = Intent(context, DetalhesProduto::class.java)

            intent.putExtra("nomeProduto", holder.nomeProduto.text )
            intent.putExtra("precoProduto",holder.precoProduto.text)
            intent.putExtra("fotoProduto",produtoSelecionado)

            context.startActivity(intent)

        }
    }

    override fun getItemCount() = lista_produto.size


    inner class ProdutoViewHolder(binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fotoProduto = binding.fotoProduto
        val nomeProduto = binding.nomeProduto
        val precoProduto = binding.precoProduto
    }


}