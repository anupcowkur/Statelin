package com.anupcowkur.statelin

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec

class StateTest : StringSpec() {

    init {

        "Should add sub states" {
            val stateA = State("A")
            val subStateA = State("subStateA")
            val subStateB = State("subStateB")

            stateA.subStates.add(subStateA)
            stateA.subStates.add(subStateB)

            stateA.subStates.size shouldBe 2
            stateA.subStates[0] shouldBe subStateA
            stateA.subStates[1] shouldBe subStateB
        }

    }
}