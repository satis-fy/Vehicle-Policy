package com.example.vehicle_policy.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.databinding.ItemVehiclePolicyBinding
import javax.inject.Inject

class VehiclePolicyAdapter @Inject constructor (private val listener: OnItemClickListener) :
    ListAdapter<VehiclePolicyCompanys, VehiclePolicyAdapter.VehiclePolicyViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclePolicyViewHolder {
        val binding = ItemVehiclePolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiclePolicyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehiclePolicyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class VehiclePolicyViewHolder(private val binding: ItemVehiclePolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val vehicleCompany = getItem(position)
                        listener.onItemClick(vehicleCompany)
                    }
                }
            }
        }

        fun bind(vehiclePolicyCompanys: VehiclePolicyCompanys) {
            binding.apply {
                tvCompanyNameVpItem.text = vehiclePolicyCompanys.companyName
                ivCompanyIconVpItem.setImageResource(vehiclePolicyCompanys.companyLogo)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(vehicleCompany: VehiclePolicyCompanys)
    }

    class DiffCallBack : DiffUtil.ItemCallback<VehiclePolicyCompanys>() {
        override fun areItemsTheSame(oldItem: VehiclePolicyCompanys, newItem: VehiclePolicyCompanys) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehiclePolicyCompanys, newItem: VehiclePolicyCompanys) =
            oldItem == newItem
    }
}