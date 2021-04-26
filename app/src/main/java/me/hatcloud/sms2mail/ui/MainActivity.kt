package me.hatcloud.sms2mail.ui

import android.Manifest
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.databinding.ActivityMainBinding
import me.hatcloud.sms2mail.ui.fragment.ConfigurationFragment
import me.hatcloud.sms2mail.ui.fragment.SmsFragment
import me.hatcloud.sms2mail.ui.fragment.ToggleFragment
import me.hatcloud.sms2mail.utils.checkPermission
import me.hatcloud.sms2mail.utils.requestPermission

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_toggle -> {
                switchFragment(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_messages -> {
                switchFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_configuration -> {
                switchFragment(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val navigation by lazy{
        findViewById<BottomNavigationView>(R.id.navigation)
    }

    private val fragments = listOf(ToggleFragment.newInstance(),
            SmsFragment.newInstance(),
            ConfigurationFragment.newInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermission(this, Manifest.permission.READ_SMS)) {
            requestPermission(this, Manifest.permission.READ_SMS)
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_toggle
    }

    private fun switchFragment(position: Int) {
        if (position !in 0..fragments.size) {
            return
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragments.forEachIndexed { index, fragment ->
            if (index != position) {
                if (fragment.isAdded) {
                    fragmentTransaction.hide(fragment)
                }
            } else {
                if (fragment.isAdded) {
                    fragmentTransaction.show(fragment)
                } else {
                    fragmentTransaction.add(R.id.container, fragment)
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

}
