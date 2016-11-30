package com.kabasoft.iws.gui

import java.time.LocalDate
import java.util.Date

import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.macros.Bootstrap.CommonStyle
import com.kabasoft.iws.gui.macros.GlobalStyles
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._
import scalacss.Defaults._
import org.widok.moment._

object Utils {
  @inline private def bss = GlobalStyles.bootstrapStyles

  def fmt(t:Date) = Moment(t.getTime).format("DD.MM.YYYY")
  def fmt(m:BigDecimal) = "%06.2f".format(m)

  // def format(d:String) = LocalDate.of(2016,7,7) //parse(d,DateTimeFormatter.ISO_LOCAL_DATE)
  //def format(d:String) = com.zoepepper.facades.jsjoda.LocalDate.parse(d,DateTimeFormatter.ISO_LOCAL_DATE)

  def noAction(e: ReactEventI):Callback = Callback {}

  def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list map (iws =>(iws.id+":"+iws.name))
  def buildTransIdList [A<:IWS](list: List[A]): List[String]= list map (iws =>(iws.id))

  def buildDate(id:String, value:Option[Date], defValue:Date, evt:ReactEventI=> Callback) = {
    val m = value getOrElse defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.date(bss.formControl, ^.id := id, //^.value := m,
    ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
  }
  def buildAmount(id:String, value:Option[BigDecimal], defValue:BigDecimal, evt:ReactEventI=> Callback) = {
    val m = value getOrElse defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.number(bss.formControl, ^.id := id,  //^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
  }


  def buildDItem1(id:String , value:Option[BigDecimal], defValue:BigDecimal, evt:ReactEventI=> Callback) = {
    val m = (value getOrElse defValue).bigDecimal
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, ^.id := id, ^.value :=m.toString,
       // <.td(<.input.text(bss.formControl, ^.id := id, ^.value := "%06.2f".format(m).replace(".",","),
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
    //^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
  }

  def buildWItem1[A](id:String , value:Option[A], defValue:A, evt:ReactEventI=> Callback) = {
    val m = value getOrElse defValue
    List(<.td(<.label(bss.td(50), ^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, bss.td(100), ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
        //^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
  }

  def buildAreaItem(id:String , value:Option[String], defValue:String, evt:ReactEventI=> Callback  , offset:String) = {
    val m = value getOrElse defValue
     <.div(^.cls :="form-group".concat( offset),
       <.label(^.`for` := id, id),
          <.textarea(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt,  ^.padding := 10.px,  ^.autoFocus := true)
  }

  def buildWItem[A](id:String , value:Option[A], defValue:A, evt:ReactEventI=> Callback ) = {
    val m = value getOrElse defValue

    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
    //^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true))
  }
  def buildBItem(id:String , value1:Option[Boolean], defValue:Boolean, evt:ReactEventI=> Callback , offset:String ) = {
    val m = value1 getOrElse defValue
    <.div( ^.cls := offset,
    <.label(^.`for` := id, id),
     // <.input(bss.formControl, ^.tpe := "checkbox", ^.checked := value1, ^.onClick   ==> evt))
       <.input.checkbox(bss.formControl, ^.id := id, ^.value := m, ^.placeholder := id),
                     ^.onChange ==> evt, ^.maxHeight:=2.px)
  }
  def buildLabel(id:String , value:Option[String], defValue:String) = {
    val m = value getOrElse  defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px ,  ^.paddingLeft := 10.px),
        <.td(<.label(^.`for` := m, m), ^.maxHeight:=2.px ,  ^.paddingLeft := 10.px))
  }
  def buildLabel(id:String , value:Option[Integer], defValue:Integer) = {
    val m = value getOrElse  defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px ,  ^.paddingLeft := 10.px),
      <.td(<.input.text(^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.maxHeight:=2.px,  ^.paddingLeft := 10.px))
  }

  def buildItem[A](id:String , value:Option[A], defValue:A) = {
    val m = value getOrElse  defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.maxHeight:=2.px,  ^.paddingLeft := 10.px))
  }

  def buildSItem(id:String,  itemsx:List[String], defValue:String, evt:String => Callback) =
    List(
     <.td(<.label(^.`for` := id, id), ^.maxHeight:=20.px),
     <.td(
         IWSSelect(label = id, value = defValue, onChange = evt, items = itemsx)
       )
    )

  def buildDateN(id:String, value:Option[Date], defValue:Date, evt:ReactEventI=> Callback, offset:String) = {
    val m = value getOrElse defValue
    <.div( ^.cls := offset,
    <.label(^.`for` := id, id),
      <.input.date(bss.formControl, ^.id := id, //^.value := m,
        ^.placeholder := id), ^.onChange ==> evt,   ^.autoFocus := true)
  }
  def buildSItemN(id:String,  itemsx:List[String], defValue:String, evt:String => Callback, offset:String) =
  <.div( ^.cls := offset,
      <.label(^.`for` := id, id),
      IWSSelect(label = id, value = defValue, onChange = evt, items = itemsx)
    )
  def buildWItemN[A](id:String , value:Option[A], defValue:A, evt:ReactEventI=> Callback, offset:String) = {
    val m = value getOrElse defValue
    <.div( ^.cls := offset,
      <.label(^.`for` := id, id),
      <.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=10, ^.autoFocus := true)
  }
  def buildDItem2[A](id:String , value:Option[A], defValue:A, evt:ReactKeyboardEventI=> Option[Callback] , offset:String) = {
  //  val m = value getOrElse defValue
    <.div( ^.cls := offset,
       <.label(^.`for` := id, id),
          <.input.text(bss.formControl, ^.id := id, //^.value := m.toString,
        ^.placeholder := id), ^.onKeyUp ==>?evt , ^.maxHeight:=2.px, ^.paddingLeft := 10.px,  ^.autoFocus := true)
  }

  def buildDateN2(id:String, value:Option[Date], defValue:Date, evt:ReactEventI=> Callback, offset:String) = {
  //  val m = value getOrElse defValue
    <.div( ^.cls := offset,
      <.label(^.`for` := id, id),
      <.input.date(bss.formControl, ^.id := id, //^.value := m,
        ^.placeholder := id), ^.onChange ==> evt,   ^.autoFocus := true)
  }

  def renderHeader (headers:Seq[String], padding:Int =10) =
    <.li(bss.listGroup.itemOpt(CommonStyle.info),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
       headers.map( field => (<.span(field ,^.padding:=padding)))
    )

  def renderHeader2 (headers:Seq[String], buttons:Seq[ReactElement], padding:Int =10) = List(
    <.li(bss.listGroup.itemOpt(CommonStyle.info),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
      headers.map( field => (<.span(field ,^.padding:=padding))),
      buttons
    ))
}

//https://oldfashionedsoftware.com/2009/07/30/lots-and-lots-of-foldleft-examples/
//def sum(list: List[Int]): Int = list.foldLeft(0)((r,c) => r+c)
//def sum(list: List[Int]): Int = list.foldLeft(0)(_+_)
//def count(list: List[Any]): Int = list.foldLeft(0)((sum,_) => sum + 1)
//def last[A](list: List[A]): A = list.foldLeft[A](list.head)((_, c) => c)
//def average(list: List[Double]): Double =
//list.foldLeft(0.0)(_+_) / list.foldLeft(0.0)((r,c) => r+1)

/*def average(list: List[Double]): Double = list match {
  case head :: tail => tail.foldLeft( (head,1.0) )((r,c) =>
  ((r._1 + (c/r._2)) * r._2 / (r._2+1), r._2+1) )._1
  case Nil => NaN
}

Penultimate

Write a function called ‘penultimate’ that takes a List[A] and returns the penultimate item (i.e. the next to last item) in the list. Hint: Use a tuple.
def penultimate[A](list: List[A]): A =
  list.foldLeft( (list.head, list.tail.head) )((r, c) => (r._2, c) )._1
This one is very much like the function ‘last’, but instead of keeping just the current item it keeps a Pair containing the previous and current items. When foldLeft completes, its result is a Pair containing the next-to-last and last items. The “_1” method returns just the penultimate item.

Contains

Write a function called ‘contains’ that takes a List[A] and an item of type A, and returns true if the item is one of the members of the list, and false if it isn’t.

def contains[A](list: List[A], item: A): Boolean =
  list.foldLeft(false)(_ || _==item)
We choose an initial value of false. That is, we’ll assume the item is not in the list until we can prove otherwise. We use each of the two parameters exactly once and in the proper order, so we can use the ‘_’ shorthand in our function literal. That function literal returns the result so far (a Boolean) ORed with a comparison of the current item and the target value. If the target is ever found, the accumulator becomes true and stays true as foldLeft continues.

Get

Write a function called ‘get’ that takes a List[A] and an index Int, and returns the list value at the index position. Throw an exception if the index is out of bounds.

def get[A](list: List[A], idx: Int): A =
  list.tail.foldLeft((list.head,0)) {
    (r,c) => if (r._2 == idx) r else (c,r._2+1)
  } match {
    case (result, index) if (idx == index) => result
    case _ => throw new Exception("Bad index")
  }
This one has two parts. First there’s the foldLeft, and the result is pattern matched. The foldLeft is pretty easy to follow. The accumulator is a Pair containing the current item and the current index. The current item keeps updating and the current index keeps incrementing until the current index equals the passed in idx. Once the correct index is found the same accumulator is returned over and over. This works fine if idx parameter is in bounds. If it’s out of bounds, though, the foldLeft just returns a Pair containing the last item and the last index. That’s where the pattern match comes in. If the Pair contains the right index then we use the result item. Otherwise, we throw an exception.

MimicToString

Write a function called ‘mimicToString’ that mimics List’s own toString method. That is, it should return a String containing a comma-delimited series of string representations of the list contents with “List(” on the left and “)” on the right.
def mimicToString[A](list: List[A]): String = list match {
  case head :: tail => tail.foldLeft("List(" + head)(_ + ", " + _) + ")"
  case Nil => "List()"
}
This one also uses a pattern match, but this time the match happens first. The pattern match just treats the empty list as a special case. For the general case (a non-empty list) we use, of course, foldLeft. The accumulator starts out as “List(” + the head item. Then each remaining item (notice foldLeft is called on tail) is appended with a leading “, ” and a final “)” is added to the result of foldLeft.

Reverse

This one’s kind of fun. Make sure to try it before you look at my solution. Write a function called ‘reverse’ that takes a List and returns the same list in reverse order.

def reverse[A](list: List[A]): List[A] =
  list.foldLeft(List[A]())((r,c) => c :: r)
A very simple solution! The initial value of the accumulator is just an empty list. We don’t use Nil, but instead spell out the List type so that Scala will know what type to make ‘r’. As I say, we start with the empty list which is sensible because the reverse of an empty list is an empty list. Then, as we go through the list, we place each item at the front of the accumulator. So the item at the front of list becomes the last item in the accumulator. This goes on until we reach the end of list, and that last member of list goes onto the front of the accumulator. It’s a really neat and tidy solution.

Unique

Write a function called ‘unique’ that takes a List and returns the same List, but with duplicated items removed.

def unique[A](list: List[A]): List[A] =
  list.foldLeft(List[A]()) { (r,c) =>
    if (r.contains(c)) r else c :: r
  }.reverse
As usual, we start with an empty list. foldLeft looks at each list item and if it’s already contained in the accumulator then then it stays as it is. If it’s not in the accumulator then it’s appended. This code bears a striking similarity to the ‘reverse’ function we wrote earlier except for the “if (r.contains(c)) r” part. Because of this, the foldLeft result is actually the original list with duplicates removed, but in reverse order. To keep the output in the same order as the input, we add the call to reverse. We could also have chained on the foldLeft from the ‘reverse’ function, like so:

def unique[A](list: List[A]): List[A] =
  list.foldLeft(List[A]()) { (r,c) =>
    if (r.contains(c)) r else c :: r
  }.foldLeft(List[A]())((r,c) => c :: r)
ToSet

Write a function called ‘toSet’ that takes a List and returns a Set containing the unique elements of the list.
def toSet[A](list: List[A]): Set[A] =
  list.foldLeft(Set[A]())( (r,c) => r + c)
Super easy one. You just start out with an empty Set, which would be the right answer for an empty List. Then you just add each list item to the accumulator. Since the accumulator is a Set, it takes care of eliminating duplicates for you.

Double

Write a function called ‘double’ that takes a List and a new List in which each item appears twice in a row. For example double(List(1, 2, 3)) should return List(1, 1, 2, 2, 3, 3).

def double[A](list: List[A]): List[A] =
  list.foldLeft(List[A]())((r,c) => c :: c :: r).reverse
Again, pretty easy. Are you starting to see a pattern. When you use foldLeft to transform one list into another, you usually end up with the reverse of what you really want.

Alternately, you could have used the foldRight method instead. This does the same thing as foldLeft, except it accumulates its result from back to front instead of front to back. I can’t recommend using it, though, due to problems I point out in my other post on foldLeft and foldRight. But here’s what it would look like:

def double[A](list: List[A]): List[A] =
  list.foldRight(List[A]())((c,r) => c :: c :: r)
InsertionSort

This one takes some thinking. Write a function called ‘insertionSort’ that uses foldLeft to sort the input List using the insertion sort algorithm. Try it on your own before you look at the solution.

Need a hint? Use List’s ‘span’ method.

Did you find a solution? Here’s mine:

def insertionSort[A <% Ordered[A]](list: List[A]): List[A] =
  list.foldLeft(List[A]()) { (r,c) =>
    val (front, back) = r.span(_ < c)
    front ::: c :: back
  }
First, the type parameter ensures that we have elements that can be arranged in order. We start, predictably, with an empty list as our initial accumulator. Then, for each item we assume the accumulator is in order (which it always will be), and use span to split it into two sub-lists: all already-sorted items less than the current item, and all already-sorted items greater than or equal to the current item. We put the current item in between these two and the accumulator remains sorted. This is, of course, not the fastest way to sort a list. But it’s a neat foldLeft trick.

Pivot

Speaking of sorting, you can implement part of quicksort with foldLeft, the pivot. Write a function called ‘pivot’ that takes a List, and returns a Tuple3 containing: (1) a list of all elements less than the original list’s first element, (2) the first element, and (3) a List of all elements greater than or equal to the first element.

def pivot[A <% Ordered[A]](list: List[A]): (List[A],A,List[A]) =
  list.tail.foldLeft[(List[A],A,List[A])]( (Nil, list.head, Nil) ) {
    (result, item) =>
    val (r1, pivot, r2) = result
    if (item < pivot) (item :: r1, pivot, r2) else (r1, pivot, item :: r2)
  }
We’re using the first element, head, as the pivot value, so we skip the head and call foldLeft on list.tail. We initialize the accumulator to a Tuple3 containing the head element with an empty list on either side. Then for each item in the list we just pick which of the two lists to add to based on a comparison with the pivot value.

If you take the additional step of turning this into a recursive call, you can implement a quicksort algorithm. It probably won’t be a very efficient one because it will involve a lot of building and rebuilding lists. Give it a try if you like, and then look at my solution:

def quicksort[A <% Ordered[A]](list: List[A]): List[A] = list match {
  case head :: _ :: _ =>
    println(list)
    list.foldLeft[(List[A],List[A],List[A])]( (Nil, Nil, Nil) ) {
      (result, item) =>
      val (r1, r2, r3) = result
      if      (item < head) (item :: r1, r2, r3)
      else if (item > head) (r1, r2, item :: r3)
      else                  (r1, item :: r2, r3)
    } match {
      case (list1, list2, list3) =>
        quicksort(list1) ::: list2  ::: quicksort(list3)
    }
  case _ => list
}
Basically, for all lists that have more than 1 element the function chooses the head element as the pivot value, uses foldLeft to divide the list into three (less than, equal to, and greater than the pivot), recursively sorts the less-than and greater-than lists, and knits the three together.

Encode

Ok, we got a little into the weeds with that last one. Here’s a simpler one. Write a function called ‘encode’ that takes a List and returns a list of Pairs containing the original values and the number of times they are repeated. So passing List(1, 2, 2, 2, 2, 2, 3, 2, 2) to encode will return List((1, 1), (2, 5), (3, 1), (2, 2)).

def encode[A](list: List[A]): List[(A,Int)] =
list.foldLeft(List[(A,Int)]()){ (r,c) =>
    r match {
      case (value, count) :: tail =>
        if (value == c) (c, count+1) :: tail
        else            (c, 1) :: r
      case Nil =>
        (c, 1) :: r
    }
}.reverse
Decode

You knew this was coming. Write a function called ‘decode’ that does the opposite of encode. Calling ‘decode(encode(list))’ should return the original list.

def decode[A](list: List[(A,Int)]): List[A] =
list.foldLeft(List[A]()){ (r,c) =>
    var result = r
    for (_ <- 1 to c._2) result = c._1 :: result
    result
}.reverse
Encode and decode could both have been written by using foldRight and dropping the call to reverse.

Group

One last example. Write a function called ‘group’ that takes a List and an Int size that groups elements into sublists of the specified sizes. So calling “group( List(1, 2, 3, 4, 5, 6, 7), 3)” should return List(List(1, 2, 3), List(4, 5, 6), List(7)). Don’t forget to make sure list items are in the right order. Try it yourself before you look at the solution below.

def group[A](list: List[A], size: Int): List[List[A]] =
  list.foldLeft( (List[List[A]](),0) ) { (r,c) => r match {
    case (head :: tail, num) =>
      if (num < size)  ( (c :: head) :: tail , num + 1 )
      else             ( List(c) :: head :: tail , 1 )
    case (Nil, num) => (List(List(c)), 1)
    }
  }._1.foldLeft(List[List[A]]())( (r,c) => c.reverse :: r)
This code uses the first foldLeft to group the items in a way that’s convenient to list operations, and that last foldLeft to fix the order, which would otherwise be wrong in both the outer and inner lists.
*/