ODFTemplate: A Template Processor for Open Document Text Documents
==================================================================

I created this small Scala library for generating Open Office text documents from templates.
The used Scala version is 2.8.0. The project is built via [sbt](http://code.google.com/p/simple-build-tool/).
For parsing and modifying the Open Document Format, ODFDom from the [ODF Toolkit](http://odftoolkit.org/) is used.

Template Language
-----------------

The template language is quite simple and is inspired by [Apache Wicket](http://wicket.apache.org).
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

Clone the Git repository and type `sbt publish-local` in the project directory (assuming you have [sbt](http://code.google.com/p/simple-build-tool/) installed).

You can then define a dependency in your project's sbt build file:

    val odftemplate = "de.gtlk" %% "odftemplate" % "1.0"

However, this is not all. The problem is that there is no public Maven repository for the ODFDom Jar file. Thus, you need to add the file `lib/odfdom-0.8.6.jar` to your project manually.

If you don't use sbt for your project, you can just use the jar `target/scala_2.8.0/odftemplate_2.8.0-1.0.jar`. The dependencies have to be added manually then (namely, `lib/odfdom-0.8.6.jar`, `xercesImpl-2.9.1.jar` and `xml-apis-1.3.04.jar`).
