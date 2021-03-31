package com.example.vehicle_policy.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "vehicle_policy")
data class VehiclePolicy(
    val companyName: String,
    val companyLogo: Int,
    val policyNumber: String,
    val vehicleNumber: String,
    val vehicleName: String,
    val purchaseDate: Long,
    val expiryDate: Long,
    val policyAmount: String,
    val policyType: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable

@Parcelize
@Entity(tableName = "vehiclePolicy_companys")
data class VehiclePolicyCompanys(
    val companyName: String,
    val companyLogo: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable