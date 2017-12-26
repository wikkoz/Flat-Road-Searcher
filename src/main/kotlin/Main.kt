import model.Model
import view.MainWindow

fun main(args: Array<String>) {
    val model = Model()
    MainWindow.createAndInit(model)
}