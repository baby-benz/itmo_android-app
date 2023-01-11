package com.example.myapplication.fragment.task3

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.contract.task3.Task3ProductCardContract
import com.example.myapplication.contract.task3.data.Task3Repository
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import com.example.myapplication.contract.task3.presenter.ProductLiveData
import com.example.myapplication.contract.task3.presenter.Task3ProductCardPresenter
import com.example.myapplication.databinding.DialogTask3ProductCardBinding

class Task3ProductCardDialog : DialogFragment(), Task3ProductCardContract.View {
    private val args: Task3ProductCardDialogArgs by navArgs()
    private lateinit var presenter: Task3ProductCardContract.Presenter
    private lateinit var binding: DialogTask3ProductCardBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogTask3ProductCardBinding.inflate(layoutInflater)
            AlertDialog.Builder(it).setView(binding.root).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_fragment_background)
        presenter = Task3ProductCardPresenter(this, Task3Repository, args.product)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProductButton.setOnClickListener {
            presenter.onEditButtonPressed()
        }

        binding.editProducer.setOnClickListener {
            presenter.onEditProducerTextViewClicked()
        }

        binding.editSupplier.setOnClickListener {
            presenter.onEditSupplierTextViewClicked()
        }

        binding.saveProductButton.setOnClickListener {
            presenter.onSaveButtonPressed(
                binding.editTitle.text.toString(),
                binding.editPrice.text.toString().toDouble()
            )
        }

        binding.cancelEditButton.setOnClickListener {
            presenter.onCancelButtonPressed()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            PRODUCER_LIST_POSITION_STATE_KEY
        )?.observe(viewLifecycleOwner) { result ->
            presenter.onProducerFilterableListPositionReceived(result)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            SUPPLIER_LIST_POSITION_STATE_KEY
        )?.observe(viewLifecycleOwner) { result ->
            presenter.onSupplierFilterableListPositionReceived(result)
        }
    }

    override fun showEditModeElements() {
        binding.editButtonsBlock.visibility = View.VISIBLE
        binding.editTitle.visibility = View.VISIBLE
        binding.editPrice.visibility = View.VISIBLE
        binding.editProducer.visibility = View.VISIBLE
        binding.editSupplier.visibility = View.VISIBLE
    }

    override fun hideEditModeElements() {
        binding.editButtonsBlock.visibility = View.INVISIBLE
        binding.editTitle.visibility = View.GONE
        binding.editPrice.visibility = View.GONE
        binding.editProducer.visibility = View.GONE
        binding.editSupplier.visibility = View.GONE
    }

    override fun showViewModeElements() {
        binding.editProductButton.visibility = View.VISIBLE
        binding.title.visibility = View.VISIBLE
        binding.price.visibility = View.VISIBLE
        binding.producer.visibility = View.VISIBLE
        binding.supplier.visibility = View.VISIBLE
    }

    override fun hideViewModeElements() {
        binding.editProductButton.visibility = View.INVISIBLE
        binding.title.visibility = View.GONE
        binding.price.visibility = View.GONE
        binding.producer.visibility = View.GONE
        binding.supplier.visibility = View.GONE
    }

    override fun setEditModeElementsToDefault() {
        binding.editTitle.setText("")
        binding.editPrice.setText("")
    }

    override fun setViewModeTextViews(
        title: String,
        price: Double,
        producerName: String,
        supplierName: String
    ) {
        binding.title.text = title
        binding.price.text = price.toString()
        binding.producer.text = producerName
        binding.supplier.text = supplierName
    }

    override fun setEditModeTextViews(
        title: String,
        price: Double,
        producerName: String,
        supplierName: String
    ) {
        binding.editTitle.setText(title)
        binding.editPrice.setText(price.toString())
        binding.editProducer.text = producerName
        binding.editSupplier.text = supplierName
    }

    override fun startLoadingAnimation() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    override fun stopLoadingAnimation() {
        binding.loadingBar.visibility = View.GONE
    }

    override fun navigateToProducerListDialog(producerList: Array<String>) {
        val action = Task3ProductCardDialogDirections.filterableList(
            producerList,
            PRODUCER_LIST_POSITION_STATE_KEY
        )
        findNavController().navigate(action)
    }

    override fun navigateToSupplierListDialog(supplierList: Array<String>) {
        val action = Task3ProductCardDialogDirections.filterableList(
            supplierList,
            SUPPLIER_LIST_POSITION_STATE_KEY
        )
        findNavController().navigate(action)
    }

    override fun setProducerEditTextView(producerName: String) {
        binding.editProducer.text = producerName
    }

    override fun setSupplierEditTextView(supplierName: String) {
        binding.editSupplier.text = supplierName
    }

    override fun sendProductToStateHandle(product: ProductResponse) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            Task3Fragment.UPDATED_PRODUCT_STATE_KEY,
            ProductLiveData(args.productPosition, product)
        )
    }

    override fun enableUserInteraction() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun disableUserInteraction() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    companion object {
        const val PRODUCER_LIST_POSITION_STATE_KEY = "producerListPosition"
        const val SUPPLIER_LIST_POSITION_STATE_KEY = "supplierListPosition"
    }
}