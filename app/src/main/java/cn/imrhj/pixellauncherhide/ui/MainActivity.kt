package cn.imrhj.pixellauncherhide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cn.imrhj.pixellauncherhide.R
import cn.imrhj.pixellauncherhide.model.AppInfo
import cn.imrhj.pixellauncherhide.utils.Common
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText


@SuppressLint("WorldReadableFiles")
class MainActivity : AppCompatActivity() {
    private val mAppInfos by lazy { mutableListOf<AppInfo>() }
    private val mPreferences by lazy { getSharedPreferences("hideapp", Context.MODE_WORLD_READABLE) }
    private val mAdapter by lazy { AppAdapter(mAppInfos, mPreferences) }
    private var mFilterInfos = mutableListOf<AppInfo>()

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initApp()
        recycler.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.background_divider, null)!!)
        recycler.addItemDecoration(divider)
        recycler.setHasFixedSize(true)
        recycler.adapter = mAdapter

        float_button.setOnClickListener {
            mPreferences.edit().commit()
            Toast.makeText(this, "请点击强行停止重启桌面", Toast.LENGTH_LONG).show()
            showAppSetting()
        }
    }

    private fun initApp() {
        val startIntent = Intent(Intent.ACTION_MAIN)
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        packageManager.queryIntentActivities(startIntent, 0).iterator().forEach {
            mAppInfos.add(AppInfo(it.loadLabel(packageManager), it.activityInfo.packageName,
                    it.loadIcon(packageManager), mPreferences.getBoolean(it.activityInfo.packageName, false)))
        }
        mAppInfos.sortBy { it.appName[0] }
    }

    private fun showAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${Common.LAUNCHER_PACKAGE_NAME}")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            applicationContext.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "打开设置界面失败，请手动重启Pixel Launcher", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu?.findItem(R.id.search_view)?.actionView as? SearchView

        val searchEditText = searchView?.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(Thread.currentThread().name, "class = MainActivity rhjlog onQueryTextSubmit: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(Thread.currentThread().name, "class = MainActivity rhjlog onQueryTextChange: $newText")
                changeDataSource(newText)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item?.itemId == R.id.setting) {
//            Toast.makeText(this, "点我干嘛？", Toast.LENGTH_SHORT).show()
//        }
        return false
    }


    private fun changeDataSource(newText: String?) {
        if (newText != null && newText.isNotEmpty()) {
            mFilterInfos.clear()
            mFilterInfos.addAll(mAppInfos.filter {
                it.appName.contains(newText, true)
                        .or(it.packageName.contains(newText, true))
            })
            mAdapter.setData(mFilterInfos)
        } else {
            if (mFilterInfos.size != mAppInfos.size) {
                mFilterInfos.clear()
                mFilterInfos.addAll(mAppInfos)
                mAdapter.setData(mFilterInfos)
            }
        }
    }

}
