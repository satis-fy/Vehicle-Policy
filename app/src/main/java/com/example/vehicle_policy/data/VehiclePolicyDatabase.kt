package com.example.vehicle_policy.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vehicle_policy.R
import com.example.vehicle_policy.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [VehiclePolicy::class,VehiclePolicyCompanys::class], version = 1)
abstract class VehiclePolicyDatabase: RoomDatabase() {

    abstract fun vehiclePolicyDao(): VehiclePolicyDao

    class Callback @Inject constructor(
        private val database: Provider<VehiclePolicyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

        //this class execute only one time when database was created
            val dao = database.get().vehiclePolicyDao()
            applicationScope.launch {
                dao.apply {
                    
                     insertCompany(VehiclePolicyCompanys("Life Insurance Corporation – LIC", R.drawable.hcl1))
                     insertCompany(VehiclePolicyCompanys("Aditya Birla Sun Life Insurance", R.drawable.hcl2))
                     insertCompany(VehiclePolicyCompanys("Aegon Life Insurance", R.drawable.hcl3))
                     insertCompany(VehiclePolicyCompanys("Appolo Munich Health Insurance", R.drawable.hcl4))
                     insertCompany(VehiclePolicyCompanys("Aviva Life Insurance", R.drawable.hcl5))
                     insertCompany(VehiclePolicyCompanys("Bajaj Allianz Life Insurance", R.drawable.hcl6))
                     insertCompany(VehiclePolicyCompanys("Bharti Axa Life Insurance", R.drawable.hcl7))
                     insertCompany(VehiclePolicyCompanys("Canara HSBC – OBC Life Insurance", R.drawable.hcl8))
                     insertCompany(VehiclePolicyCompanys("Cholamandalam General Insurance", R.drawable.hcl9))
                     insertCompany(VehiclePolicyCompanys("Exide Life Insurance", R.drawable.hcl10))
                     insertCompany(VehiclePolicyCompanys("Future Generali Insurance", R.drawable.hcl11))
                     insertCompany(VehiclePolicyCompanys("HDFC Ergo General Insurance", R.drawable.hcl12))
                     insertCompany(VehiclePolicyCompanys("HDFC Life Insurance Co. Ltd.", R.drawable.hcl13))
                     insertCompany(VehiclePolicyCompanys("ICICI Lombard", R.drawable.hcl14))
                     insertCompany(VehiclePolicyCompanys("ICICI Prudential Life Insurance", R.drawable.hcl15))
                     insertCompany(VehiclePolicyCompanys("IDBI Federal Life Insurance", R.drawable.hcl16))
                     insertCompany(VehiclePolicyCompanys("Iffco Tokio General Insurance", R.drawable.hcl17))
                     insertCompany(VehiclePolicyCompanys("IndiaFirst Life Insurance", R.drawable.hcl18))
                     insertCompany(VehiclePolicyCompanys("Max Bupa Health Insurance", R.drawable.hcl19))
                     insertCompany(VehiclePolicyCompanys("Max Life Insurance", R.drawable.hcl20))
                     insertCompany(VehiclePolicyCompanys("PNB MetLife Life Insurance", R.drawable.hcl21))
                     insertCompany(VehiclePolicyCompanys("Pramerica Life Insurance Ltd.", R.drawable.hcl22))
                     insertCompany(VehiclePolicyCompanys("Reliance Nippon Life Insurance Co. Ltd.", R.drawable.hcl23))
                     insertCompany(VehiclePolicyCompanys("Royal Sundaram General Insurance", R.drawable.hcl24))
                     insertCompany(VehiclePolicyCompanys("SBI General Health Insurance", R.drawable.hcl25))
                     insertCompany(VehiclePolicyCompanys("SBI Life Insurance", R.drawable.hcl26))
                     insertCompany(VehiclePolicyCompanys("Shriram Life Insurance Co. Ltd.", R.drawable.hcl27))
                     insertCompany(VehiclePolicyCompanys("Star Union Dai Ichi Life Insurance", R.drawable.hcl28))
                     insertCompany(VehiclePolicyCompanys("Tata AIA Life Insurance", R.drawable.hcl29))
                     insertCompany(VehiclePolicyCompanys("The Oriental Insurance Co. Ltd.", R.drawable.hcl30))
                }
            }
        }
    }
}