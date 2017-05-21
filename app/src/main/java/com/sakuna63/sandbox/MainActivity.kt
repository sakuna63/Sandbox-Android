package com.sakuna63.sandbox

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaCodecList
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.sakuna63.sandbox.databinding.ActivityMainBinding
import customtabsclient.shared.CustomTabsHelper

class MainActivity : AppCompatActivity() {

    private var client: CustomTabsClient? = null
    private var session: CustomTabsSession? = null
    private val uri = Uri.parse("https://google.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val packageName = CustomTabsHelper.getPackageNameToUse(this)
        packageName.log()
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {
                this@MainActivity.client = client
//                client?.warmup(0)

                this@MainActivity.session = client?.newSession(object: CustomTabsCallback() {
                    override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
                        super.onNavigationEvent(navigationEvent, extras)
                        navigationEvent.log()
                    }
                })
//                this@MainActivity.session?.mayLaunchUrl(uri, null, null)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                this@MainActivity.client = null
            }
        }

        CustomTabsClient.bindCustomTabsService(this, packageName, connection)


        binding.button.setOnClickListener {
            CustomTabsIntent.Builder()
                    .setToolbarColor(Color.RED)
                    .setActionButton(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher), "Action!!!!!!!!!", PendingIntent.getActivity(this, 100, Intent(), 100))
                    .enableUrlBarHiding()
                    .build()
                    .launchUrl(this, uri)
        }
    }
}

fun Any?.log() = Log.d("Android-Sandbox", this?.toString())
