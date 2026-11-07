package com.tungsten.fcl

import android.content.Context
import com.tungsten.fcllibrary.component.theme.ThemePreference
import com.tungsten.fcllibrary.component.theme.themeDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * [mio] fcl-澪 Application：在主题系统首次初始化前播种澪的默认粉主题。
 * 仅当 DataStore 从未写入过（等于框架默认值）时写入，用户后续改主题不受影响。
 * 其余行为与 FCLApp 完全一致。
 */
class MioApp : FCLApp() {

    override fun onCreate() {
        super.onCreate()
        seedMioTheme(this)
    }

    private fun seedMioTheme(context: Context) {
        runBlocking(Dispatchers.IO) {
            runCatching {
                val pref = context.themeDataStore.data.first()
                if (pref == ThemePreference()) {
                    // 从未写入过：播种澪默认粉主题（colorPrimary #F97297 / colorPrimaryDark #FF5180）
                    context.themeDataStore.updateData {
                        it.copy(color = 0xFFF97297.toInt(), colorDark = 0xFFFF5180.toInt())
                    }
                }
            }
        }
    }
}
