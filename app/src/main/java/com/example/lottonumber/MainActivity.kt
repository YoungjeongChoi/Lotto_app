package com.example.lottonumber

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
//import kotlinx.coroutines.NonCancellable.message

class MainActivity : AppCompatActivity() {

    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add_num) }
    private val autoButton by lazy { findViewById<Button>(R.id.btn_auto) }
    private val numPicker by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6)
        )
    }

    private var finishedRun = false

    private var pickNum = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numPicker.minValue = 1
        numPicker.maxValue = 45

        initAddButton()
        initAutoButton()
        initClearButton()

    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            when {
                finishedRun -> showToast("자동생성 완료, 초기화 후에 시도")
                pickNum.size >= 5 -> showToast("숫자 선택 최대 5개")
                pickNum.contains(numPicker.value) -> showToast("이미 선택된 숫자")
                else -> {
                    val textView = numTextList[pickNum.size]
                    textView.isVisible = true
                    textView.text = numPicker.value.toString()
                    setNumBack(numPicker.value, textView)
                    pickNum.add(numPicker.value)
                }
            }
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNum.clear()
            numTextList.forEach{it.isVisible = false}
            finishedRun = false
            numPicker.value = 1
        }
    }

    private fun initAutoButton() {
        autoButton.setOnClickListener {
            val randList = getRandom()
            randList.forEachIndexed { index, number ->
                val textView = numTextList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)

            }
            finishedRun = true
        }
    }

    private fun getRandom(): List<Int> {
        val numbers = (1..45).filter{it !in pickNum}
        return (pickNum + numbers.shuffled().take(6-pickNum.size)).sorted()
    }

    private fun showToast (message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setNumBack (number: Int, textView: TextView) {
        val background =when(number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
        textView.background = ContextCompat.getDrawable(this, background)
    }
}