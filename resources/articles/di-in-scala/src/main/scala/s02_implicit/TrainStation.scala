package s02_implicit

case class PointSwitcher()

case class TrainCarCoupler()

case class TrainShunter(implicit pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

case class CraneController()

case class TrainLoader(implicit craneController: CraneController, pointSwitcher: PointSwitcher)

case class TrainDispatch()

case class TrainStation(implicit trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

  def prepareAndDispatchNextTrain(): Unit = {
    println(s"dispatching $this")
  }

}

object TrainStation extends App {

  implicit lazy val pointSwitcher = new PointSwitcher
  implicit lazy val trainCarCoupler = new TrainCarCoupler
  implicit lazy val trainShunter = new TrainShunter

  implicit lazy val craneController = new CraneController
  implicit  lazy val trainLoader = new TrainLoader

  implicit  lazy val trainDispatch = new TrainDispatch

  implicit  lazy val trainStation = new TrainStation

  trainStation.prepareAndDispatchNextTrain()

}
