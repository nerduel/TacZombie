package controller

import spray.json._
import spray.json.DefaultJsonProtocol._
import DefaultJsonProtocol._ // !!! IMPORTAN
import taczombie.model._

object test {
 		
		case class GameAsJson(cmd: String, currentPlayer: Char, gameState: JsValue, cells: JsValue)
 				
 		case class Data(cmd: String, currentPlayer: Char, gameState: JsValue, cells: JsValue)
 		case class GameData(gameState: String, lives: Int, movesLeft: Int, coins: Int, score: Int)
 		case class Cell(x: Int, y: Int, token: String, isHiglighted: Boolean)

		object GameDataJsonProtocol extends DefaultJsonProtocol {
		  implicit val GameDataFormat = jsonFormat5(GameData)
		  implicit val CellFormat = jsonFormat4(Cell)
		  implicit val DataFormat = jsonFormat4(Data)
		}
		
		import GameDataJsonProtocol._
		val cell = Cell(2,2,"H",true)     //> cell  : controller.test.Cell = Cell(2,2,H,true)
		val cells = List[Cell](cell,cell,cell)
                                                  //> cells  : List[controller.test.Cell] = List(Cell(2,2,H,true), Cell(2,2,H,true
                                                  //| ), Cell(2,2,H,true))
		val cellsAsJson = cells.toJson    //> cellsAsJson  : spray.json.JsValue = [{"x":2,"y":2,"token":"H","isHiglighted"
                                                  //| :true},{"x":2,"y":2,"token":"H","isHiglighted":true},{"x":2,"y":2,"token":"H
                                                  //| ","isHiglighted":true}]
	  val gameData = GameData("won",1,1,1,1)  //> gameData  : controller.test.GameData = GameData(won,1,1,1,1)
	  val gameDataJson = gameData.toJson      //> gameDataJson  : spray.json.JsValue = {"gameState":"won","lives":1,"movesLeft
                                                  //| ":1,"coins":1,"score":1}
	  val data = Data("notify", 'H', gameDataJson, cellsAsJson)
                                                  //> data  : controller.test.Data = Data(notify,H,{"gameState":"won","lives":1,"
                                                  //| movesLeft":1,"coins":1,"score":1},[{"x":2,"y":2,"token":"H","isHiglighted":
                                                  //| true},{"x":2,"y":2,"token":"H","isHiglighted":true},{"x":2,"y":2,"token":"H
                                                  //| ","isHiglighted":true}])
	  val dataAsJson = data.toJson            //> dataAsJson  : spray.json.JsValue = {"cmd":"notify","currentPlayer":"H","gam
                                                  //| eState":{"gameState":"won","lives":1,"movesLeft":1,"coins":1,"score":1},"ce
                                                  //| lls":[{"x":2,"y":2,"token":"H","isHiglighted":true},{"x":2,"y":2,"token":"H
                                                  //| ","isHiglighted":true},{"x":2,"y":2,"token":"H","isHiglighted":true}]}

}