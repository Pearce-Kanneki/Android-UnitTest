package com.example.mvvmtest

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmtest.api.ProductResponse
import com.example.mvvmtest.repository.IProductRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var repository: IProductRepository
    private var productResponse = ProductResponse()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)

        productResponse.apply {
            id = "pixel3"
            name = "Google Pixel 3"
            price = 27000
            desc = "Desc"
        }

        viewModel = MainViewModel(repository)
    }

    @Test
    fun getProductTest() {
        val productId = "pixel3"
        val slot = slot<IProductRepository.LoadProductCallback>()

        // 驗證是否有呼叫IProductRepository.LoadProductCallback
        every { repository.getProduct(eq(productId), capture(slot)) }
            .answers {
                //將callback攔截下載並指定productResponse的值。
                slot.captured.onProductResult(productResponse)
            }

        viewModel.getProduct(productId)

        GlobalScope.launch (Dispatchers.Default){
            Assert.assertEquals(productResponse.name, viewModel.productName.value)
            Assert.assertEquals(productResponse.desc, viewModel.productDesc.value)
            Assert.assertEquals(productResponse.price, viewModel.productPrice.value)
        }
    }

    @Test
    fun buySuccess(){
        val productId = "pixel3"
        val items = 3
        viewModel.productId.postValue(productId)
        viewModel.productItems.postValue(items.toString())

        val slot = slot<IProductRepository.BuyProductCallback>()
        every { repository.buy(com.example.mvvmtest.eq(productId), com.example.mvvmtest.eq(items), capture(slot)) }
            .answers {
                //驗證是否有呼叫IProductRepository.Product
                slot.captured.onBuyResult(true)
            }

        viewModel.buy()
        GlobalScope.launch(Dispatchers.Default){
            Assert.assertTrue(viewModel.buySuccessText.value != null)
        }
    }
}