package com.example.myapplication.fragment.task3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.contract.task3.data.dto.ProductResponse
import java.util.*

class Task3ProductsAdapter(
    private val productList: ArrayList<ProductResponse>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<Task3ProductsAdapter.ProductViewHolder>(), Filterable {
    private var filteredList = ArrayList(productList)

    inner class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val icon: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val price: TextView = itemView.findViewById(R.id.price)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
           onItemClickListener.onItemClick(null ,v , layoutPosition, getItemId(layoutPosition))
        }
    }

    /*override fun getView(position: Int, convertView: View?, parent: ViewGroup): ProductViewHolder {
        var currentItemView = convertView

        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.task3_list_element, parent, false)
        }

        val currentProduct = getItem(position)

        currentItemView?.findViewById<ImageView>(R.id.image)
            ?.setImageResource(R.drawable.ic_skull) // TODO: вставлять реальную картинку
        //productsImage?.setImageResource(currentProduct.getNumbersImageId())

        currentItemView?.findViewById<TextView>(R.id.title)?.text = currentProduct.name

        currentItemView?.findViewById<TextView>(R.id.price)?.text =
            context.getString(R.string.task_3_product_price, currentProduct.price)

        return currentItemView!!
    }*/

    //override fun getCount() = filteredList.size

    //override fun getItem(position: Int): ProductResponse = filteredList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterString = constraint.toString().lowercase(Locale.getDefault())
            val results = FilterResults()
            val list: List<ProductResponse> = productList
            val count = list.size
            val nlist = ArrayList<ProductResponse>(count)
            var filterableString: String
            for (i in 0 until count) {
                filterableString = list[i].name
                if (filterableString.lowercase(Locale.getDefault()).contains(filterString)) {
                    nlist.add(
                        ProductResponse(
                            list[i].id,
                            filterableString,
                            list[i].price,
                            list[i].producer,
                            list[i].supplier
                        )
                    )
                }
            }
            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            filteredList = results.values as ArrayList<ProductResponse>
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task3_list_element, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.icon.setImageResource(R.drawable.ic_skull) // TODO: вставлять реальную картинку
        //productsImage?.setImageResource(currentProduct.getNumbersImageId())

        val currentProduct = productList[position]

        holder.title.text = currentProduct.name

        holder.price.text =
            holder.itemView.context.getString(R.string.task_3_product_price, currentProduct.price)
    }

    override fun getItemCount(): Int {
        return if (filteredList.size > productList.size) productList.size else filteredList.size
    }
}