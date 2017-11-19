package s10_factories.s01_functions

import s10_factories.s01_functions.loading.LoadingModule
import s10_factories.s01_functions.shunting.ShuntingModule
import s10_factories.s01_functions.station.StationModule

package shunting {

  import com.softwaremill.macwire.wire

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler)

  trait ShuntingModule {
    lazy val pointSwitcher = wire[PointSwitcher]
    lazy val trainCarCoupler = wire[TrainCarCoupler]
    lazy val trainShunter = wire[TrainShunter]
  }

}

package loading {

  import com.softwaremill.macwire.wire
  import s10_factories.s01_functions.loading.TrainLoader.CarLoaderFactory
  import s10_factories.s01_functions.shunting.PointSwitcher

  sealed trait CarType
  case object Coal extends CarType
  case object Refrigerated extends CarType
  case object Chemical extends CarType

  case class CarLoader()

  case class CraneController()

  case class TrainLoader(
                          craneController: CraneController,
                          pointSwitcher: PointSwitcher,
                          carLoaderFactory: CarLoaderFactory
                        )

  object TrainLoader {
    type CarLoaderFactory = CarType => CarLoader
  }

  // dependencies expressed using self type
  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

    lazy val carLoaderFactory = (ct: CarType) => wire[CarLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s10_factories.s01_functions.loading.TrainLoader
  import s10_factories.s01_functions.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  // dependencies expressed using self type
  trait StationModule {
    this: ShuntingModule with LoadingModule =>

    lazy val trainDispatch = wire[TrainDispatch]

    lazy val trainStation = wire[TrainStation]

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation.prepareAndDispatchNextTrain()

}
