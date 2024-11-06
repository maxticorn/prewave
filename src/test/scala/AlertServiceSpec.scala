import cats.effect.*
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AsyncFreeSpec
import client.*
import service.*
import domain.*

import java.time.LocalDateTime

// for some reason, only class works here; object will not be detected by sbt
class AlertServiceSpec extends AsyncFreeSpec with AsyncIOSpec with Matchers {
  "AlertService" - {
    "return empty if everything is empty" in {
      val emptyApiClient = new PrewaveApiClient {
        def getQueryTerms: IO[Set[QueryTerm]] = IO.pure(Set.empty)

        def getAlerts: IO[List[Alert]] = IO.pure(List.empty)
      }
      val emptyAlertsService = new AlertsServiceImpl(emptyApiClient)
      emptyAlertsService.findTerms.asserting(_ shouldEqual Map.empty)
    }

    "return empty if client throws" in {
      val emptyApiClient = new PrewaveApiClient {
        def getQueryTerms: IO[Set[QueryTerm]] = IO.raiseError(new Throwable())

        def getAlerts: IO[List[Alert]] = IO.raiseError(new Throwable())
      }
      val emptyAlertsService = new AlertsServiceImpl(emptyApiClient)
      emptyAlertsService.findTerms.asserting(_ shouldEqual Map.empty)
    }

    "find ordered terms" in {
      val emptyApiClient = new PrewaveApiClient {
        def getQueryTerms: IO[Set[QueryTerm]] =
          IO.pure(Set(
            QueryTerm(0, "test term", Language.English, true),
            QueryTerm(1, "ordered test", Language.English, true),
            QueryTerm(2, "not found", Language.English, true)
          ))

        def getAlerts: IO[List[Alert]] =
          IO.pure(List(
            Alert("a", List(Content("ordered test term search", ContentType.text, Language.English)), LocalDateTime.now(), InputType.link)
          ))
      }
      val emptyAlertsService = new AlertsServiceImpl(emptyApiClient)
      emptyAlertsService.findTerms.asserting(_ shouldEqual Map("a" -> Set(0, 1)))
    }

    "find unordered terms" in {
      val emptyApiClient = new PrewaveApiClient {
        def getQueryTerms: IO[Set[QueryTerm]] =
          IO.pure(Set(
            QueryTerm(0, "unordered test", Language.English, false),
            QueryTerm(1, "unordered test", Language.English, true)
          ))

        def getAlerts: IO[List[Alert]] =
          IO.pure(List(
            Alert("a", List(Content("unordered term test search", ContentType.text, Language.English)), LocalDateTime.now(), InputType.link)
          ))
      }
      val emptyAlertsService = new AlertsServiceImpl(emptyApiClient)
      emptyAlertsService.findTerms.asserting(_ shouldEqual Map("a" -> Set(0)))
    }
  }
}
