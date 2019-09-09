package com.liangzhiyang.youmi.view

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.liangzhiyang.youmi.R

class MsgDialog : Dialog {

    private var tvMsg: TextView ?= null
    private var tvCancle: TextView ?= null
    private var tvConfirm: TextView ?= null

    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context, themeStyle: Int): super(context,themeStyle){
        initView()
    }

    fun initView(){
        setContentView(R.layout.dialog_msg)
        setCanceledOnTouchOutside(false)
        tvMsg = findViewById(R.id.msg_tv)
        tvCancle = findViewById(R.id.cancle_tv)
        tvConfirm = findViewById(R.id.confirm_tv)
    }

    class Builder(var context: Context){
        private var confirmListener : OnConfirmListener ?= null
        private var cancleListener : OnCancleListener ?= null
        private var msg = ""
        private var cancle = ""
        private var confirm = ""

        fun setOnConfirmListener(confirmListener: OnConfirmListener): Builder{
            this.confirmListener = confirmListener
            return this
        }

        fun setOnCancleListener(onCancleListener: OnCancleListener): Builder{
            this.cancleListener = onCancleListener
            return this
        }

        fun setMsg(msg: String): Builder{
            this.msg = msg
            return this
        }

        fun setCancle(cancle: String): Builder{
            this.cancle = cancle
            return this
        }

        fun setConfirm(confirm: String): Builder{
            this.confirm = confirm
            return this
        }

        fun create(): MsgDialog{
            val dialog = MsgDialog(context,R.style.dialog_style)
            dialog.tvMsg?.text = msg
            dialog.tvConfirm?.text = confirm?:"确认"
            dialog.tvCancle?.text = cancle?:"取消"

            if (confirmListener != null)
                dialog.tvConfirm?.setOnClickListener { confirmListener!!.onConfirm(dialog) }

            if (cancleListener != null)
                dialog.tvCancle?.setOnClickListener { cancleListener!!.onCancle(dialog) }
            return dialog
        }


    }

    interface OnCancleListener{
        fun onCancle(dialog: Dialog)
    }

    interface OnConfirmListener{
        fun onConfirm(dialog: Dialog)
    }

}