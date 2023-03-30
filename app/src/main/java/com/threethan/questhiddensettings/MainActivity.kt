package com.threethan.questhiddensettings

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var audioApps: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun settingsActivity(intentname: String) {
            val intent = Intent()
            intent.component =
                ComponentName("com.android.settings", "com.android.settings.Settings\$"+intentname)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }


        val brightnessBtn = findViewById<Button>(R.id.brightnessBtn)
        val dnsBtn = findViewById<Button>(R.id.dnsBtn)
        val datetimeBtn = findViewById<Button>(R.id.datetimeBtn)
        val appsBtn = findViewById<Button>(R.id.appsBtn)
        val accessibilityBtn = findViewById<Button>(R.id.accessiblityBtn)
        val infoBtn = findViewById<Button>(R.id.infoBtn)

        brightnessBtn.setOnClickListener {
            if (Settings.System.canWrite(applicationContext)) {
                Settings.System.putInt(application.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 0)
            }
            else {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + applicationContext.packageName));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        dnsBtn.setOnClickListener {
            val items = arrayOf<CharSequence>("Mullvad Adblock (Blocks Ads, Private)", "Mullvad DNS (Private)", "Adguard DNS (Blocks Ads)", "Cloudflare DNS (Fast)", "Quad9 (Private)", "Google DNS")
            val hostnames = arrayOf<CharSequence>("adblock.doh.mullvad.net", "doh.mullvad.net", "dns.adguard.com", "1dot1dot1dot1.cloudflare-dns.com", "dns.google")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select a DNS provider to copy it's hostname to the clipboard")
            builder.setItems(items) { _, item ->
                val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("label",hostnames[item])
                clipBoard.setPrimaryClip(clipData)

                // Show instructions
                val builder2 = AlertDialog.Builder(this)
                builder2.setTitle("Copied hostname!")
                builder2.setMessage(
                    "1. Click next to open network settings\n2. Scroll down, press Advanced, then Private DNS\n3. Select Private DNS provider hostname. Delete all the text, then press and hold to paste \""+hostnames[item]+"\"\n4. Press OK and enjoy!"
                )
                builder2.setPositiveButton("Next", DialogInterface.OnClickListener { _, _ ->
                    settingsActivity("NetworkDashboardActivity")
                })
                builder2.create().show()
                }
            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.create().show()
        }

        datetimeBtn.setOnClickListener {
            settingsActivity("DateTimeSettingsActivity")
        }

        appsBtn.setOnClickListener {
            val intent = Intent()
            intent.component =
                ComponentName("com.android.settings", "com.android.settings.applications.ManageApplications")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        accessibilityBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Notice")
            builder.setMessage(
                "Many of the settings here don't work.\nYou can enable mono audio or adjust balance,\nenable high-contrast text system wide,\nor adjust text size without restarting.\n\nUse Oculus's accessibility for working color filters."
            )
            builder.setPositiveButton("Continue", { _, _ ->
                settingsActivity("AccessibilitySettingsActivity")
            })
            builder.setNegativeButton("Cancel", {_,_ -> })
            builder.create().show()
        }

        infoBtn.setOnClickListener {
            settingsActivity("MyDeviceInfoActivity")
        }

    }
}
