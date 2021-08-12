package it.grizzi.motionstackview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stack_view_activity)
        val stackView = findViewById<ExpandableStackView>(R.id.expandable_stack)
        stackView.setAdapter(
            ExpandableStackViewAdapter((1..10).toList().map { it.toString() }, this)
        )
        findViewById<AppCompatButton>(R.id.button).setOnClickListener {
            if (stackView.currentState == stackView.startState) {
                stackView.transitionToEnd()
            } else {
                stackView.transitionToStart()
            }
        }
    }
}
