package com.cdsap.talaiot.mock

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState

object TaskMeasurementAggregatedMock {
    fun taskMeasurementAggregated(): TaskMeasurementAggregated {
        return TaskMeasurementAggregated(
            emptyMap(),
            listOf(
                TaskLength(
                    1,
                    "assemble",
                    "assemble",
                    TaskMessageState.EXECUTED,
                    true,
                    "app",
                    emptyList()
                ),
                TaskLength(
                    2,
                    "compileKotlin",
                    "compileKotlin",
                    TaskMessageState.EXECUTED,
                    false,
                    "app",
                    listOf("assemble")

                )
            )
        )
    }

    fun taskMeasurementAggregatedWrongMetricsFormat(): TaskMeasurementAggregated {
        return TaskMeasurementAggregated(
            mapOf(
                "me=tric1" to "va====lue1",
                "metric2" to "val,,   , ue2"
            ), listOf(
                TaskLength(
                    1, "clean", ":clean", TaskMessageState.EXECUTED,
                    true, "app", emptyList()
                )
            )
        )
    }


}