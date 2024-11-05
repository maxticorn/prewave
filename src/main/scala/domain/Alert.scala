package domain

import io.circe.Decoder

import java.time.LocalDateTime
import io.circe.generic.semiauto.*

import java.time.format.DateTimeFormatter

case class Content(text: Text,
                   `type`: ContentType,
                   language: Language)

object Content:
  given Decoder[Content] = deriveDecoder

case class Alert(id: AlertId,
                 contents: List[Content],
                 date: LocalDateTime,
                 inputType: InputType)

object Alert:
  given Decoder[LocalDateTime] = Decoder.decodeLocalDateTimeWithFormatter(
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
  )

  given Decoder[Alert] = deriveDecoder
