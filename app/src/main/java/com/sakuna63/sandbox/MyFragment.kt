package com.sakuna63.sandbox

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView")
        return container?.context?.let {
            TextView(it).also { text ->
                text.text = arguments.getString("key")
                text.textSize = when (text.text) {
                    "A" -> 20f
                    "B" -> 40f
                    "C" -> 60f
                    else -> 80f
                }
                text.gravity = Gravity.CENTER

                val lp = text.layoutParams ?: ViewGroup.LayoutParams(0, 0)
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT
                text.layoutParams = lp
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        log("onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        log("onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onResume() {
        super.onResume()
        log("onResume")
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
    }

    override fun onStop() {
        super.onStop()
        log("onStop")
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    private fun log(msg: String) {
        Log.d("MyFragment", "${arguments.getString("key")}: $msg")
    }
}

