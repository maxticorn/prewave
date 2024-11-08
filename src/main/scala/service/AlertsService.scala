package service

import cats.effect.*
import client.PrewaveApiClient
import domain._

trait AlertsService:
  def findTerms: IO[Map[AlertId, Set[TermId]]]

class AlertsServiceImpl(apiClient: PrewaveApiClient) extends AlertsService:
  private case class Result(termId: TermId, alertId: AlertId)

  def findTerms: IO[Map[AlertId, Set[TermId]]] =
    (for {
      terms <- apiClient.getQueryTerms.onError(th => IO.println(s"error while getting query terms: ${th.getMessage}"))
      alerts <- apiClient.getAlerts.onError(th => IO.println(s"error while getting alerts: ${th.getMessage}"))
      results = terms.flatMap(findTermInAlerts(alerts, _))
    } yield results.groupBy(_.alertId).view.mapValues(_.map(_.termId)).toMap)
      .handleError(_ => Map.empty)
      // decided to just return empty value in case of failure and log where the error happened
      // since I think it is enough for such a program.
      // would consider logs/traces/metrics for observability
      // and specific responses for different cases
      // if it was a http server.


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
