package com.example.myapplication.contract.task3

import com.example.myapplication.contract.task3.data.dto.ProductRequest
import com.example.myapplication.contract.task3.data.dto.ProductResponse

interface Task3Contract {
    interface View {
        fun showEmptyListTextView()
        fun hideEmptyListTextView()
        fun setListAdapter(products: ArrayList<ProductResponse>)
        fun navigateToCreateProductCard()
        fun navigateToProductCard(position: Int, productToPass: ProductResponse)
        fun startLoadingAnimation()
        fun stopLoadingAnimation()
        fun enableUserInteraction()
        fun disableUserInteraction()
        fun notifyListAdapterOnItemCreated(position: Int)
        fun notifyListAdapterOnItemDeleted(position: Int)
        fun notifyListAdapterOnItemChange(position: Int)
    }

    interface Presenter {
        fun onListItemClicked(position: Int)
        fun onBarcodeScanButtonPressed()
        fun onCreatedProductReceived(product: ProductResponse)
        fun onUpdatedProductReceived(product: ProductResponse, position: Int)
        fun onListItemSwiped(position: Int)
        fun onDestroy()
    }

    interface Model {
        suspend fun getProducts(): List<ProductResponse>
        suspend fun getProductsPaginated(): List<ProductResponse>
        suspend fun deleteProduct(id: String)
        fun close()
    }
}