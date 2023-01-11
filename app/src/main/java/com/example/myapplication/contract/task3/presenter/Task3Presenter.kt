package com.example.myapplication.contract.task3.presenter

import com.example.myapplication.contract.task3.Task3Contract
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Task3Presenter(
    private var view: Task3Contract.View?,
    private val model: Task3Contract.Model
) : Task3Contract.Presenter, CoroutineScope {
    private lateinit var currentProductList: ArrayList<ProductResponse>
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        view?.disableUserInteraction()
        launch {
            loadAndSetProductList()
            if (currentProductList.isNotEmpty()) view?.hideEmptyListTextView()
            view?.stopLoadingAnimation()
            view?.enableUserInteraction()
        }
    }

    override fun onListItemClicked(position: Int) {
        view?.navigateToProductCard(position, currentProductList[position])
    }

    override fun onBarcodeScanButtonPressed() {
        view?.navigateToCreateProductCard()
    }

    override fun onCreatedProductReceived(product: ProductResponse) {
        currentProductList.add(product)
        view?.notifyListAdapterOnItemCreated(currentProductList.size)
    }

    override fun onUpdatedProductReceived(product: ProductResponse, position: Int) {
        currentProductList[position] = product
        view?.notifyListAdapterOnItemChange(position)
    }

    override fun onListItemSwiped(position: Int) {
        view?.disableUserInteraction()
        view?.startLoadingAnimation()
        launch {
            model.deleteProduct(currentProductList[position].id)
            currentProductList.removeAt(position)
            view?.notifyListAdapterOnItemDeleted(position)
            view?.stopLoadingAnimation()
            view?.enableUserInteraction()
        }
    }

    override fun onDestroy() {
        job.cancel()
        model.close()
        this.view = null
    }

    private suspend fun loadAndSetProductList() {
        currentProductList = ArrayList(model.getProducts())
        view?.setListAdapter(currentProductList)
    }
}