package me.hatcloud.sms2mail.ui

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.core.SmsObserver
import me.hatcloud.sms2mail.ui.fragment.ConfigurationFragment
import me.hatcloud.sms2mail.ui.fragment.SmsFragment
import me.hatcloud.sms2mail.ui.fragment.ToggleFragment
import me.hatcloud.sms2mail.utils.SMS_INBOX_URI
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

    private val fragments = listOf(ToggleFragment.newInstance(), SmsFragment.newInstance(), ConfigurationFragment.newInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermission(this, Manifest.permission.READ_SMS)) {
            requestPermission(this, Manifest.permission.READ_SMS)
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_toggle
        registerSmsObserver()
    }

    private fun switchFragment(position: Int) {
        if (position !in 0..fragments.size) {
            return
        }
        val fragment = fragments[position]
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun registerSmsObserver() {
        val smsObserver = SmsObserver(this, Handler())
        contentResolver.registerContentObserver(SMS_INBOX_URI, true, smsObserver);
    }
}
