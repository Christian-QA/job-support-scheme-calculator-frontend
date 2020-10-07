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

package uk.gov.hmrc.jobsupportschemecalculatorfrontend.controllers.actions

import com.google.inject.{Inject, Singleton}
import play.api.i18n.MessagesApi
import play.api.mvc._
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.config.ErrorHandler
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.models.{SessionData, UserType}
import uk.gov.hmrc.jobsupportschemecalculatorfrontend.repos.SessionStore

import scala.concurrent.ExecutionContext

final case class RequestWithSessionData[A](
  sessionData: Option[SessionData],
  request: MessagesRequest[A]
) extends WrappedRequest[A](request)
    with PreferredMessagesProvider {
  override def messagesApi: MessagesApi =
    request.messagesApi
  val userType: Option[UserType]        = sessionData.flatMap(_.userType)
}

@Singleton
class SessionDataAction @Inject() (
  val sessionStore: SessionStore,
  val errorHandler: ErrorHandler
)(implicit
  val executionContext: ExecutionContext
) extends SessionDataActionBase[
      MessagesRequest,
      RequestWithSessionData
    ] {

  def sessionDataAction[A](
    sessionData: Option[SessionData],
    request: MessagesRequest[A]
  ): RequestWithSessionData[A] =
    RequestWithSessionData(sessionData, request)

}
