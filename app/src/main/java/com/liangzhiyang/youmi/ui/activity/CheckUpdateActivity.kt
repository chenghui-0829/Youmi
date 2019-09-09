package com.liangzhiyang.youmi.ui.activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import com.liangzhiyang.youmi.R
import com.liangzhiyang.youmi.api.ApiManager
import com.liangzhiyang.youmi.api.rxjava.RxJavaHelper
import com.liangzhiyang.youmi.base.BaseActivity
import com.liangzhiyang.youmi.utils.AppUtils
import com.liangzhiyang.youmi.view.MsgDialog
import kotlinx.android.synthetic.main.layout_base_toolbar.*

/**
 * Created by zy
 * Date: 2019/2/26
 * desc:
 */

class CheckUpdateActivity: BaseActivity() {
    override fun layoutId(): Int {
        return R.layout.activity_check_update

    }

    override fun initData() {
    }

    override fun initView() {
        toolbar_title.text = "检查更新"
    }

    override fun loadData() {
        val curVersion = AppUtils.getVersionName(this)
        showProgressDialog("检查更新...")
        val disposable = ApiManager.service.checkUpdate("1",curVersion)
                .compose(RxJavaHelper.observableToMain())
                .subscribe({
                    dismissProgressDialog()
                    if (it.isSuccess()){
                        var msg = ""
                        var confirm = ""
                        var update = false
                        if (it.data.versionNum.isEmpty() ||
                                curVersion == it.data.versionNum){
                            msg = "当前已是最新版本"
                            confirm = "确认"
                            update = false
                        }else{
                            msg = "有新的版本\n，是否前往更新?"
                            confirm = "更新"
                            update = true
                        }
                        MsgDialog.Builder(this)
                                .setMsg(msg)
                                .setConfirm(confirm)
                                .setCancle("取消")
                                .setOnCancleListener(object : MsgDialog.OnCancleListener{
                                    override fun onCancle(dialog: Dialog) {
                                        dialog.dismiss()
                                    }
                                })
                                .setOnConfirmListener(object : MsgDialog.OnConfirmListener{
                                    override fun onConfirm(dialog: Dialog) {
                                        dialog.dismiss()
                                        if (update){
                                            val intent = Intent()
                                            intent.setAction("android.intent.action.VIEW")
                                            val uri = Uri.parse(it.data.downloadUrl)
                                            intent.setData(uri)
                                            startActivity(intent)
                                        }else{
                                            finish()
                                        }
                                    }
                                })
                                .create()
                                .show()
                    }
                },{
                    dismissProgressDialog()
                    showMessage(getString(R.string.net_error))
                })
        addSubscription(disposable)
    }
}