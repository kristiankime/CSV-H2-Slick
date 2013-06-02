package com.artclod.h2

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
// Use the implicit threadLocalSession
import Database.threadLocalSession

object SimpleData extends Table[(String, String, String)]("TEST") {
	def column1 = column[String]("column 1")
	def column2 = column[String]("column 2")
	def column3 = column[String]("column 3")
	def * = column1 ~ column2 ~ column3
}