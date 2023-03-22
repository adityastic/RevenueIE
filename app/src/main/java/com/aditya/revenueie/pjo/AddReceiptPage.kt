package com.aditya.revenueie.pjo

import android.webkit.WebView
import com.aditya.revenueie.PJOInterface
import java.text.DecimalFormat
import java.time.LocalDateTime

class AddReceiptPage : PJOInterface() {

    override fun checkPageConditions(view: WebView?, url: String?): Boolean {
        return (url?.equals("https://www.ros.ie/myreceipts-web/ros/receipt/expense-details/add") == true)
    }

    override fun codeToExecute(): String {
        val dateNow = LocalDateTime.now()
        val dmFormat = DecimalFormat("00")
        return "javascript:" +
                getFillValueOfElementByIdScript("dob-day-input", dmFormat.format(dateNow.dayOfMonth)) +
                getFillValueOfElementByIdScript("dob-month-input", dmFormat.format(dateNow.monthValue)) +
                getFillValueOfElementByIdScript("dob-year-input", dateNow.year.toString())
    }

    private fun getFillValueOfElementByIdScript(id: String?, value: String?): String {
        return "document.getElementById('${id}').value = '${value}';"
    }
}