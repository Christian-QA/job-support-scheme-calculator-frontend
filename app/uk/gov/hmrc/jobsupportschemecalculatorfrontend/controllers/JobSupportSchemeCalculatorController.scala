/*
 * Copyright 2020 HM Revenue & Customs
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

package uk.gov.hmrc.jobsupportschemecalculatorfrontend.controllers

import com.google.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.config.ViewConfig
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.controllers.actions.{SessionDataAction, WithSessionDataAction}
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.util.Logging
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.views
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

class JobSupportSchemeCalculatorController @Inject() (
  cc: MessagesControllerComponents,
  testPage: views.html.test,
  val sessionDataAction: SessionDataAction
)(implicit viewConfig: ViewConfig)
    extends FrontendController(cc)
    with WithSessionDataAction
    with Logging
    with SessionUpdates {

  def start(): Action[AnyContent] = withSessionData { implicit request =>
    val _ = s"${viewConfig.abilityNetUrl}"
    Ok(testPage())
  }
}
