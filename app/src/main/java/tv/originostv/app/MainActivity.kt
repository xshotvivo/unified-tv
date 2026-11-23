package tv.originostv.app

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildHome())
    }

    private fun buildHome(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(72), dp(36), dp(72), dp(36))
            setBackgroundColor(Color.rgb(16, 19, 24))
            isFocusable = true
        }

        val brand = TextView(this).apply {
            text = "OriginOSTV"
            setTextColor(Color.WHITE)
            textSize = 38f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
        root.addView(brand)

        val subtitle = TextView(this).apply {
            text = "智能 · 家 · 生态"
            setTextColor(Color.rgb(174, 185, 201))
            textSize = 18f
            setPadding(0, dp(8), 0, dp(28))
        }
        root.addView(subtitle)

        val section = TextView(this).apply {
            text = "家庭影音"
            setTextColor(Color.rgb(226, 232, 240))
            textSize = 22f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setPadding(0, 0, 0, dp(14))
        }
        root.addView(section)

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        actions.addView(featureButton("直播电视", "Live TV 尚未接入"))
        actions.addView(featureButton("媒体中心", "Media Center 尚未接入"))
        root.addView(actions)

        val status = TextView(this).apply {
            text = "V1.0 Foundation · 功能正在逐项交付"
            setTextColor(Color.rgb(125, 139, 158))
            textSize = 14f
            setPadding(0, dp(30), 0, 0)
        }
        root.addView(status)

        return root
    }

    private fun featureButton(title: String, message: String): Button {
        return Button(this).apply {
            text = title
            textSize = 20f
            isAllCaps = false
            minWidth = dp(240)
            minHeight = dp(76)
            setTextColor(Color.WHITE)
            background = focusBackground(false)
            setOnFocusChangeListener { view, hasFocus ->
                view.background = focusBackground(hasFocus)
                view.scaleX = if (hasFocus) 1.035f else 1f
                view.scaleY = if (hasFocus) 1.035f else 1f
            }
            setOnClickListener {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dp(76)
            ).apply {
                marginEnd = dp(20)
            }
        }
    }

    private fun focusBackground(focused: Boolean): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(14).toFloat()
            setColor(if (focused) Color.rgb(49, 66, 91) else Color.rgb(34, 41, 51))
            setStroke(dp(if (focused) 2 else 1), if (focused) {
                Color.rgb(214, 228, 255)
            } else {
                Color.rgb(67, 78, 94)
            })
        }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
