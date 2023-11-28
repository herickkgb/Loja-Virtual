package com.herick.lojavirtual.activities.pagamento;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.herick.lojavirtual.R;
import com.herick.lojavirtual.databinding.ActivityPagamentoBinding;

public class Pagamento extends AppCompatActivity {
    private ActivityPagamentoBinding binding;

    private String tamanho_calcado;
    private String nome;
    private String preco;
    private final String PUBLIC_KEY = "TEST-c53d0963-1025-4fc4-9d45-4c0bf920ab52";
    private final String ACCESS_TOKEN = "TEST-5333937517053456-010623-7b1c73c226f567102bf13cd4b2298391-225243913";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tamanho_calcado = getIntent().getStringExtra("tamanho_calcado");
        nome = getIntent().getStringExtra("nome");
        preco = getIntent().getStringExtra("preco");

        if (tamanho_calcado == null || nome == null || preco == null) {
            // Lidar com a situação em que algum dos extras é nulo
            return;
        }

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
                prepararDadosPedido();
            }
        });
    }

    private void prepararDadosPedido() {

        JsonObject dados = new JsonObject();

        JsonArray item_lista = new JsonArray();
        JsonObject item;


        item = new JsonObject();
        item.addProperty("title", nome);
        item.addProperty("quantity", 1);
        item.addProperty("currency_id", "BRL");

        // Pré-processar a string 'preco' para remover caracteres não numéricos
        String numericPart = preco.replaceAll("[^\\d.]", "");

        try {
            double precoDouble = Double.parseDouble(numericPart);
            item.addProperty("unit_price", precoDouble);
            item_lista.add(item);

        } catch (NumberFormatException e) {
            Log.e("PrepararDadosPedido", "Erro ao converter 'preco' para double", e);
        }
        dados.add("items", item_lista);
    }


}
