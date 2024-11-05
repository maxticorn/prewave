package client

import cats.effect.*
import config.PrewaveApiConfig
import domain.*
import sttp.client3.*
import sttp.client3.httpclient.cats.HttpClientCatsBackend
import sttp.client3.circe.*

trait PrewaveApiClient:
  def getQueryTerms: IO[Set[QueryTerm]]

  def getAlerts: IO[List[Alert]]

class PrewaveApiClientImpl(backend: SttpBackend[IO, Any],
                           config: PrewaveApiConfig)
  extends PrewaveApiClient:
  def getQueryTerms: IO[Set[QueryTerm]] =
    basicRequest
      .get(
        uri"${config.host}/${config.queryTermEndpoint}".addParam("key", config.apiKey)
      )
      .response(asJson[Set[QueryTerm]])
      .send(backend)
      .flatMap { response =>
        IO.fromEither(response.body)
      }

  def getAlerts: IO[List[Alert]] =
    basicRequest
      .get(
        uri"${config.host}/${config.alertsEndpoint}".addParam("key", config.apiKey)
      )
      .response(asJson[List[Alert]])
      .send(backend)
      .flatMap { response =>
        IO.fromEither(response.body)
      }

object PrewaveApiClient:
  def mk(config: PrewaveApiConfig): Resource[IO, PrewaveApiClient] =
    for {
      client <- HttpClientCatsBackend.resource[IO]()
    } yield new PrewaveApiClientImpl(client, config)
