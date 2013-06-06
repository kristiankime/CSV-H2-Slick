package com.artclod.h2

import scala.slick.driver.H2Driver.simple._ // Use H2Driver to connect to an H2 database
import scala.slick.session.Database
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import Database.threadLocalSession // Use the implicit threadLocalSession
import java.util.UUID
import com.artclod.h2.WorkingData._

object SimpleExample3 {

	def main(args: Array[String]) {
		loadCSV("data/data.csv", ScalaTable)
		
		run{Query(ScalaTable) foreach ( println(_) ) }
	}

}