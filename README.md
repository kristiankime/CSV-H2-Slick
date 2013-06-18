# CSV-H2-Slick

## Introduction

There are a number of programming environments that let users quickly load CSV files into a format that can be more easily manipulated in code. Typically there is some load CSV command that can be pointed at a file on disk. The command reads the file, tries to guess the right types for each of the column and then prepares the file as some easily accessible data structure. R, for example, has "read.csv" which takes a path to a CSV file and loads the file as a Data Frame.

Simple Load CSV commands have a number of obvious advantages. Simply put, at least when you start working with a file, it's usually exactly what you want to do. In a quick one liner you have immediate access to the CSV file as the appropriate data structure. But these commands are not without their drawbacks. Using R for a concrete point of reference "read.csv" always loads the entire file into memory. Everything is dynamically typed. And it's easy to forget the types may have been guessed wrong which can lead to some confusing results later on. 

This project is an attempting to build something that has the ease of use of a load CSV one liner but ends up providing the user with something that is a more rigorous/powerful then is usually obtained. Specifically this project provides code that can, in a few lines, load CSV files into a relational database (H2) and generate the Slick Scala code which makes interfacing with that table easy.

The most important part of the code is the WorkingData Scala object. It is essential an easy way to interact with a vm wide H2 database. The major functions are 

* scalaCodeFromCSV
* loadCSV
* run

### scalaCodeFromCSV
This method will look at a CSV file and generate the code for a Slick Table Scala object that corresponds to the CSV file. The function literally returns a string that can be copied and pasted into an editor or a REPL.

### loadCSV
This method is usually used directly after scalaCodeFromCSV taking the same CSV file as input and the generated Slick object. It will set everything up (ie load the CSV file with the correct types etc) so that future Slick calls will work against the correct data. 

### run
This method is just syntactic sugar to execute Slick commands with a session associated with the vm wide H2 database.
