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

object ScalaTable extends Table[(Int, String, Date)]("SIMPLE_DATA_TYPED") {
	def COLUMNA = column[Int]("COLUMNA")
	def COLUMNB = column[String]("COLUMNB")
	def COLUMNC = column[Date]("COLUMNC")
	def * = COLUMNA ~ COLUMNB ~ COLUMNC
	def inferredColumnData = Vector(InferedColumn("COLUMNA", false, 1, ColumnInt), InferedColumn("COLUMNB", false, 1, ColumnString), InferedColumn("COLUMNC", false, 9, ColumnDate_yyy_MM_dd))
}