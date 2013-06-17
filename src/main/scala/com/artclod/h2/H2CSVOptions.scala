package com.artclod.h2

case class H2CSVOptions(caseSensitiveColumnNames: Boolean = false,
	charset: String = null,
	escape: String = null,
	fieldDelimiter: String = null,
	fieldSeparator: String = null,
	lineComment: Boolean = false,
	lineSeparator: String = null,
	`null`: String = null,
	rowSeparator: String,
	preserveWhitespace: Boolean = false) {

	private def sql(v: String, pre: String) = { if (v == null) { None } else { Some(pre + v) } }
	private def sql(v: Boolean, pre: String) = { if (!v) { None } else { Some(pre + v) } }
	private def csvOptions = sql(charset, "charset=") ::
		sql(escape, "escape=") ::
		sql(fieldDelimiter, "fieldDelimiter=") ::
		sql(fieldSeparator, "fieldSeparator=") ::
		sql(lineComment, "lineComment=") ::
		sql(lineSeparator, "lineSeparator=") ::
		sql(`null`, "null=") ::
		sql(rowSeparator, "rowSeparator=") ::
		sql(preserveWhitespace, "preserveWhitespace=") ::
		Nil

	//	STRINGDECODE('charset=UTF-8 escape=\" fieldDelimiter=\" fieldSeparator=, ' || 'lineComment=# lineSeparator=\n null= rowSeparator=')
	val sqlString = "STRINGDECODE('" + csvOptions.filter(!_.isEmpty).mkString(" ") + "')"

}