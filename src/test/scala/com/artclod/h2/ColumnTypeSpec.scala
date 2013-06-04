package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class ColumnTypeSpec extends FlatSpec with ShouldMatchers {

	ColumnInt + "'s isValidSQL " should " say true for 1000" in {
		ColumnInt.isValidSQL("1000") should equal (true)
	}
	
	ColumnInt + "'s isValidSQL " should " say false for adsf" in {
		ColumnInt.isValidSQL("adsf") should equal (false)
	}
}