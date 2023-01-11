package com.example.myapplication.contract.task3.presenter

import com.example.myapplication.contract.task3.Task3ProductCardContract
import com.example.myapplication.contract.task3.data.dto.CompanyResponse
import com.example.myapplication.contract.task3.data.dto.ProductRequest
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Task3ProductCardPresenter(
    private var view: Task3ProductCardContract.View?,
    private val model: Task3ProductCardContract.Model,
    private var product: ProductResponse
) : Task3ProductCardContract.Presenter, CoroutineScope {
    private var job: Job = Job()
    private var producers: List<CompanyResponse>? = null
    private var suppliers: List<CompanyResponse>? = null
    private var currentProducerId: String = product.producer.id
    private var currentSupplierId: String = product.supplier.id

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        view?.setViewModeTextViews(product.name, product.price, product.producer.name, product.supplier.name)
    }

    override fun onEditButtonPressed() {
        if (producers == null || suppliers == null) {
            view?.disableUserInteraction()
            view?.startLoadingAnimation()
            launch {
                producers = model.getProducers()
                suppliers = model.getSuppliers()
                view?.stopLoadingAnimation()
                view?.enableUserInteraction()
            }
        }
        view?.hideViewModeElements()
        view?.showEditModeElements()
        view?.setEditModeTextViews(product.name, product.price, product.producer.name, product.supplier.name)
    }

    override fun onSaveButtonPressed(name: String, price: Double) {
        view?.disableUserInteraction()
        view?.startLoadingAnimation()
        view?.hideEditModeElements()
        view?.showViewModeElements()
        launch {
            product = model.updateProduct(product.id, ProductRequest(name, price, currentProducerId, currentSupplierId))
            view?.setViewModeTextViews(name, price, product.producer.name, product.supplier.name)
            view?.sendProductToStateHandle(product)
            view?.stopLoadingAnimation()
            view?.enableUserInteraction()
        }
    }

    override fun onCancelButtonPressed() {
        view?.setEditModeElementsToDefault()
        view?.hideEditModeElements()
        view?.showViewModeElements()
    }

    override fun onEditProducerTextViewClicked() {
        view?.navigateToProducerListDialog(producers!!.map { it.name }.toTypedArray())
    }

    override fun onEditSupplierTextViewClicked() {
        view?.navigateToSupplierListDialog(suppliers!!.map { it.name }.toTypedArray())
    }

    override fun onProducerFilterableListPositionReceived(position: Int) {
        currentProducerId = producers?.get(position)?.id ?: currentProducerId
        view?.setProducerEditTextView(producers!![position].name)
    }

    override fun onSupplierFilterableListPositionReceived(position: Int) {
        currentSupplierId = suppliers?.get(position)?.id ?: currentSupplierId
        view?.setSupplierEditTextView(suppliers!![position].name)
    }

    override fun onDestroy() {
        job.cancel()
        this.view = null
    }
}