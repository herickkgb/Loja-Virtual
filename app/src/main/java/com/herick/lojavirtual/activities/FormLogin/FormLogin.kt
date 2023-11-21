package com.herick.lojavirtual.activities.FormLogin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.herick.lojavirtual.R
import com.herick.lojavirtual.databinding.ActivityFormLoginBinding

class FormLogin : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        getWindow().setStatusBarColor(Color.BLACK);
    }
}