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

            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, {}))
            machine.addTriggerHandler(TriggerHandler(stateB, triggerY, {}))
            
            machine.eventHandlers.size shouldBe 2
        }

        "Should throw exception on duplicate trigger handlers" {
            val stateA = State("A", {}, {})
            val stateADuplicate = State("A", {}, {})

            val triggerX = Trigger("TriggerX")

            val machine = Machine(stateA)

            // Add valid trigger handler
            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, {}))

            val exception = shouldThrow<IllegalArgumentException> {
                // Add same trigger handler again
                machine.addTriggerHandler(TriggerHandler(stateADuplicate, triggerX, {}))
            }

            exception.message shouldBe "TriggerHandler $stateA -> $triggerX is already added"
        }

        "Should invoke trigger handler on trigger" {
            val stateA = State("A")

            val triggerX = Trigger("TriggerX")

            val machine = Machine(stateA)

            var invocationCounter = 0
            machine.addTriggerHandler(TriggerHandler(stateA, triggerX, { invocationCounter++ }))

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

    }
}