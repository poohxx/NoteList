package com.poohxx.notelist.billing

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*

class BillingManager(val activity: AppCompatActivity) {

    init {
        setupBillingClient()
    }


    private var bClient: BillingClient? = null
    private fun setupBillingClient() {
        bClient = BillingClient.newBuilder(activity).setListener(getPurchaseListener())
            .enablePendingPurchases().build()
    }

    private fun savePref(isPurchase: Boolean) {
        val pref = activity.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(REMOVE_ADS_KEY, isPurchase)
        editor.apply()

    }

    fun startConnection() {
        bClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                getItem()
            }
        })
    }

    private fun getItem() {
        val skuList = ArrayList<String>()
        skuList.add(REMOVE_AD_ITEM)
        val skuDetails = SkuDetailsParams.newBuilder()
        skuDetails.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        bClient?.querySkuDetailsAsync(skuDetails.build()) { bResult, list ->
            kotlin.run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (list != null) {
                        if (list.isNotEmpty()) {
                            val bFlowParams =
                                BillingFlowParams.newBuilder().setSkuDetails(list[0]).build()
                            bClient?.launchBillingFlow(activity, bFlowParams)
                        }
                    }
                }

            }
        }
    }

    private fun getPurchaseListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { bResult, list ->
            run {
                if (bResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    list?.get(0)?.let { nonConsumableItem(it) }

                }
            }
        }
    }


    private fun nonConsumableItem(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()
                bClient?.acknowledgePurchase(acParams) {
                    if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                        savePref(true)
                        Toast.makeText(
                            activity,
                            "I'll find You and I'll thank You",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        savePref(false)
                        Toast.makeText(
                            activity,
                            "What if I told you Failure is the essence of self discovery?",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }

    fun closeConnection() {
        bClient?.endConnection()
    }

    companion object {
        const val REMOVE_AD_ITEM = "remove_ad_item_id"
        const val MAIN_PREF = "main_pref"
        const val REMOVE_ADS_KEY = "remove_ads_key"
    }
}