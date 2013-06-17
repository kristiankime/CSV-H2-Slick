package com.artclod.h2

import com.artclod.h2.WorkingData.scalaCodeFromCSV

object PrintScalaCodeExample {

	def main(args: Array[String]) {
		println(scalaCodeFromCSV("ScalaTable", "data/data.csv"))
	}

}