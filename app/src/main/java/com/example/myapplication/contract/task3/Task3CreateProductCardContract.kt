package com.example.myapplication.contract.task3

import com.example.myapplication.contract.task3.data.dto.CompanyResponse
import com.example.myapplication.contract.task3.data.dto.ProductRequest
import com.example.myapplication.contract.task3.data.dto.ProductResponse

interface Task3CreateProductCardContract {
    interface View {
        fun startLoadingAnimation()
        fun stopLoadingAnimation()
        fun sendProductToStateHandle(product: ProductResponse)
        fun enableUserInteraction()
        fun disableUserInteraction()
        fun navigateToProducerListDialog(producerList: Array<String>)
        fun navigateToSupplierListDialog(supplierList: Array<String>)
        fun setProducerEditTextView(producerName: String)
        fun setSupplierEditTextView(supplierName: String)
        fun dismiss()
    }

    interface Presenter {
        fun onSaveButtonPressed(name: String, price: Double)
        fun onEditProducerTextViewClicked()
        fun onEditSupplierTextViewClicked()
        fun onProducerFilterableListPositionReceived(position: Int)
        fun onSupplierFilterableListPositionReceived(position: Int)
        fun onDestroy()
    }

    interface Model {
        suspend fun getProducers(): List<CompanyResponse>
        suspend fun getSuppliers(): List<CompanyResponse>
        suspend fun saveProduct(productToSave: ProductRequest): ProductResponse
    }
}