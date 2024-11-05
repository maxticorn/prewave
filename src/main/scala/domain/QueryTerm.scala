package domain

import io.circe.Decoder

import io.circe.generic.semiauto.*

case class QueryTerm(id: TermId,
                     text: Text,
                     language: Language,
                     keepOrder: Boolean)

object QueryTerm:
  given Decoder[QueryTerm] = deriveDecoder
