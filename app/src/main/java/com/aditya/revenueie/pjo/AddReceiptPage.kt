package com.aditya.revenueie.pjo

import android.content.Context
import android.content.SharedPreferences
import android.webkit.WebView
import com.aditya.revenueie.PJOInterface
import com.aditya.revenueie.R
import java.text.DecimalFormat
import java.time.LocalDateTime

class AddReceiptPage(
    private val context: Context,
    private val prefs: SharedPreferences,
) : PJOInterface() {

    override fun checkPageConditions(view: WebView?, url: String?): Boolean {
        return (url?.equals("https://www.ros.ie/myreceipts-web/ros/receipt/expense-details/add") == true)
    }

    override fun codeToExecute(): String {
        val shouldAutoSelect =
            prefs.getBoolean(context.getString(R.string.mode_autoselect_health_key), false)

        val dateNow = LocalDateTime.now()
        val dmFormat = DecimalFormat("00")
        return "javascript:" +
                getFillValueOfElementByIdScript(
                    "dob-day-input",
                    dmFormat.format(dateNow.dayOfMonth)
                ) +
                getFillValueOfElementByIdScript(
                    "dob-month-input",
                    dmFormat.format(dateNow.monthValue)
                ) +
                getFillValueOfElementByIdScript("dob-year-input", dateNow.year.toString()) +
                """
                    var dispatchEvent = function(element, eventName) {
                    if ('createEvent' in document) {
                        var event = document.createEvent('HTMLEvents');
                        event.initEvent(eventName, false, true);
                        element.dispatchEvent(event);
                    } else {
                        element.fireEvent(eventName);
                    }
                    };
                    
                    dispatchEvent(document.getElementById('dob-year-input'), 'change');
                    """.trimIndent() +
                if (shouldAutoSelect)
                    """setTimeout(() => {
                        document.getElementById('categorySelect').value=0;
                        categorySelectChange();   

                        setTimeout(() => {
                            document.getElementById('subCategorySelect').value=0;   
                            subCategorySelectChange();
                        }, 300);
                    }, 300);
""".trimIndent()
                else ""
    }

    private fun getFillValueOfElementByIdScript(id: String?, value: String?): String {
        return "document.getElementById('${id}').value = '${value}';"
    }
}