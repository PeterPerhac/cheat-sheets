import java.time.{LocalDate, LocalDateTime}
import java.util.Date

import org.joda.time.DateTime


trait Now[T] {
  def apply(): T
}

object Now {

  def apply[T : Now]: Now[T] = implicitly

  def apply[T](value: => T): Now[T] = new Now[T] {
    override def apply(): T = value
  }

  implicit object DateNow extends Now[Date] {
    override def apply(): Date = new Date()
  }

  implicit object JodaDateTimeNow extends Now[DateTime] {
    override def apply(): DateTime = DateTime.now()
  }

  implicit object LocalDateNow extends Now[LocalDate] {
    override def apply(): LocalDate = LocalDate.now()
  }

  implicit object LocalDateTimeNow extends Now[LocalDateTime] {
    override def apply(): LocalDateTime = LocalDateTime.now()
  }

}

