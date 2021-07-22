package com.example.mvvmtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmtest.api.ProductAPI
import com.example.mvvmtest.databinding.ActivityMainBinding
import com.example.mvvmtest.repository.ProductRepository

class MainActivity : AppCompatActivity() {

    private val productId = "pixel5"
    
    private val dataBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productAPI = ProductAPI()
        val productRepository = ProductRepository(productAPI)
        val viewModel = ViewModelProvider(this, ProductViewModelFactory(productRepository))
            .get(MainViewModel::class.java)

        with(dataBinding){
            vm = viewModel
            lifecycleOwner = this@MainActivity
        }
        
        viewModel.getProduct(productId)
        viewModel.alertText.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { 
                 AlertDialog.Builder(this)
                    .setMessage(it)
                    .setTitle("錯誤")
                    .show()
            }
        })
        viewModel.buySuccessText.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}