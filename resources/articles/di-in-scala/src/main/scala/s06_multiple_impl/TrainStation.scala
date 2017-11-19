package s06_multiple_impl

import s06_multiple_impl.loading.LoadingModule
import s06_multiple_impl.shunting.{ModernShuntingModule, ShuntingModule, TraditionalShuntingModule}
import s06_multiple_impl.station.StationModule

// NOTE: lots of boilerplate code, use only when needed!
package shunting {

  import com.softwaremill.macwire.wire

  trait TrainShunter

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TraditionalTrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler) extends TrainShunter

  case class TeleportingTrainShunter() extends TrainShunter

  trait ShuntingModule {
    lazy val pointSwitcher = wire[PointSwitcher]

    def trainShunter: TrainShunter
  }

  trait TraditionalShuntingModule extends ShuntingModule {
    lazy val trainCarCoupler = wire[TrainCarCoupler]
    lazy val trainShunter = wire[TraditionalTrainShunter]
  }

  trait ModernShuntingModule extends ShuntingModule {
    lazy val trainShunter = wire[TeleportingTrainShunter]
  }

}

package loading {

  import com.softwaremill.macwire.wire
  import s06_multiple_impl.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  // dependencies expressed using self type
  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s06_multiple_impl.loading.TrainLoader
  import s06_multiple_impl.shunting.TrainShunter

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

  val traditionalModules = new TraditionalShuntingModule
    with LoadingModule
    with StationModule

  val modernModules = new ModernShuntingModule
    with LoadingModule
    with StationModule

  traditionalModules.trainStation.prepareAndDispatchNextTrain()
  modernModules.trainStation.prepareAndDispatchNextTrain()

}
