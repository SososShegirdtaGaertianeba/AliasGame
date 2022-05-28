package com.example.alias.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.alias.databinding.ArrowBtnBinding

class ArrowBtn @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ArrowBtnBinding.inflate(LayoutInflater.from(context), this, true)

    fun setText(text: String) {
        binding.textView.text = text
    }

    fun setBtnColor(id: Int) =
        binding.btn.setBackgroundResource(id)

    fun setDrawable(id: Int) =
        binding.drawableView.setBackgroundResource(id)

    override fun setOnClickListener(l: OnClickListener?) {
        binding.btn.setOnClickListener(l)
    }

    fun setOnClickListener(f: () -> Unit) =
        setOnClickListener(OnClickListener { f() })

}