package com.artclod.h2

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import java.sql.Date

object SimpleDataTyped extends Table[(Int, String, Date)]("SIMPLE_DATA_TYPED") {
	def column1 = column[Int]("COLUMNA")
	def column2 = column[String]("COLUMNB")
	def column3 = column[Date]("COLUMNC")
	def * = column1 ~ column2 ~ column3
}