package ru.contlog.mobile.helper.rvadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.contlog.mobile.helper.R
import ru.contlog.mobile.helper.databinding.ItemDivisionBinding
import ru.contlog.mobile.helper.databinding.ItemProductBinding
import ru.contlog.mobile.helper.model.Division
import ru.contlog.mobile.helper.model.Product
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager

class ProductsRVAdapter() :
    RecyclerView.Adapter<ProductsRVAdapter.VH>() {
    private val productsList = mutableListOf<Product>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)

        return VH(binding)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int
    ) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int = productsList.size

    fun setData(newData: List<Product>) {
        val oldCount = productsList.size
        productsList.clear()
        notifyItemRangeRemoved(0, oldCount)
        productsList.addAll(newData)
        notifyItemRangeChanged(0, productsList.size)
    }

    class VH(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productCode.text = product.productCode
            binding.barcodeCode.text = product.barcodeCode.toString()
            binding.productName.text = product.productLinkString

            binding.productPlaces.layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.VERTICAL,
                false
            )

            val count = product.places.sumOf {
                it.leftoversFree + it.leftoversInReserve
            }
            if (product.places.isEmpty()) {
                binding.expansionTitle.text =
                    binding.root.context.getString(R.string.label_no_product_places)
                binding.expansionHeader.setOnClickListener {}
                binding.expansionIndicator.visibility = View.GONE

                binding.productPlaces.adapter = null
            } else {
                binding.expansionTitle.text =
                    binding.root.context.getString(R.string.title_places_exp, count)
                binding.expansionHeader.setOnClickListener {
                    binding.expansionContent.visibility =
                        if (binding.expansionContent.isGone) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    binding.expansionIndicator.setImageDrawable(
                        if (binding.expansionContent.isGone) {
                            AppCompatResources.getDrawable(
                                binding.root.context,
                                R.drawable.outline_arrow_drop_down_24
                            )
                        } else {
                            AppCompatResources.getDrawable(
                                binding.root.context,
                                R.drawable.outline_arrow_drop_up_24
                            )
                        }
                    )
                }

                binding.productPlaces.adapter =
                    ProductPlacesRVAdapter(product.unitName, product.places.sortedBy {
                        !it.primaryPlace
                    })
            }

            Glide.with(binding.root).load(product.imageSrc).into(binding.productImage)
        }
    }
}