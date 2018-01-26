package com.anupcowkur.statelin

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec

class MachineTest : StringSpec() {

    init {

        "Should add transitions" {
            val state1 = State1("John")
            val state2 = State2(36.0)
            val state3 = State3("John", "Smith")
            val state4 = State4(24f)

            val action1 = Action1("John")
            val action2 = Action2(36.0)
            val action3 = Action3("John", "Smith")
            val action4 = Action4(24f)

            val machine = Machine(State1("John"))

            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))
            machine.addTransition(Transition(state2, action3, state3, { _, _, _ -> }))
            machine.addTransition(Transition(state3, action4, state4, { _, _, _ -> }))
            machine.addTransition(Transition(state4, action1, state1, { _, _, _ -> }))
            machine.transitions.size shouldBe 4
        }


        "Should throw exception on duplicate transitions" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(State1("John"))

            // valid transition
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            val exception = shouldThrow<IllegalArgumentException> {
                // same transition added again
                machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))
            }

            exception.message shouldBe "Transition $state1 -> $action2 -> $state2 is already added"
        }


        "Should invoke transition callback on trigger" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(State1("John"))

            var invocationCounter = 0
            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> invocationCounter++ }))

            machine.trigger(action2)

            invocationCounter shouldBe 1
        }

        "Should set new state on trigger" {
            val state1 = State1("John")
            val state2 = State2(36.0)

            val action2 = Action2(36.0)

            val machine = Machine(State1("John"))

            machine.addTransition(Transition(state1, action2, state2, { _, _, _ -> }))

            machine.trigger(action2)

            machine.state shouldBe state2
        }

    }
}