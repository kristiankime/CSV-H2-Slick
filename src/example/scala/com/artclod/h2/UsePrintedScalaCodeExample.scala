package com.artclod.h2

import java.sql.Date

import scala.slick.driver.H2Driver.simple._ // Use H2Driver to connect to an H2 database
import scala.slick.session.Database._
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }

import com.artclod.h2.WorkingData._

object ScalaTable extends Table[(Int, String, Date)]("SCALATABLE") {
	def COLUMNA = column[Int]("COLUMNA")
	def COLUMNB = column[String]("COLUMNB")
	def COLUMNC = column[Date]("COLUMNC")
	def * = COLUMNA ~ COLUMNB ~ COLUMNC
	def inferredColumnData = Vector(InferredColumn("COLUMNA", false, 1, ColumnInt), InferredColumn("COLUMNB", false, 1, ColumnString), InferredColumn("COLUMNC", false, 8, ColumnDate_yyy_MM_dd))
}

object UsePrintedScalaCodeExample {

	def main(args: Array[String]) {
		loadCSV("data/data.csv", ScalaTable)
		
		run{Query(ScalaTable) foreach ( println(_) ) }
	}

}