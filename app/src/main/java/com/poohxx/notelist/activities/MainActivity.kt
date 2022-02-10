package com.poohxx.notelist.activities

import TaskListNamesFragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.poohxx.notelist.R
import com.poohxx.notelist.billing.BillingManager
import com.poohxx.notelist.databinding.ActivityMainBinding
import com.poohxx.notelist.dialogs.NewListDialog
import com.poohxx.notelist.fragments.FragmentManager
import com.poohxx.notelist.fragments.NoteFragment
import com.poohxx.notelist.settings.SettingsActivity


class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.task_list
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""
    private var iAd: InterstitialAd? = null
    private var counterAD = 0
    private var counterAdMax = 4
    private lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        pref=getSharedPreferences(BillingManager.MAIN_PREF, MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        currentTheme = defPref.getString("choose_theme_key", "Yellow").toString()
        setContentView(binding.root)


        FragmentManager.setFragment(
            TaskListNamesFragment.newInstance(),
            this
        )
        setBotNavViewListener()
       if (!pref.getBoolean(BillingManager.REMOVE_ADS_KEY, false)) loadInterAd()
    }

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            getString(R.string.inter_ad_id),
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    iAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    iAd = null
                }

            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (iAd != null && counterAD > counterAdMax) {
            iAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                    adListener.onFinish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    iAd = null
                    loadInterAd()
                }

                override fun onAdShowedFullScreenContent() {
                    iAd = null
                    loadInterAd()
                }
            }
            counterAD = 0
            iAd?.show(this)
        } else {
            counterAD++
            adListener.onFinish()
        }
    }

    private fun setBotNavViewListener() {
        binding.btmNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        }
                    })

                }
                R.id.new_task -> {
                    FragmentManager.currentFrag?.onClickNew()

                }
                R.id.task_list -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            currentMenuItemId = R.id.task_list
                            FragmentManager.setFragment(
                                TaskListNamesFragment.newInstance(), this@MainActivity
                            )
                        }
                    })
                }

                R.id.notes -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(
                                NoteFragment.newInstance(), this@MainActivity
                            )
                        }
                    })

                }
            }
            true

        }

    }

    override fun onResume() {
        super.onResume()
        binding.btmNavView.selectedItemId = currentMenuItemId
        if (defPref.getString("choose_theme_key", "Yellow") != currentTheme) recreate()
    }

    private fun getSelectedTheme(): Int {
        return if (defPref.getString("choose_theme_key", "Yellow") == "Yellow") {
            R.style.Theme_NoteListYellow
        } else {
            R.style.Theme_NoteListPurple
        }
    }

    override fun onClick(name: String) {
        Toast.makeText(this, "Name: $name", Toast.LENGTH_SHORT).show()
    }

    interface AdListener {
        fun onFinish()
    }
}