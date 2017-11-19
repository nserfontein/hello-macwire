package s04_simple_scoping

case class PointSwitcher()

case class TrainCarCoupler()

case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

case class CraneController()

case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

case class TrainDispatch() {
  println("constructing train dispatch")
}

case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

  def prepareAndDispatchNextTrain(): Unit = {
    println(s"dispatching $this")
  }

}

object TrainStation extends App {

  import com.softwaremill.macwire._

  lazy val trainShunter = wire[TrainShunter]
  lazy val pointSwitcher = wire[PointSwitcher]
  lazy val trainCarCoupler = wire[TrainCarCoupler]

  lazy val craneController = wire[CraneController]
  lazy val trainLoader = wire[TrainLoader]

  // dependent scope using 'def'
  def trainDispatch = wire[TrainDispatch]

  // the stations share all services except the train dispatch,
  // for which a new instance is create on each usage
  lazy val trainStationEast = wire[TrainStation]
  lazy val trainStationWest = wire[TrainStation]

  trainStationEast.prepareAndDispatchNextTrain()
  trainStationWest.prepareAndDispatchNextTrain()

}
