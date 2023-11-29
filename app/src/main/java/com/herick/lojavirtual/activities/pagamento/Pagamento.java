package com.herick.lojavirtual.activities.pagamento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.herick.lojavirtual.R;
import com.herick.lojavirtual.databinding.ActivityPagamentoBinding;
import com.herick.lojavirtual.interfaceMercadoPago.ComunicacaoServidorMP;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pagamento extends AppCompatActivity {
    private ActivityPagamentoBinding binding;

    private String tamanho_calcado;
    private String nome;
    private String preco;
    private final String PUBLIC_KEY = "TEST-c53d0963-1025-4fc4-9d45-4c0bf920ab52";
    private final String ACCESS_TOKEN = "TEST-5333937517053456-010623-7b1c73c226f567102bf13cd4b2298391-225243913";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tamanho_calcado = getIntent().getStringExtra("tamanho_calcado");
        nome = getIntent().getStringExtra("nome");
        preco = getIntent().getStringExtra("preco");

        binding.btfinalizarPedido.setOnClickListener(it -> {
            String bairro = binding.editBairro.getText().toString();
            String ruaNumero = binding.edirRuaNumero.getText().toString();
            String cidadeEstado = binding.editCidadeEstado.getText().toString();
            String celular = binding.editCelular.getText().toString();

            if (bairro.isEmpty() || ruaNumero.isEmpty() || cidadeEstado.isEmpty() || celular.isEmpty()) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Preencha os campos!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(getColor(R.color.white));
                snackbar.show();
            } else {
                criarJsonObject(celular);
            }
        });

    }

    private void criarJsonObject(String celular) {
        JsonObject dados = new JsonObject();

        JsonArray item_lista = new JsonArray();
        JsonObject item;

        JsonObject email = new JsonObject();

        item = new JsonObject();
        item.addProperty("title", nome);
        item.addProperty("quantity", 1);
        item.addProperty("currency_id", "BRL");

        String numericPart = preco.replaceAll("[^\\d.]", "");

        try {
            double precoDouble = Double.parseDouble(numericPart);
            item.addProperty("unit_price", precoDouble);
            item_lista.add(item);

        } catch (NumberFormatException e) {
            Log.e("PrepararDadosPedido", "Erro ao converter 'preco' para double", e);
        }
        dados.add("items", item_lista);

        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.addProperty("email", emailUsuario);
        dados.add("payer", email);

        Log.d("Teste", dados.toString());

        criarPreferenciaPagamento(dados);
    }

    private void criarPreferenciaPagamento(JsonObject dados) {
        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token=" + ACCESS_TOKEN;

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ComunicacaoServidorMP conexao_pagamento = retrofit.create(ComunicacaoServidorMP.class);

        Call<JsonObject> request = conexao_pagamento.enviarPagamento(url, dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    String preferenceId = response.body().get("id").getAsString();
                    startMercadoPagoCheckout(preferenceId); // Inicia o checkout aqui
                } else {
                    // Aqui, você pode tratar a resposta de erro de forma apropriada
                    Log.e("MeuApp", "Erro na criação da preferência de pagamento: " + response.errorBody());
                    exibirErroPagamento(); // Adicione um método para exibir uma mensagem de erro ao usuário, se necessário
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("MeuApp", "Falha na requisição para criar a preferência de pagamento", t);
                exibirErroPagamento(); // Adicione um método para exibir uma mensagem de erro ao usuário, se necessário
            }
        });
    }

    private void startMercadoPagoCheckout(final String checkoutPreferenceId) {
        final AdvancedConfiguration advancedConfiguration =
                new AdvancedConfiguration.Builder().setBankDealsEnabled(false).build();
        new MercadoPagoCheckout
                .Builder(PUBLIC_KEY, checkoutPreferenceId)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment pagamento = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                respostaMercadoPago(pagamento);
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar snackbar = Snackbar.make(binding.container, "Pagamento Rejeitado", Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            } else {
                Log.e("MeuApp", "Resultado desconhecido: " + resultCode);
                Snackbar snackbar = Snackbar.make(binding.container, "Erro no pagamento", Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
        } else {
            Log.e("MeuApp", "Request code desconhecido: " + requestCode);
        }
    }

    private void respostaMercadoPago(Payment pagamento) {
        String status = pagamento.getPaymentStatus();
        if (status.equalsIgnoreCase("approved")) {
            Snackbar snackbar = Snackbar.make(binding.container, "Pagamento aprovado!", Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(Color.GREEN);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
        } else if (status.equalsIgnoreCase("rejected")) {
            Snackbar snackbar = Snackbar.make(binding.container, "Pagamento rejeitado", Snackbar.LENGTH_LONG);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.show();
        }else {
            Snackbar snackbar = Snackbar.make(binding.container, status.toString(), Snackbar.LENGTH_LONG);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.show();
        }
    }

    private void exibirErroPagamento() {
        Snackbar snackbar = Snackbar.make(binding.container, "Erro ao processar o pagamento", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }
}
