package com.example.mvvmtest.repository

import com.example.mvvmtest.api.IProductAPI
import com.example.mvvmtest.api.ProductResponse

class ProductRepository(private val productAPI: IProductAPI) : IProductRepository {

    override fun buy(id: String, items: Int, callback: IProductRepository.BuyProductCallback) {
        // 假設買超過10個就會失敗
        if (items <= 10){
            //模擬購買成功
            callback.onBuyResult(true)
        } else {
            callback.onBuyResult(false)
        }

    }

    override fun getProduct(
        productId: String,
        loadProductCallback: IProductRepository.LoadProductCallback
    ) {

        productAPI.getProduct(productId, object : IProductAPI.LoadAPICallBack {
            override fun onGetResult(productResponse: ProductResponse) {
                loadProductCallback.onProductResult(productResponse)
            }
        })
    }
}