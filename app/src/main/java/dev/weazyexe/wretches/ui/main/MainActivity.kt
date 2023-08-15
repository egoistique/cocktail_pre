package com.example.coctailbar.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

import dev.weazyexe.wretches.databinding.ActivityMainBinding
import dev.weazyexe.wretches.ui.main.AllCocktailsActivity
import dev.weazyexe.wretches.ui.newcrime.NewCrimeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initListeners()
    }

    private fun initListeners() = with(binding) {
        addCocktailsBt.setOnClickListener {
            openActivity<NewCrimeActivity>()
        }
        myCocktailsBt.setOnClickListener {
            openActivity<AllCocktailsActivity>()
        }
    }

    private inline fun <reified A : AppCompatActivity> openActivity() {
        val intent = Intent(this@MainActivity, A::class.java)
        startActivity(intent)
    }
}


