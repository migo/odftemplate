ODFTemplate: A Template Processor for Open Document Text Documents
==================================================================

I created this small Scala library for generating Open Office text documents from templates.
The used Scala version is 2.13.7. The project is built via [sbt](https://www.scala-sbt.org/).
For parsing and modifying the Open Document Format, ODFDom from the [ODF Toolkit](https://odftoolkit.org/) is used.

Template Language
-----------------

The template language is quite simple and is inspired by [Apache Wicket](https://wicket.apache.org/).
In the template, there are only simple markers such as [[amount]]. It contains no logic at all. The template can be created with Open Office and saved as a normal ODT document.

The action that is performed with a paragraph containing such a marker is defined in the Scala code:

* The marker can just be replaced with a value
* The whole paragraph containing the marker can be removed
* The paragraph containing the marker can be repeated. In each repetition, all markers in the paragraph are replaced with new values.

These are the current actions that can be performed with a marker. However, it should be fairly easy to add other replacements if needed. For example, repetition of a table row could be added.

Example
-------

There is an example document and corresponding Scala code under `src/test/scala`. The example is a simple invoice. The template parameters are the address of the recipient and the invoice positions.

Using the Library in your project
---------------------------------

Clone the Git repository and type `sbt publishLocal` in the project directory (assuming you have [sbt](https://www.scala-sbt.org/) installed).

You can then define a dependency in your project's sbt build file:

    val odftemplate = "de.gtlk" %% "odftemplate" % "1.1"

