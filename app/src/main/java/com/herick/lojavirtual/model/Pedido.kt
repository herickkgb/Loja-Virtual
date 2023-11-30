package com.herick.lojavirtual.model

data class Pedido(
    var endereco: String? = null,
    var celular: String? = null,
    var produto: String? = null,
    var preco: String? = null,
    var tamanho_calcado: String? = null,
    var status_pagamento: String? = null,
    var status_entrega: String? = null
)