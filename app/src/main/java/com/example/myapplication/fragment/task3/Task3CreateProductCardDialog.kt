package com.example.myapplication.fragment.task3

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.contract.task3.Task3CreateProductCardContract
import com.example.myapplication.contract.task3.data.Task3Repository
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import com.example.myapplication.contract.task3.presenter.Task3CreateProductCardPresenter
import com.example.myapplication.databinding.DialogTask3ProductCardCreateBinding

class Task3CreateProductCardDialog : DialogFragment(), Task3CreateProductCardContract.View {
    private lateinit var presenter: Task3CreateProductCardContract.Presenter
    private lateinit var binding: DialogTask3ProductCardCreateBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogTask3ProductCardCreateBinding.inflate(layoutInflater)
            AlertDialog.Builder(it).setView(binding.root).create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_fragment_background)
        presenter = Task3CreateProductCardPresenter(this, Task3Repository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun startLoadingAnimation() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    override fun stopLoadingAnimation() {
        binding.loadingBar.visibility = View.GONE
    }

    override fun sendProductToStateHandle(product: ProductResponse) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            Task3Fragment.CREATED_PRODUCT_STATE_KEY,
            product
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

    override fun navigateToProducerListDialog(producerList: Array<String>) {
        val action = Task3ProductCardDialogDirections.filterableList(
            producerList,
            Task3ProductCardDialog.PRODUCER_LIST_POSITION_STATE_KEY
        )
        findNavController().navigate(action)
    }

    override fun navigateToSupplierListDialog(supplierList: Array<String>) {
        val action = Task3ProductCardDialogDirections.filterableList(
            supplierList,
            Task3ProductCardDialog.SUPPLIER_LIST_POSITION_STATE_KEY
        )
        findNavController().navigate(action)
    }

    override fun setProducerEditTextView(producerName: String) {
        binding.editProducer.text = producerName
    }

    override fun setSupplierEditTextView(supplierName: String) {
        binding.editSupplier.text = supplierName
    }

    companion object {
        const val PRODUCER_LIST_POSITION_STATE_KEY = "producerListPosition"
        const val SUPPLIER_LIST_POSITION_STATE_KEY = "supplierListPosition"
    }
}
