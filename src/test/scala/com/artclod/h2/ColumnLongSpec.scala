package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class ColumnLongSpec extends FlatSpec with ShouldMatchers {

	ColumnLong + "'s isValidSQL " should " say true for positive ints (eg 1000)" in {
		ColumnLong.isValidSQL("1000") should equal(true)
	}

	ColumnLong + "'s isValidSQL " should " say false for arbitrary strings (eg adsf)" in {
		ColumnLong.isValidSQL("adsf") should equal(false)
	}

	ColumnLong + "'s isValidSQL " should " say true for negative ints (eg -100)" in {
		ColumnLong.isValidSQL("-100") should equal(true)
	}

	ColumnLong + "'s isValidSQL " should " say false for decimals (eg 100.45)" in {
		ColumnLong.isValidSQL("100.45") should equal(false)
	}
	
	ColumnLong + "'s isValidSQL " should " say true for numbers with commas (eg 1,000,000)" in {
		ColumnLong.isValidSQL("1,000,000") should equal(true)
	}

	ColumnLong + "'s isValidSQL " should " say true for max int (i.e. 9,223,372,036,854,775,807)" in {
		ColumnLong.isValidSQL("9,223,372,036,854,775,807") should equal(true)
	}

	ColumnLong + "'s isValidSQL " should " say false for above max int (i.e. 9,223,372,036,854,775,807 + 1)" in {
		ColumnLong.isValidSQL("9,223,372,036,854,775,808") should equal(false)
	}

	ColumnLong + "'s isValidSQL " should " say true for min int (i.e. -9,223,372,036,854,775,808)" in {
		ColumnLong.isValidSQL("-9,223,372,036,854,775,808") should equal(true)
	}

	ColumnLong + "'s isValidSQL " should " say false for below min int (i.e. -9,223,372,036,854,775,808) - 1)" in {
		ColumnLong.isValidSQL("-9,223,372,036,854,775,809") should equal(false)
	}

}