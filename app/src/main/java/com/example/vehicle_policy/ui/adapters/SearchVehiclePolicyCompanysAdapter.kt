package com.example.vehicle_policy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.databinding.ItemSearchVehiclePolicyCompanyBinding

class SearchVehiclePolicyCompanysAdapter(private val listener: OnItemClickListener) :
    androidx.recyclerview.widget.ListAdapter<VehiclePolicyCompanys,
            SearchVehiclePolicyCompanysAdapter.SearchVehiclePolicyCompanysViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVehiclePolicyCompanysViewHolder {
        val binding = ItemSearchVehiclePolicyCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchVehiclePolicyCompanysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchVehiclePolicyCompanysViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class SearchVehiclePolicyCompanysViewHolder(private val binding: ItemSearchVehiclePolicyCompanyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val vehiclePolicyCompanys = getItem(position)
                        listener.onItemClick(vehiclePolicyCompanys)
                    }
                }
            }
        }

        fun bind(vehiclePolicyCompanys: VehiclePolicyCompanys) {
            binding.apply {
                tvCompanyNameVpSearch.text = vehiclePolicyCompanys.companyName
                ivCompanyLogoVpSearch.setImageResource(vehiclePolicyCompanys.companyLogo)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(vehiclePolicyCompanys: VehiclePolicyCompanys)
    }

    class DiffCallback : DiffUtil.ItemCallback<VehiclePolicyCompanys>() {
        override fun areItemsTheSame(oldItem: VehiclePolicyCompanys, newItem: VehiclePolicyCompanys) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehiclePolicyCompanys, newItem: VehiclePolicyCompanys) =
            oldItem == newItem
    }
}