package com.example.vehicle_policy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.databinding.ItemListVehiclePolicyBinding

class ListVehiclePolicyAdapter(private val listener: OnItemClickListener):
    ListAdapter<VehiclePolicy, ListVehiclePolicyAdapter.VehiclePolicyViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclePolicyViewHolder {
        val binding =
            ItemListVehiclePolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiclePolicyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehiclePolicyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class VehiclePolicyViewHolder(private val binding: ItemListVehiclePolicyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        val vehiclePolicy = getItem(position)
                        listener.onItemClick(vehiclePolicy)
                    }
                }
                ivOptionsVplItem.setOnClickListener {
                    val position = adapterPosition
                    if(position!=RecyclerView.NO_POSITION){
                        val vehiclePolicy = getItem(position)
                        listener.onOptionIconClick(vehiclePolicy,ivOptionsVplItem)
                    }
                }
            }
        }

        fun bind(vehiclePolicy: VehiclePolicy) {
            binding.apply {
                tvPolicyNumberVplItem.text = vehiclePolicy.policyNumber
                tvVehicleNumberVplItem.text = vehiclePolicy.vehicleNumber
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(vehiclePolicy: VehiclePolicy)
        fun onOptionIconClick(vehiclePolicy: VehiclePolicy,view: View)
    }

    class DiffCallback : DiffUtil.ItemCallback<VehiclePolicy>() {
        override fun areItemsTheSame(oldItem: VehiclePolicy, newItem: VehiclePolicy) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VehiclePolicy, newItem: VehiclePolicy) =
            oldItem == newItem
    }
}