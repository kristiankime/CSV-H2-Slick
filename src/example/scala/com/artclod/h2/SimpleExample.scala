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

		run(Query(SimpleData) foreach { v => println(v)})
		
		System.err.println(guessColumnTypes(SimpleData.tableName))
	}

}