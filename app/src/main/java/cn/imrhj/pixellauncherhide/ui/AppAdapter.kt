package cn.imrhj.pixellauncherhide.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import cn.imrhj.pixellauncherhide.R
import cn.imrhj.pixellauncherhide.model.AppInfo

/**
 * Created by rhj on 23/02/2018.
 */
class AppAdapter(apps: List<AppInfo>, preferences: SharedPreferences) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {
    private var mApps = apps
    private val mPreferences = preferences
    @SuppressLint("ApplySharedPref")

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val info = mApps[position]
        holder.icon?.setImageDrawable(info.appIcon)
        holder.name?.text = info.appName
        holder.packageName?.text = info.packageName
        holder.hideSwitch?.setOnCheckedChangeListener(null)
        holder.hideSwitch?.isChecked = info.hide
        holder.hideSwitch?.setOnCheckedChangeListener { _, isChecked ->
            mApps[position].hide = isChecked
            mPreferences.edit().putBoolean(info.packageName.toString(), isChecked).commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mApps.size
    }

    fun setData(apps: List<AppInfo>) {
        this.mApps = apps
        notifyDataSetChanged()
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon by lazy { itemView.findViewById<ImageView>(R.id.icon) }
        val name by lazy { itemView.findViewById<TextView>(R.id.name) }
        val packageName by lazy { itemView.findViewById<TextView>(R.id.package_name) }
        val hideSwitch by lazy { itemView.findViewById<Switch>(R.id.switch_view) }
    }
}