package com.liangzhiyang.youmi.model

import java.io.Serializable

/**
 * Created by zy
 * Date: 2019/2/21
 * desc:
 */

data class ProductDetailBean(val endLoanDuration: String,
                             val endLoanDurationUnit: String,
                             val forwardUrl: String,
                             val interestRate: String,
                             val interestRateUnit: String,
                             val maxLoanLimit: String,
                             val maxLoanLimitUnit: String,
                             val minLoanLimit: String,
                             val minLoanLimitUnit: String,
                             val productFeature: String,
                             val productId: String,
                             val productName: String,
                             val productimg: String,
                             val slogan: String,
                             val startLoanDuration: String,
                             val startLoanDurationUnit: String,
                             val agencyMobile: String,
                             val applyConditions: ArrayList<String>,
                             val applyCount: String,
                             val applyMaterials: ArrayList<String>) : Serializable