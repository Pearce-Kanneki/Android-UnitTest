package com.example.mvvmtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmtest.api.ProductResponse
import com.example.mvvmtest.repository.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val productRepository: IProductRepository): ViewModel() {
    val productId: MutableLiveData<String> = MutableLiveData()
    val productName: MutableLiveData<String> = MutableLiveData()
    val productDesc: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<Int> = MutableLiveData()
    val productItems: MutableLiveData<String> = MutableLiveData()

    val alertText: MutableLiveData<Event<String>> = MutableLiveData()
    val buySuccessText: MutableLiveData<Event<String>> = MutableLiveData()

    fun getProduct(productId: String) {
        this.productId.postValue(productId)
        productRepository.getProduct(productId, object : IProductRepository.LoadProductCallback {
            override fun onProductResult(productResponse: ProductResponse) {
                productName.postValue(productResponse.name)
                productDesc.postValue(productResponse.desc)
                productPrice.postValue(productResponse.price)
            }
        })
    }

    fun buy() {
        val productId = productId.value ?: ""
        val numbers = (productItems.value ?: "0").toInt()

        productRepository.buy(productId, numbers, object : IProductRepository.BuyProductCallback {
            override fun onBuyResult(isSuccess: Boolean) {
                if (isSuccess){
                    buySuccessText.postValue(Event("購買成功"))
                } else {
                    alertText.postValue(Event("購買失敗"))
                }
            }
        })
    }

}