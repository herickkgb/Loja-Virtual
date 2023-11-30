package com.herick.lojavirtual.activities.pagamento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.herick.lojavirtual.model.DB;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pagamento extends AppCompatActivity {
    private ActivityPagamentoBinding binding;

    private String tamanho_calcado;

    DB db = new DB();
    private String nome;
    private String preco;
    private final String PUBLIC_KEY = "APP_USR-1bd11b96-1239-4c5a-944d-ed4b025e76d1";
    private final String ACCESS_TOKEN = "APP_USR-5333937517053456-010623-fc19fd0b19ccfa1c583596a9020b59d1-225243913";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tamanho_calcado = getIntent().getStringExtra("tamanho_calcado");
        nome = getIntent().getStringExtra("nome");
        preco = getIntent().getStringExtra("preco");

        // Remove non-numeric characters from the preco string
        Double precoDouble = Double.parseDouble(preco.replaceAll("[^\\d.]", ""));

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
                criarJsonObject(precoDouble);
            }
        });
    }

    private void criarJsonObject(Double precoDouble) {
        JsonObject dados = new JsonObject();

        JsonArray item_lista = new JsonArray();
        JsonObject item;

        JsonObject email = new JsonObject();

        item = new JsonObject();

        item.addProperty("title", nome);
        item.addProperty("quantity", 1);
        item.addProperty("currency_id", "BRL");
        item.addProperty("unit_price", precoDouble);
        item_lista.add(item);

        dados.add("items", item_lista);

        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.addProperty("email", emailUsuario);
        dados.add("payer", email);

        criarPreferenciaPagamento(dados);
    }

    private void criarPreferenciaPagamento(JsonObject dados) {
        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token=" + ACCESS_TOKEN;

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ComunicacaoServidorMP conexao_pagamento = retrofit.create(ComunicacaoServidorMP.class);

        Call<JsonObject> request = conexao_pagamento.enviarPagamento(url, dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String preferenceId = response.body().get("id").getAsString();
                criarPagamento(preferenceId); // Inicia o checkout aqui
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("MeuApp", "Falha na requisição para criar a preferência de pagamento", t);
                exibirErroPagamento(); // Adicione um método para exibir uma mensagem de erro ao usuário, se necessário
            }
        });
    }

    private void criarPagamento(String preferenceId) {
        final AdvancedConfiguration advancedConfiguration =
                new AdvancedConfiguration.Builder().setBankDealsEnabled(false).build();
        new MercadoPagoCheckout
                .Builder(PUBLIC_KEY, preferenceId)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, 123);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                respostaMercadoPago(payment);
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar snackbar = Snackbar.make(binding.container, "Pagamento Cancelado", Snackbar.LENGTH_LONG);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            } else {
                //Resolve canceled checkout
            }
        } else {
            Log.d("requestCode", "requestCode: != 123");
        }
    }

    private void respostaMercadoPago(Payment pagamento) {
        String status = pagamento.getPaymentStatus();

        String bairro = binding.editBairro.getText().toString();
        String ruaNumero = binding.edirRuaNumero.getText().toString();
        String cidadeEstado = binding.editCidadeEstado.getText().toString();
        String celular = binding.editCelular.getText().toString();

        String endereco = "Bairro:  " + " " + bairro + " " + " Rua/Número:  " + " " + ruaNumero + " " + " Cidade/Estado: " + " " + cidadeEstado + " ";
        String status_pagamento = "Status de Pagamento: " + " " + "Pagamento Aprovado";
        String status_entrega = "Status de Entrega: " + " " + "Em Andamento";

        String nomeProduto = "Nome: " + " " + nome;
        String precoProduto = "Preço: " + " " + preco;
        String tamanho = "Tamanho do Calçado: " + " " + tamanho_calcado;
        String celular_usuario = "Celular: " + " " + celular;

        if (status.equalsIgnoreCase("approved")) {
            Toast.makeText(this, "Pagamento aprovado", Toast.LENGTH_LONG).show();

            db.salvarDadosPedidoUsuario(endereco, celular_usuario, nomeProduto, precoProduto, tamanho, status_pagamento, status_entrega);
        } else if (status.equalsIgnoreCase("rejected")) {
            Toast.makeText(this, "Pagamento rejeitado", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Pagamento outro erro", Toast.LENGTH_LONG).show();
        }
    }

    private void exibirErroPagamento() {
        Snackbar snackbar = Snackbar.make(binding.container, "Erro ao processar o pagamento", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }
}
