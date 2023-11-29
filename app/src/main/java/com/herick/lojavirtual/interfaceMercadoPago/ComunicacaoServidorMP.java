package com.herick.lojavirtual.interfaceMercadoPago;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ComunicacaoServidorMP {
    @Headers({
            "Content-Type: application/json"
    })

    @POST()
    Call<JsonObject> enviarPagamento(
            @Url String url,
            @Body JsonObject dados );

}
