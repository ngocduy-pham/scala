/*     ___ ____ ___   __   ___   ___
**    / _// __// _ | / /  / _ | / _ \  Scala classfile decoder
**  __\ \/ /__/ __ |/ /__/ __ |/ ___/  (c) 2003-2010, LAMP/EPFL
** /____/\___/_/ |_/____/_/ |_/_/      http://scala-lang.org/
**
*/

// $Id$

package scala.tools.scalap

import scala.tools.scalap.scalax.rules.scalasig._
import scala.tools.nsc.util.ScalaClassLoader.getSystemLoader

/** Temporary decoder.  This would be better off in the scala.tools.nsc
 *  but right now the compiler won't acknowledge scala.tools.scalap
 *  when it's bootstrapping, so the reference has to go from here to there.
 */
object Decode {  
  private def getAliasSymbol(t: Type): Symbol = t match {
    case TypeRefType(_, s, _)   => s
    case PolyType(typeRef, _)   => getAliasSymbol(typeRef)
    case _                      => NoSymbol
  }

  /** private[scala] so nobody gets the idea this is a supported interface.
   */
  private[scala] def caseParamNames(path: String): Option[List[String]] = {
    val (outer, inner) = (path indexOf '$') match {
      case -1   => (path, "")
      case x    => (path take x, path drop (x + 1))
    }
    
    for {
      clazz <- getSystemLoader.tryToLoadClass[AnyRef](outer)
      ssig <- ScalaSigParser.parse(clazz)
    }
    yield {
      val f: PartialFunction[Symbol, List[String]] =
        if (inner.isEmpty) {
          case x: MethodSymbol if x.isCaseAccessor && (x.name endsWith " ") => List(x.name dropRight 1)
        }
        else {
          case x: ClassSymbol if x.name == inner  =>
            val xs = x.children filter (child => child.isCaseAccessor && (child.name endsWith " "))
            xs.toList map (_.name dropRight 1)
        }
      
      (ssig.symbols partialMap f).flatten toList
    }
  }
  
  /** Returns a map of Alias -> Type for the given package.
   */
  private[scala] def typeAliases(pkg: String) = {
    for {
      clazz <- getSystemLoader.tryToLoadClass[AnyRef](pkg + ".package")
      ssig <- ScalaSigParser.parse(clazz)
    }
    yield {
      val typeAliases = ssig.symbols partialMap { case x: AliasSymbol => x }
      Map(typeAliases map (x => (x.name, getAliasSymbol(x.infoType).path)): _*)
    }
  }
}  
