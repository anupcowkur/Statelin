package com.anupcowkur.statelin

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec

class MachineTest : StringSpec() {

    init {

        "Should add trigger handlers" {
            val stateA = State("A")
            val stateB = State("B")

            val triggerX = Trigger("TriggerX")
            val triggerY = Trigger("TriggerY")

            val machine = Machine(stateA)

            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, { _, _ -> }))
            machine.addTriggerHandler(TriggerHandler(stateB, triggerY, { _, _ -> }))
            machine.eventHandlers.size shouldBe 2
        }

        "Should throw exception on duplicate trigger handlers" {
            val stateA = State("A")

            val triggerX = Trigger("TriggerX")

            val machine = Machine(stateA)

            // Add valid trigger handler
            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, { _, _ -> }))

            val exception = shouldThrow<IllegalArgumentException> {
                // Add same trigger handler again
                machine.addTriggerHandler(TriggerHandler(stateA, triggerX, { _, _ -> }))
            }

            exception.message shouldBe "TriggerHandler $stateA -> $triggerX is already added"

            val subStateA = State("subStateA")
            stateA.subStates.add(subStateA)

            // Add valid sub-state trigger handler
            machine.addTriggerHandler(TriggerHandler(subStateA, triggerX, { _, _ -> }))

            val subStateException = shouldThrow<IllegalArgumentException> {
                // Add same sub-state trigger handler again
                machine.addTriggerHandler(TriggerHandler(subStateA, triggerX, { _, _ -> }))
            }

            subStateException.message shouldBe "TriggerHandler $subStateA -> $triggerX is already added"

            // TODO: Implement concept of parent so that this exception message can say something like parentState.childState -> triggerX is already added
        }

        "Should invoke trigger handler on trigger" {
            val stateA = State("A")

            val triggerX = Trigger("TriggerX")

            val machine = Machine(stateA)

            var invocationCounter = 0
            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, { _, _ -> invocationCounter++ }))

            machine.trigger(triggerX)

            invocationCounter shouldBe 1
        }

        "Should invoke onEnter on state entry" {
            var invocationCounter = 0
            val stateA = State("A", onEnter = { invocationCounter++ })
            val stateB = State("B", onEnter = { invocationCounter++ })

            // Initial state is A
            val machine = Machine(stateA)

            // onEnter should be called
            invocationCounter shouldBe 1

            // Set same state
            machine.state = stateA

            // onEnter should not be called
            invocationCounter shouldBe 1

            // Set different state
            machine.state = stateB

            // onEnter should be called
            invocationCounter shouldBe 2
        }

        "Should invoke onEnter on sub state entry" {
            var invocationCounter = 0
            val stateA = State("A")
            val subStateA = State("subStateA", onEnter = { invocationCounter++ })

            // Initial state is A
            val machine = Machine(stateA)

            // Set same state
            machine.state = stateA

            // onEnter should not be called
            invocationCounter shouldBe 0

            // Set sub state
            machine.state = subStateA

            // onEnter should be called
            invocationCounter shouldBe 1
        }

        "Should invoke onExit on state exit" {
            var invocationCounter = 0
            val stateA = State("A", onExit = { invocationCounter++ })
            val stateB = State("B")

            // Initial state is A
            val machine = Machine(stateA)

            // onExit should not be called
            invocationCounter shouldBe 0

            // Set same state
            machine.state = stateA

            // onExit should not be called
            invocationCounter shouldBe 0

            // Set different state
            machine.state = stateB

            // onExit should be called
            invocationCounter shouldBe 1
        }

        "Should invoke onExit on sub state exit" {
            var invocationCounter = 0
            val stateA = State("A")
            val subStateA = State("subStateA", onExit = { invocationCounter++ })

            stateA.subStates.add(subStateA)

            // Initial state is A
            val machine = Machine(stateA)

            // Set sub state
            machine.state = subStateA

            // Go back to stateA
            machine.state = stateA

            // onExit should be called
            invocationCounter shouldBe 1
        }


        "Should set new state" {
            var invocationCounter = 0
            val stateA = State("A")
            val stateB = State("B")

            // Initial state is A
            val machine = Machine(stateA)

            machine.state shouldBe stateA

            // Set same state
            machine.state = stateA

            // onExit should not be called
            machine.state shouldBe stateA

            // Set different state
            machine.state = stateB

            // onExit should be called
            machine.state shouldBe stateB
        }

    }
}