package murait.the.android.mania

import android.content.Context
import android.content.SharedPreferences

class PrefManager internal constructor(mContext: Context) {
    var editor: SharedPreferences.Editor? = null
    fun addVar() {
        if (pref != null) {
            var tVar = pref!!.getInt("XD", 0)
            editor = pref!!.edit()
            editor?.putInt("XD",++tVar)
            editor?.commit()
        }
    }

    val tVar: Int
        get() {
            var tVar = 0
            if (pref != null) {
                tVar = pref!!.getInt("XD", 0)
            }
            return tVar
        }

    companion object {
        var pref: SharedPreferences? = null
        const val ADS_SHOW_TIME = 10
    }

    init {
        if (pref == null) {
            pref = mContext.getSharedPreferences("WebView", Context.MODE_PRIVATE)
        }
    }
}