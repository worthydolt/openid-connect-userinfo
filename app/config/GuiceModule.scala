/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package config

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.typesafe.config.Config
import play.api.{Configuration, Environment}
import uk.gov.hmrc.http.{CorePost, HttpClient, HttpGet}
import connectors._
import services.{LiveUserInfoService, SandboxUserInfoService, UserInfoService}
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient
import uk.gov.hmrc.play.bootstrap.config.ControllerConfig

class GuiceModule(val environment: Environment, val configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[AuthConnector]).to(classOf[AuthConnectorV1])
    bind(classOf[HttpClient]).to(classOf[DefaultHttpClient])
    bind(classOf[UserInfoService]).annotatedWith(Names.named("live")).to(classOf[LiveUserInfoService])
    bind(classOf[UserInfoService]).annotatedWith(Names.named("sandbox")).to(classOf[SandboxUserInfoService])
    bind(classOf[CorePost]).to(classOf[DefaultHttpClient])
    bind(classOf[HttpGet]).to(classOf[DefaultHttpClient])
    bind(classOf[ControllerConfig]).toInstance {
      new ControllerConfig {
        def controllerConfigs: Config = configuration.underlying.getConfig("controllers")
      }
    }
  }
}
