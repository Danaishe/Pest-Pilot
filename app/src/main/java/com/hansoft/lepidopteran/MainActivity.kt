package com.hansoft.lepidopteran

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hansoft.lepidopteran.databinding.ActivityMainBinding
import com.hansoft.lepidopteran.database.chemicaldatabse.Chemical
import com.hansoft.lepidopteran.database.chemicaldatabse.ChemicalViewModel
import com.hansoft.lepidopteran.database.pestsdatabase.Pest
import com.hansoft.lepidopteran.database.pestsdatabase.PestViewModel
import com.hansoft.lepidopteran.frag.HomeFragment
import com.hansoft.lepidopteran.frag.PestFragment
import com.hansoft.lepidopteran.frag.SettingFragment
import com.hansoft.lepidopteran.frag.TackerFragment
import com.hansoft.lepidopteran.helpers.AlarmReceiver
import com.hansoft.lepidopteran.helpers.GeoPermission
import com.hansoft.lepidopteran.helpers.Util


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val util = Util()

    companion object {
        const val CHANNEL_ID = "pest_chemical"
        const val notificationId = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.greenColor)

        GeoPermission.requestPermissions(this)
        createNotificationChannel()
        scheduleNotification(this)

        adddata()

        val homeFragment = HomeFragment()
        val pestFragment = PestFragment()
        val trackerFragment = TackerFragment()
        val settingFragment = SettingFragment()

        setCurrentFragment(homeFragment)


        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> setCurrentFragment(homeFragment)
                R.id.nav_dashboard -> setCurrentFragment(pestFragment)
                R.id.nav_notifications -> setCurrentFragment(trackerFragment)
                R.id.nav_setting -> setCurrentFragment(settingFragment)
            }
            true
        }

    }

    private fun adddata(){
        if(util.getLocalData(this,"add") == "0"){
            val ChemicalViewModel = ViewModelProvider(this)[ChemicalViewModel::class.java]
            val c1 = Chemical(0,"Emamectin Benzoate","Lepidoptera, beetles, thrips",
                "5-25 g/ha","yellow","Conventional","(4''R)-4''-Deoxy-4''-(methylamino)avermectin B1 benzoate")
            val c2 = Chemical(0,"Indoxacarb","Caterpillars, beetles, weevils",
                "75-150 g/ha","yellow","Conventional","Methyl 7-chloro-2,5-dihydro-2-[[methoxy-(trifluoromethyl)phenyl]carbamoyl]indeno[1,2-e][1,3,4]oxadiazine-4a(3H)-carboxylate")
            val c3 = Chemical(0,"Carbaryl","Lepidoptera, beetles, thripsWide range of insects",
                "1-2 kg/ha","red","Conventional","1-naphthyl methylcarbamate")
            val c4 = Chemical(0,"Various (e.g., Abamectin)","Nematodes",
                "Specific to product instructions","yellow","Biological","Varies")
            val c5 = Chemical(0,"Phosplus","General pests",
                "Specific to product instructions","red","Conventional","Varies based on formulation")
            val c6 = Chemical(0,"Lambda-cyhalothrin","Various insects including caterpillars, aphids",
                "20-30 g/ha","red","Conventional",": A racemic mixture of cyano(3-phenoxyphenyl)methyl 3-(2-chloro-3,3,3-trifluoroprop-1-enyl)-2,2-dimethylcyclopropanecarboxylate")
            val c7 = Chemical(0,"Acetamiprid","Aphids, leafhoppers",
                "10-20 g/ha","yellow","Conventional","(E)-N1-[(6-chloropyridin-3-yl)methyl]-N2-cyano-N1-methylacetamidine")
            val c8 = Chemical(0,"Cartap","Caterpillars, beetles",
                "0.5-1 kg/ha","yellow","Conventional","S,S'-(2-dimethylaminotrimethylene) bis(thiocarbamate)")
            val c9 = Chemical(0,"Dimethoate","Aphids, mites, thrips",
                "0.5-1 L/ha","red","Conventional","O,O-Dimethyl S-methylcarbamoylmethyl phosphorodithioate")
            val c10 = Chemical(0,"Tsunami","Effective against a broad spectrum of pests, including caterpillars, beetle larvae, and other insects. The combination provides both immediate insecticidal action and growth regulation.",
                "300ml/ha","red","Conventional","Diflubenzuron and Lambda-cyhalothrin")
            ChemicalViewModel.insert(c1)
            ChemicalViewModel.insert(c2)
            ChemicalViewModel.insert(c3)
            ChemicalViewModel.insert(c4)
            ChemicalViewModel.insert(c5)
            ChemicalViewModel.insert(c6)
            ChemicalViewModel.insert(c7)
            ChemicalViewModel.insert(c8)
            ChemicalViewModel.insert(c9)
            ChemicalViewModel.insert(c10)

            val pestViewModel = ViewModelProvider(this)[PestViewModel::class.java]
            val p1 = Pest(0,"a01","African Monarch","Danaus chrysippus",
                 "Orange wings with black and white markings","Low")
            val p2 = Pest(0,"a02","African Cotton Leafworm","Spodoptera littoralis",
                "Brown moths; greenish caterpillars with stripes","high")
            val p3 = Pest(0,"a03","Maize Stalk Borer","Busseola fusca",
                "Pinkish-brown caterpillars with dark spots","High")
            val p4 = Pest(0,"a04","Red Bollworm","Diparopsis castanea",
                "Reddish-brown moths; pink caterpillars with spots","High")
            val p5 = Pest(0,"a05","Citrus Swallowtail","Papilio demodocus",
                "Black and yellow wings with red and blue markings","Medium")
            val p6 = Pest(0,"a06","Fall Armyworm","Spodoptera frugiperda",
                "Brown/gray moths; striped caterpillars","High")
            val p7 = Pest(0,"a07","Tobacco Cutworm","Spodoptera litura",
                "Dark brown moths; green/brown caterpillars with stripes.","High")
            val p8 = Pest(0,"a08","Diamondback Moth","Plutella xylostella",
                "Small moths with brown and cream diamond patterns","High")
            val p9 = Pest(0,"a09","African Sugarcane Borer","Eldana saccharina",
                "Brownish-yellow moths; pinkish-white larvae","High")
            val p10 = Pest(0, "a10","Common Leopard","Phalanta phalantha",
                "Bright orange and black patterns","Low")


            pestViewModel.insert(p1)
            pestViewModel.insert(p2)
            pestViewModel.insert(p3)
            pestViewModel.insert(p4)
            pestViewModel.insert(p5)
            pestViewModel.insert(p6)
            pestViewModel.insert(p7)
            pestViewModel.insert(p8)
            pestViewModel.insert(p9)
            pestViewModel.insert(p10)

            util.saveLocalData(this,"add","1")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Notification Channel"
            val descriptionText = "A description of the channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chemical)
            .setContentTitle("Pests Pilot")
            .setContentText("Add Pests Chemical Tracking info")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    private fun scheduleNotification(context: Context) {
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_MUTABLE
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val interval: Long = util.getLocalData(this,"d")!!.toLong()*60000
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            interval, pendingIntent
        )
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!GeoPermission.hasGeoPermissions(this)) {
            if (GeoPermission.shouldShowRequestPermissionRationale(this)) {
                GeoPermission.launchPermissionSettings(this)
            }
        }
    }

}