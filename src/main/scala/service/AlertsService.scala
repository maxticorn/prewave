package service

import cats.effect.*
import client.PrewaveApiClient
import domain._

trait AlertsService:
  def findTerms: IO[Map[AlertId, Set[TermId]]]

class AlertsServiceImpl(apiClient: PrewaveApiClient) extends AlertsService:
  private case class Result(termId: TermId, alertId: AlertId)

  def findTerms: IO[Map[AlertId, Set[TermId]]] =
    for {
      terms <- apiClient.getQueryTerms
      alerts <- apiClient.getAlerts
      results = terms.flatMap(findTermInAlerts(alerts, _))
    } yield results.groupBy(_.alertId).view.mapValues(_.map(_.termId)).toMap

  private def findTermInAlerts(alerts: List[Alert], term: QueryTerm) =
    val lowerTerm = term.text.toLowerCase
    val termTokens =
      if (term.keepOrder) List(lowerTerm)
      else lowerTerm.split(' ').toList
    alerts.flatMap(alert =>
      alert.contents.flatMap(findTermInContent(
        term.language,
        termTokens,
        term.id,
        alert.id,
        _)))

  private def findTermInContent(termLanguage: Language,
                                termTokens: List[String],
                                termId: TermId,
                                alertId: AlertId,
                                content: Content): Option[Result] =
    val lowerText = content.text.toLowerCase
    if (content.language == termLanguage &&
      termTokens.forall(term => lowerText.contains(term)))
      Some(Result(termId, alertId))
    else None
