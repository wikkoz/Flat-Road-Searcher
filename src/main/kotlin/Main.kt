import main.kotlin.controller.PlaceSearcherListener
import main.kotlin.controller.RoadSearcher
import main.kotlin.database.Database
import model.Model
import view.MainWindow


fun main(args: Array<String>) {
    val model = Model()
    val database = Database()
    MainWindow.createAndInit(model, PlaceSearcherListener(database), RoadSearcher(database))
}