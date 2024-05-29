/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.sample;

import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.spanner.SpannerOptions;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfiguration {

  @Value("${open_telemetry.project}")
  private String project;

  @Bean
  public OpenTelemetry openTelemetry() throws IOException {
    // Enable OpenTelemetry tracing in Spanner.
    SpannerOptions.enableOpenTelemetryTraces();

    TraceConfiguration.Builder traceConfigurationBuilder = TraceConfiguration.builder();
    TraceConfiguration traceConfiguration = traceConfigurationBuilder.setProjectId(project).build();
    SpanExporter traceExporter = TraceExporter.createWithConfiguration(traceConfiguration);

    // Create an OpenTelemetry object and register it as the global OpenTelemetry object. This
    // will automatically be picked up by the Spanner libraries and used for tracing.
    return OpenTelemetrySdk.builder()
        .setTracerProvider(
            SdkTracerProvider.builder()
                // Set sampling to 'AlwaysOn' in this example. In production, you want to reduce
                // this to a smaller fraction to limit the number of traces that are being
                // collected.
                .setSampler(Sampler.alwaysOn())
                .setResource(
                    Resource.builder()
                        .put(
                            "service.name",
                            "spanner-jdbc-spring-data-sample-"
                                + ThreadLocalRandom.current().nextInt())
                        .build())
                .addSpanProcessor(BatchSpanProcessor.builder(traceExporter).build())
                .build())
        .buildAndRegisterGlobal();
  }

  @Bean
  public Tracer tracer(OpenTelemetry openTelemetry) {
    return openTelemetry.getTracer("com.google.cloud.spanner.jdbc.sample.spring-data-jdbc");
  }
}
