package com.example.myapplication.contract.task3

import com.example.myapplication.contract.task3.data.dto.CompanyResponse
import com.example.myapplication.contract.task3.data.dto.ProductRequest
import com.example.myapplication.contract.task3.data.dto.ProductResponse

interface Task3ProductCardContract {
    interface View {
        fun showEditModeElements()
        fun hideEditModeElements()
        fun showViewModeElements()
        fun hideViewModeElements()
        fun setEditModeElementsToDefault()
        fun setViewModeTextViews(title: String, price: Double, producerName: String, supplierName: String)
        fun setEditModeTextViews(title: String, price: Double, producerName: String, supplierName: String)
        fun startLoadingAnimation()
        fun stopLoadingAnimation()
        fun navigateToProducerListDialog(producerList: Array<String>)
        fun navigateToSupplierListDialog(supplierList: Array<String>)
        fun setProducerEditTextView(producerName: String)
        fun setSupplierEditTextView(supplierName: String)
        fun sendProductToStateHandle(product: ProductResponse)
        fun enableUserInteraction()
        fun disableUserInteraction()
    }

    interface Presenter {
        fun onEditButtonPressed()
        fun onSaveButtonPressed(name: String, price: Double)
        fun onCancelButtonPressed()
        fun onEditProducerTextViewClicked()
        fun onEditSupplierTextViewClicked()
        fun onProducerFilterableListPositionReceived(position: Int)
        fun onSupplierFilterableListPositionReceived(position: Int)
        fun onDestroy()
    }

    interface Model {
        suspend fun getProducers(): List<CompanyResponse>
        suspend fun getSuppliers(): List<CompanyResponse>
        suspend fun updateProduct(id: String, productToUpdate: ProductRequest): ProductResponse
    }
}