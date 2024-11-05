/**
 * for type safety decided to define these types
 * could be Refined, NewType or opaque, but I don't think it's necessary for this task
 */
package object domain:
  type TermId = Long
  type AlertId = String
  type Text = String
