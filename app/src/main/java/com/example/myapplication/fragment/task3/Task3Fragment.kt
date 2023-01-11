package com.example.myapplication.fragment.task3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.contract.task3.Task3Contract
import com.example.myapplication.contract.task3.Task3ProductCardContract
import com.example.myapplication.contract.task3.data.Task3Repository
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import com.example.myapplication.contract.task3.presenter.ProductLiveData
import com.example.myapplication.contract.task3.presenter.Task3Presenter
import com.example.myapplication.databinding.FragmentTask3Binding


class Task3Fragment : Fragment(), Task3Contract.View, AdapterView.OnItemClickListener {
    /*private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC)
        .build()*/
    private lateinit var presenter: Task3Contract.Presenter
    private lateinit var binding: FragmentTask3Binding
    private lateinit var listAdapter: Task3ProductsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = Task3Presenter(this, Task3Repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTask3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.doAfterTextChanged {
            listAdapter.filter.filter(it)
        }

        binding.barcodeScanButton.setOnClickListener {
            presenter.onBarcodeScanButtonPressed()
        }

        /*binding.productsListView.liste { _: AdapterView<*>, _: View, position: Int, _: Long ->
            presenter.onListItemClicked(position)
        }*/

        setRecyclerViewTouchSupport()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProductLiveData>(
            UPDATED_PRODUCT_STATE_KEY
        )?.observe(viewLifecycleOwner) { result ->
            presenter.onUpdatedProductReceived(result.product, result.position)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ProductResponse>(
            CREATED_PRODUCT_STATE_KEY
        )?.observe(viewLifecycleOwner) { result ->
            presenter.onCreatedProductReceived(result)
        }
    }

    override fun navigateToProductCard(position: Int, productToPass: ProductResponse) {
        val action = Task3FragmentDirections.productDetails(productPosition = position, product = productToPass)
        findNavController().navigate(action)
    }

    override fun startLoadingAnimation() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    override fun stopLoadingAnimation() {
        binding.loadingBar.visibility = View.GONE
    }

    override fun showEmptyListTextView() {
        binding.emptyListTextView.visibility = View.VISIBLE
    }

    override fun hideEmptyListTextView() {
        binding.emptyListTextView.visibility = View.GONE
    }

    override fun setListAdapter(products: ArrayList<ProductResponse>) {
        //listAdapter = Task3ProductsAdapter(requireContext(), products)
        listAdapter = Task3ProductsAdapter(products, this)
        binding.productsRecyclerView.adapter = listAdapter
    }

    override fun navigateToCreateProductCard() {
        findNavController().navigate(Task3FragmentDirections.createProduct())
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

    override fun notifyListAdapterOnItemCreated(position: Int) {
        listAdapter.notifyItemInserted(position)
    }

    override fun notifyListAdapterOnItemDeleted(position: Int) {
        listAdapter.notifyItemRemoved(position)
    }

    override fun notifyListAdapterOnItemChange(position: Int) {
        listAdapter.notifyItemChanged(position)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        presenter.onListItemClicked(position)
    }

    private fun setRecyclerViewTouchSupport() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                presenter.onListItemSwiped(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(binding.productsRecyclerView)
    }

    companion object {
        const val CREATED_PRODUCT_STATE_KEY = "createdProduct"
        const val UPDATED_PRODUCT_STATE_KEY = "updatedProduct"
    }
}