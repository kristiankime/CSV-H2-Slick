package com.artclod.h2

import scala.slick.driver.H2Driver.simple._ // Use H2Driver to connect to an H2 database
import scala.slick.session.Database
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Database.threadLocalSession // Use the implicit threadLocalSession
import java.util.UUID
import com.artclod.h2.WorkingData._

object SimpleExample {

	def main(args: Array[String]) {
		loadCSVColumnsAllString("data/data.csv", SimpleData.tableName)
		val columnTypes = guessColumnTypes(SimpleData.tableName)
		System.err.println(columnTypes)
		run(Query(SimpleData) foreach { v => println(v)})
		

		loadCSV("data/data.csv", SimpleDataTyped.tableName, columnTypes: _*)
		run(Query(SimpleDataTyped) foreach { v => println(v) })
		
//		run{
//			val foo = Query(SimpleDataTyped).groupBy(r => (r.column1, r.column2))
//		}
		
		println(scalaCodeFor("ScalaTable", SimpleDataTyped.tableName, columnTypes: _*))
		
		run(Query(ScalaTable) foreach { v => println(v) })
	}

}