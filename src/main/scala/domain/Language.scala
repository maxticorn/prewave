package domain

import enumeratum._

/** 
 * decided to present this type in the form of enum for type safety
 * included all languages that were found during the use of the API
 * if the api starts returning new languages, modifications will be necessary
 * 
 * (in reality I would separate this part into API library
 * to assign the responsibility for revision to the API developers)
 */
sealed abstract class Language(override val entryName: String) extends EnumEntry

case object Language extends Enum[Language] with CirceEnum[Language]:
  case object English extends Language("en")

  case object German extends Language("de")

  case object Spanish extends Language("es")

  case object Italian extends Language("it")

  case object Portuguese extends Language("pt")

  case object Arabic extends Language("ar")

  case object Vietnamese extends Language("ên")

  case object Norwegian extends Language("no")

  val values = findValues
