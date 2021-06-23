package ncmbdataquick.mbaas.com.nifcloud.datastorequickstart

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.util.Log
import com.nifcloud.mbaas.core.NCMB
import com.nifcloud.mbaas.core.NCMBException
import com.nifcloud.mbaas.core.NCMBObject
import com.nifcloud.mbaas.core.NCMBCallback
import android.widget.Toast
import android.content.Context
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this, "3c99589bee9dda8184febdf64cdcfe65f84faf3ec5a2b158e477cea807299b30",
                "4f77045784c3d667ccf2557cb31e507a1488e37bf0f88ba042610271f4e3f981")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun doStartDemo(view: View) {
        val obj = NCMBObject("TestClass")

        try {
            obj.put("message", "Hello, NCMB!")
            val callback = NCMBCallback{ e, obj ->
                if (e != null) {
                    //保存失敗
                    Log.v("NCMB","NCMB Error:" + e.message)
                    backgroundThreadShortToast(NCMB.getCurrentContext(), "NCMB Error:" + e.message);
                } else {
                    //保存成功
                    if(obj is NCMBObject) {
                        Log.v("NCMB", "Save successfull! with ID:" + obj.getObjectId())
                        backgroundThreadShortToast(NCMB.getCurrentContext(), "Save successfull! with ID:" + obj.getObjectId());
                    }
                }
            }
            obj.saveInBackground(callback)
        } catch (e: NCMBException) {
            e.printStackTrace()
        }

    }
}

// This method is to process UI on different thread to be called on callback for NCMB connection using okhttp
fun backgroundThreadShortToast(context: Context?, msg: String?) {
    if (context != null && msg != null) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() //UI更新
            }
        })
    }
}
