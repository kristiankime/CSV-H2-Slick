package com.artclod.h2

import scala.slick.jdbc.GetResult
import scala.slick.jdbc.{StaticQuery => Q}
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession

object SimpleExample {

	def main(args: Array[String]) {
		Database.forURL("jdbc:h2:mem:working_data;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver") withSession {
			Q.updateNA("CREATE TABLE DATA_TABLE AS SELECT * FROM CSVREAD('data/data.csv');").execute
			Q.queryNA("SELECT * FROM DATA_TABLE")(GetResult(r => (r.nextString, r.nextString, r.nextString))).foreach(println(_))
		}
	}

}