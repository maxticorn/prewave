import cats.effect.*
import service.*
import cats.syntax.traverse.*
import config.PrewaveApiConfig
import client.PrewaveApiClient
import pureconfig.*
import cats.syntax.either.*

object Main extends IOApp:
  private def buildService: Resource[IO, AlertsService] =
    for {
      config <- Resource.eval(IO(ConfigSource.default.loadOrThrow[PrewaveApiConfig]))
      client <- PrewaveApiClient.mk(config)
      service = new AlertsServiceImpl(client)
    } yield service

  def run(args: List[String]): IO[ExitCode] =
    buildService.use { service =>
      for {
        results <- service.findTerms
        _ <- results.toList.traverse(IO.println)
      } yield ()
    }.as(ExitCode.Success)

