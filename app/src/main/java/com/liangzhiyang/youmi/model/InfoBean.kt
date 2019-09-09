package com.liangzhiyang.youmi.model

import java.io.Serializable

data class InfoBean(val accountIntroduction: String,
                    val accountName: String,
                    val accountSlogan: String,
                    val accountUrl: String,
                    val customServiceTelephone: String,
                    val customServiceWeChat: String,
                    val logoUrl: String,
                    val version: String) : Serializable