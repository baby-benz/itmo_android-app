package com.example.myapplication.contract.task3.presenter

import com.example.myapplication.contract.task3.Task3CreateProductCardContract
import com.example.myapplication.contract.task3.data.dto.CompanyResponse
import com.example.myapplication.contract.task3.data.dto.ProductRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Task3CreateProductCardPresenter(
    private var view: Task3CreateProductCardContract.View?,
    private val model: Task3CreateProductCardContract.Model
) : Task3CreateProductCardContract.Presenter, CoroutineScope {
    private var job: Job = Job()
    private lateinit var producers: List<CompanyResponse>
    private lateinit var suppliers: List<CompanyResponse>
    private lateinit var currentProducerId: String
    private lateinit var currentSupplierId: String

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        view?.disableUserInteraction()
        view?.startLoadingAnimation()
        launch {
            producers = model.getProducers()
            suppliers = model.getSuppliers()
            view?.stopLoadingAnimation()
            view?.enableUserInteraction()
        }
    }

    override fun onSaveButtonPressed(name: String, price: Double) {
        view?.disableUserInteraction()
        view?.startLoadingAnimation()
        launch {
            val product = model.saveProduct(ProductRequest(name, price, currentProducerId, currentSupplierId))
            view?.sendProductToStateHandle(product)
            view?.stopLoadingAnimation()
            view?.enableUserInteraction()
            view?.dismiss()
        }
    }

    override fun onEditProducerTextViewClicked() {
        view?.navigateToProducerListDialog(producers.map { it.name }.toTypedArray())
    }

    override fun onEditSupplierTextViewClicked() {
        view?.navigateToSupplierListDialog(suppliers.map { it.name }.toTypedArray())
    }

    override fun onProducerFilterableListPositionReceived(position: Int) {
        currentProducerId = producers[position].id
        view?.setProducerEditTextView(producers[position].name)
    }

    override fun onSupplierFilterableListPositionReceived(position: Int) {
        currentSupplierId = suppliers[position].id
        view?.setSupplierEditTextView(suppliers[position].name)
    }

    override fun onDestroy() {
        job.cancel()
        this.view = null
    }
}