package com.herick.lojavirtual.model

import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.herick.lojavirtual.adapter.AdapterProduto
import org.w3c.dom.Text

class DB {
    fun salvarDadosUsuario(nome: String) {
        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf(
            "nome" to nome
        )

        val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documentReference.set(usuarios).addOnSuccessListener {
            Log.d("DB", "Sucesso ao salvar os dados")
        }.addOnFailureListener { error ->
            Log.d("DB_ERRO", "Erro ao dalvar os dados! ${error.printStackTrace()}")

        }
    }

    fun recuperarDadosUsuarioPerfil(nomeUsuario: TextView, emailUsuario: TextView) {
        val usuarioID = FirebaseAuth.getInstance().currentUser?.uid
        val email = FirebaseAuth.getInstance().currentUser?.email
        val db = FirebaseFirestore.getInstance()

        if (usuarioID != null) {
            val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
            documentReference.addSnapshotListener { documento, error ->

                if (documento != null && documento.exists()) {
                    nomeUsuario.text = documento.getString("nome")
                    emailUsuario.text = email
                }
            }
        }
    }

    fun obterListaDeProdutos(listaProdutos: MutableList<Produto>, adapter_produto: AdapterProduto) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Produtos").get()
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    for (documento in tarefa.result!!) {
                        val produtos = documento.toObject(Produto::class.java)
                        listaProdutos.add(produtos)
                        adapter_produto.notifyDataSetChanged()
                    }
                }
            }
    }

    fun salvarDadosPedido(
        endereco: String,
        celular: String,
        produto: String,
        preco: String,
        tamanho_calcado: String,
        status_pagamento: String,
        status_entrega: String

    ) {

        var db = FirebaseFirestore.getInstance()
        var usuarioID = FirebaseAuth.getInstance().currentUser!!.uid

        val pedidos = hashMapOf(
            "endereco" to endereco,
            "celular" to celular,
            "produto" to produto,
            "preco" to preco,
            "tamanho_calcado" to tamanho_calcado,
            "status_pagamento" to status_pagamento,
            "status_entrega" to status_entrega
        )

        val documentReference = db.collection("Usuario_pedidos").document(usuarioID).collection("")
    }

}