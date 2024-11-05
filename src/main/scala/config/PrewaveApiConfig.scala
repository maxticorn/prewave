package config

import pureconfig._
import pureconfig.generic.derivation.default._

case class PrewaveApiConfig(host: String,
                            apiKey: String,
                            queryTermEndpoint: String,
                            alertsEndpoint: String)
  derives ConfigReader
