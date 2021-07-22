package com.example.mvvmtest.api

interface IProductAPI {
    interface LoadAPICallBack {
        fun onGetResult(productResponse: ProductResponse)
    }

    fun getProduct(productId:String, loadAPICallBack: LoadAPICallBack)
}