package com.example.vehicle_policy.ui.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vehicle_policy.R
import com.example.vehicle_policy.databinding.FragmentUpdateVehiclePolicyBinding
import com.example.vehicle_policy.ui.viewmodel.UpdateVehiclePolicyViewModel
import com.example.vehicle_policy.util.Response
import com.example.vehicle_policy.util.convertLongToTime
import com.example.vehicle_policy.util.exhaustive
import com.example.vehicle_policy.util.mySnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UpdateVehiclePolicyFragment : Fragment(R.layout.fragment_update_vehicle_policy) {

    lateinit var viewModel: UpdateVehiclePolicyViewModel
    private lateinit var binding: FragmentUpdateVehiclePolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUpdateVehiclePolicyBinding.bind(view)
        viewModel = ViewModelProvider(this).get(UpdateVehiclePolicyViewModel::class.java)

        binding.apply {

            ivBackVpu.setOnClickListener {
                findNavController().popBackStack()
            }
            etCompanyNameVpu.setText(viewModel.companyName)
            etPolicyNumberVpu.setText(viewModel.policyNumber)
            etPolicyNumberVpu.addTextChangedListener {
                viewModel.policyNumber = it.toString()
                checkUpdatePolicyButton()
            }
            etVehicleNumberVpu.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
            etVehicleNumberVpu.setText(viewModel.vehicleNumber)
            etVehicleNumberVpu.addTextChangedListener {
                viewModel.vehicleNumber = it.toString()
                checkUpdatePolicyButton()
            }
            etVehicleNameVpu.setText(viewModel.vehicleName)
            etVehicleNameVpu.addTextChangedListener {
                viewModel.vehicleName = it.toString()
                checkUpdatePolicyButton()
            }
            viewModel.apply {
                etPurchaseDateVpu.setText(convertLongToTime(purchaseDate))
                etPurchaseDateVpu.setOnClickListener {
                    datePickerPurchase()
                }
                etExpiryDateVpu.setText(convertLongToTime(expiryDate))
                etExpiryDateVpu.setOnClickListener {
                    datePickerExpiry()
                }
            }
            etPolicyTypeVpu.setText(viewModel.policyType)
            etPolicyTypeVpu.addTextChangedListener {
                viewModel.policyType = it.toString()
                checkUpdatePolicyButton()
            }
            etPolicyAmountVpu.setText(viewModel.policyAmount)
            etPolicyAmountVpu.addTextChangedListener {
                viewModel.policyAmount = it.toString()
                checkUpdatePolicyButton()
            }
            btUpdatePolicyVpu.isEnabled = viewModel.updatePolicyButton
            btUpdatePolicyVpu.setOnClickListener {
                viewModel.onUpdateClick()
            }
            checkUpdatePolicyButton()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { response ->
                when (response) {
                    is Response.NotValid -> {
                        if (response.higherPurchaseDate) {
                            view.mySnackbar(R.string.SnackBar_PolicyHigherPurchaseDate)
                        } else { view.mySnackbar(R.string.SnackBar_PolicyFieldEmpty)
                        }
                    }
                    is Response.Valid -> {
                        view.mySnackbar(R.string.SnackBar_PolicyUpdate)
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }

    private fun checkUpdatePolicyButton() {
        binding.apply {
            btUpdatePolicyVpu.isEnabled = etCompanyNameVpu.text.toString().isNotEmpty() &&
                    etPolicyNumberVpu.text.toString().isNotEmpty() &&
                    etVehicleNumberVpu.text.toString().isNotEmpty() &&
                    etVehicleNameVpu.text.toString().isNotEmpty() &&
                    etPurchaseDateVpu.text.toString().isNotEmpty() &&
                    etExpiryDateVpu.text.toString().isNotEmpty() &&
                    etPolicyAmountVpu.text.toString().isNotEmpty() &&
                    viewModel.companyLogo != 0 &&
                    viewModel.purchaseDate < viewModel.expiryDate

            viewModel.updatePolicyButton = btUpdatePolicyVpu.isEnabled
        }
    }

    private fun datePickerPurchase() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year2, monthOfYear, dayOfMonth ->
            c.set(year2, monthOfYear, dayOfMonth)
            binding.etPurchaseDateVpu.setText("${dayOfMonth}/${monthOfYear + 1}/${year2}")
            viewModel.purchaseDate = c.timeInMillis
            checkUpdatePolicyButton()
        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun datePickerExpiry() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year2, monthOfYear, dayOfMonth ->
            c.set(year2, monthOfYear, dayOfMonth)
            binding.etExpiryDateVpu.setText("${dayOfMonth}/${monthOfYear + 1}/${year2}")
            viewModel.expiryDate = c.timeInMillis
            checkUpdatePolicyButton()
        }, year, month, day)
        dpd.datePicker.minDate = viewModel.purchaseDate
        dpd.show()
    }
}