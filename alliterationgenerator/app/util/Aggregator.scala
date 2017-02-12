package util

import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.StandardOpenOption._
import java.nio.charset.StandardCharsets
import java.io.IOException

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import models.TermTypes
import models.terms.types.Term

import play.Play.application
import play.api.Logger

object Aggregator {

  val baseDirectoryPath = Paths.get(application.path.toString)
    .resolve(application.configuration.getString("files.submissions.basePath"))

  val baseAggregatedPath = baseDirectoryPath.
      resolve(application.configuration.getString("files.submissions.aggregatedBasePath"))

  val baseProcessedPath = baseDirectoryPath
          .resolve(application.configuration.getString("files.submissions.processedBasePath"))

  def aggregate() = Future {
    preparePath(baseAggregatedPath)
    preparePath(baseProcessedPath)

    TermTypes.terms.map { term =>
      val path = baseAggregatedPath.resolve(term.name + ".txt")
      aggregateTermFiles(term, path)
    }
  }

  def aggregateTermFiles(term: Term, path: Path) = {
    // Walk file tree, aggregating all files and moving processed files into a subdirectory
    val options = java.util.EnumSet.of(FileVisitOption.FOLLOW_LINKS)
    Files.walkFileTree(baseDirectoryPath, options, 1, new FileAggregator(term, path))
    //val matcher = FileSystems.getDefault().getPathMatcher("regex:^(" + term.name + ")_*$")
    //Files.walkFileTree(baseDirectoryPath, options, 1, new FileAggregator(matcher, target))
  }

  private def preparePath(path: Path) {
    if (!Files.exists(path)) {
      Logger.debug("Path nonexistant. Creating path: " + path)
      Files.createDirectories(path)
    }
  }

  /**
   *
   */
  //private class FileAggregator(matcher: PathMatcher, targetPath: Path) extends FileVisitor[Path] {
  private class FileAggregator(term: Term, targetPath: Path) extends FileVisitor[Path] {

    val termsSet = scala.collection.mutable.SortedSet[String]()

    def preVisitDirectory(directory: Path, attr: BasicFileAttributes) = {
      // Do nothing
      FileVisitResult.CONTINUE
    }

    def postVisitDirectory(directory: Path, exc: IOException) = {

      Future {
        // Export set to a file
        val writer = Files.newBufferedWriter(targetPath, StandardCharsets.UTF_8, CREATE, APPEND, WRITE)

        termsSet.foreach { term =>
          writer.write(term, 0, term.length)
          writer.newLine()
        }

        writer.close()
        Logger.debug("File written: " + targetPath)
      }

      FileVisitResult.CONTINUE
    }

    def visitFile(file: Path, attr: BasicFileAttributes) = {
      //if (matcher.matches(file.getFileName)) {
      if (file.getFileName.toString.startsWith(term.name)) {
        Logger.debug("Processing file: " + file)
        val reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)

        /* TODO: Implement when using Java 8
        reader.lines().map { line => }
        */

        // TODO: test if this is null
        var line = reader.readLine()
        if (line != null) {
          do {
            line = line.trim()
            if (!line.isEmpty) {
              if (!term.isCaseSensitive) {
                line = line.toLowerCase
              }
              termsSet += line
            }

            line = reader.readLine()
          } while (line != null)
        }

        reader.close()

        // Now move file into processed subdirectory
        val newPath = baseProcessedPath.resolve(file.getFileName)
        Files.move(file, newPath)

        Logger.debug("Done processing file: " + file)
      }

      FileVisitResult.CONTINUE
    }

    def visitFileFailed(file: Path, exc: IOException) = {
      Logger.error("[FileAggregator:visitFileFailed] Error: " + exc)
      FileVisitResult.CONTINUE
    }
  }
}
