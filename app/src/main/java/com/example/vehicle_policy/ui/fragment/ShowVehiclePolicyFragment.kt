package com.example.vehicle_policy.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.vehicle_policy.R
import com.example.vehicle_policy.databinding.FragmentShowVehiclePolicyBinding
import com.example.vehicle_policy.ui.viewmodel.ShowVehiclePolicyViewModel
import com.example.vehicle_policy.util.convertLongToTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowVehiclePolicyFragment constructor(
    var viewModel: ShowVehiclePolicyViewModel? = null
) : Fragment(R.layout.fragment_show_vehicle_policy) {

    private lateinit var binding: FragmentShowVehiclePolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(this).get(ShowVehiclePolicyViewModel::class.java)
        binding = FragmentShowVehiclePolicyBinding.bind(view)

        binding.apply {

            ivBackVps.setOnClickListener {
                findNavController().popBackStack()
            }
            viewModel?.apply {
                tvCompanyNameVps.text = companyName
                tvPolicyNumberVps.text = policyNumber
                tvVehicleNumberVps.text = vehicleNumber
                tvVehicleNameVps.text = vehicleName
                tvPurchaseDateVps.text = convertLongToTime(purchaseDate)
                tvExpiryDateVps.text = convertLongToTime(expiryDate)
                tvPolicyAmountVps.text = policyAmount

                if (policyType.isNotEmpty()) {
                    tvPolicyTypeTitleVps.visibility = View.VISIBLE
                    tvPolicyTypeVps.visibility = View.VISIBLE
                    tvPolicyTypeVps.text = policyType
                }
            }
            ivCopyClipBoardIconVps.setOnClickListener {
                val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("VehiclePolicy", viewModel?.copyClipBoard())
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity,R.string.CopyTextToast,Toast.LENGTH_SHORT).show()
            }
        }
    }
}