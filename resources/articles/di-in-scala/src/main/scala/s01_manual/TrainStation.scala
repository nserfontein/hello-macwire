package s01_manual

case class PointSwitcher()

case class TrainCarCoupler()

case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

case class CraneController()

case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

case class TrainDispatch()

case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

  def prepareAndDispatchNextTrain(): Unit = {
    println(s"dispatching $this")
  }

}

object TrainStation extends App {

  lazy val trainShunter = new TrainShunter(pointSwitcher, trainCarCoupler)
  lazy val pointSwitcher = new PointSwitcher()
  lazy val trainCarCoupler = new TrainCarCoupler()

  lazy val craneController = new CraneController()
  lazy val trainLoader = new TrainLoader(craneController, pointSwitcher)

  lazy val trainDispatch = new TrainDispatch()

  lazy val trainStation = new TrainStation(trainShunter, trainLoader, trainDispatch)

  trainStation.prepareAndDispatchNextTrain()
}
