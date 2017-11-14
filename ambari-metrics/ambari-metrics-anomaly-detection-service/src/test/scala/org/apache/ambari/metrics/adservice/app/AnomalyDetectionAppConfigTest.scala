/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ambari.metrics.adservice.app

import java.io.File

import javax.validation.Validator

import org.scalatest.FunSuite

import com.fasterxml.jackson.databind.ObjectMapper

import io.dropwizard.configuration.YamlConfigurationFactory
import io.dropwizard.jackson.Jackson
import io.dropwizard.jersey.validation.Validators

class AnomalyDetectionAppConfigTest extends FunSuite {

  test("testConfiguration") {

    val objectMapper: ObjectMapper = Jackson.newObjectMapper()
    val validator: Validator = Validators.newValidator
    val factory: YamlConfigurationFactory[AnomalyDetectionAppConfig] =
      new YamlConfigurationFactory[AnomalyDetectionAppConfig](classOf[AnomalyDetectionAppConfig], validator, objectMapper, "")

    val classLoader = getClass.getClassLoader
    val file = new File(classLoader.getResource("config.yml").getFile)
    val config = factory.build(file)

    assert(config.isInstanceOf[AnomalyDetectionAppConfig])

    assert(config.getMetricDefinitionServiceConfiguration.getInputDefinitionDirectory ==
      "/etc/ambari-metrics-anomaly-detection/conf/definitionDirectory")

    assert(config.getMetricCollectorConfiguration.getHosts == "host1,host2")

    assert(config.getMetricCollectorConfiguration.getPort == "6188")

    assert(config.getAdServiceConfiguration.getAnomalyDataTtl == 604800)

    assert(config.getMetricDefinitionDBConfiguration.getDbDirPath == "/var/lib/ambari-metrics-anomaly-detection/")

    assert(config.getMetricDefinitionDBConfiguration.getVerifyChecksums)

    assert(!config.getMetricDefinitionDBConfiguration.getPerformParanoidChecks)

  }

}
