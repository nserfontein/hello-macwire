package s10_factories.s02_trait_modules

import s10_factories.s02_trait_modules.loading.LoadingModule
import s10_factories.s02_trait_modules.shunting.ShuntingModule
import s10_factories.s02_trait_modules.station.{Name, StationModule}

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
  import s10_factories.s02_trait_modules.shunting.PointSwitcher

  case class CraneController()

  case class TrainLoader(craneController: CraneController, pointSwitcher: PointSwitcher)

  trait LoadingModule {
    this: ShuntingModule =>

    lazy val craneController = wire[CraneController]
    lazy val trainLoader = wire[TrainLoader]

  }

}

package station {

  import com.softwaremill.macwire.wire
  import s10_factories.s02_trait_modules.loading.TrainLoader
  import s10_factories.s02_trait_modules.shunting.TrainShunter

  case class Name(value: String)

  case class TrainDispatch()

  case class TrainStation(name: Name, trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  trait StationModule {
    this: ShuntingModule with LoadingModule =>

    lazy val trainDispatch = wire[TrainDispatch]

    def trainStation(name: Name) = wire[TrainStation]

    // dependencies of the module ???
    //    def trainShunter: TrainShunter
    //    def trainLoader: TrainLoader

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule

  modules.trainStation(Name("Choochoo")).prepareAndDispatchNextTrain()

}
