package com.cdsap.talaiot.plugin
import com.cdsap.talaiot.base.Publisher
import com.cdsap.talaiot.base.extension.TalaiotExtension
import com.cdsap.talaiot.base.logger.LogTrackerImpl
import com.cdsap.talaiot.base.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.base.JsonPublisher
import com.cdsap.talaiot.publisher.base.OutputPublisher
import com.cdsap.talaiot.publisher.base.TimelinePublisher
import com.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import com.cdsap.talaiot.publisher.graph.GraphPublisherFactoryImpl
import com.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher
import com.cdsap.talaiot.publisher.hybrid.HybridPublisher
import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import com.cdsap.talaiot.publisher.pushgateway.PushGatewayFormatter
import com.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import com.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project
import java.util.concurrent.Executors

class TalaiotConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotFatExtensionPlugin
        val logger = LogTrackerImpl(talaiotExtension.logger)
        val executor = Executors.newSingleThreadExecutor()
        val heavyExecutor = Executors.newSingleThreadExecutor()
        talaiotExtension.publishers?.apply {
            outputPublisher?.apply {
                publishers.add(OutputPublisher(this, logger))
            }

            influxDbPublisher?.apply {
                publishers.add(
                    InfluxDbPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }
            taskDependencyGraphPublisher?.apply {
                publishers.add(
                    TaskDependencyGraphPublisher(
                        this,
                        logger,
                        heavyExecutor,
                        GraphPublisherFactoryImpl()
                    )
                )
            }
            pushGatewayPublisher?.apply {
                publishers.add(
                    PushGatewayPublisher(
                        this,
                        logger,
                        SimpleRequest(logger),
                        executor,
                        PushGatewayFormatter()
                    )
                )
            }
            if (jsonPublisher) {
                publishers.add(JsonPublisher(project.gradle))
            }

            if (timelinePublisher) {
                publishers.add(TimelinePublisher(project.gradle))
            }

            elasticSearchPublisher?.apply {
                publishers.add(
                    ElasticSearchPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            hybridPublisher?.apply {
                publishers.add(
                    HybridPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            rethinkDbPublisher?.apply {
                publishers.add(
                    RethinkDbPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            publishers.addAll(customPublishers)
        }
        return publishers
    }
}