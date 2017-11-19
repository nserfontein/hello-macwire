package s03_macwire

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

  import com.softwaremill.macwire._

  lazy val trainShunter = wire[TrainShunter]
  lazy val pointSwitcher = wire[PointSwitcher]
  lazy val trainCarCoupler = wire[TrainCarCoupler]

  lazy val craneController = wire[CraneController]
  lazy val trainLoader = wire[TrainLoader]

  lazy val trainDispatch = wire[TrainDispatch]

  lazy val trainStation = wire[TrainStation]

  trainStation.prepareAndDispatchNextTrain()

}
