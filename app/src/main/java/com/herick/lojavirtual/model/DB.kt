package com.herick.lojavirtual.model

import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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

}