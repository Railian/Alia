package ua.railian.alia.alerts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class AliaContextAlerts(val context: Context)

operator fun AliaAlerts.get(context: Context) = AliaContextAlerts(context)

open class AliaActivityAlerts(val activity: AppCompatActivity) : AliaContextAlerts(activity)

operator fun AliaAlerts.get(activity: AppCompatActivity) = AliaActivityAlerts(activity)

open class AliaFragmentAlerts(val fragment: Fragment) : AliaContextAlerts(fragment.requireContext())

operator fun AliaAlerts.get(fragment: Fragment) = AliaFragmentAlerts(fragment)