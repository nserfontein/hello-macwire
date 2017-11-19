package s08_interceptors

import com.softwaremill.macwire.aop.ProxyingInterceptor
import s08_interceptors.loading.LoadingModule
import s08_interceptors.shunting.ShuntingModule
import s08_interceptors.station.StationModule

package shunting {

  import com.softwaremill.macwire.aop.Interceptor
  import com.softwaremill.macwire.wire

  case class PointSwitcher()

  case class TrainCarCoupler()

  case class TrainShunter(pointSwitcher: PointSwitcher, trainCarCoupler: TrainCarCoupler) {
    def shunt() = {
      println("shunting!")
    }
  }

  trait ShuntingModule {
    lazy val pointSwitcher = logEvents(wire[PointSwitcher])
    lazy val trainCarCoupler = logEvents(wire[TrainCarCoupler])
    lazy val trainShunter = wire[TrainShunter]

    def logEvents: Interceptor

  }

}

package loading {

  import com.softwaremill.macwire.wire
  import s08_interceptors.shunting.PointSwitcher

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
  import s08_interceptors.loading.TrainLoader
  import s08_interceptors.shunting.TrainShunter

  case class TrainDispatch()

  case class TrainStation(trainShunter: TrainShunter, trainLoader: TrainLoader, trainDispatch: TrainDispatch) {

    def prepareAndDispatchNextTrain(): Unit = {
      println(s"dispatching $this")
    }

  }

  trait StationModule {
    this: ShuntingModule with LoadingModule =>

    lazy val trainDispatch = wire[TrainDispatch]

    lazy val trainStation = wire[TrainStation]

  }

}

object TrainStation extends App {

  val modules = new ShuntingModule with LoadingModule with StationModule {
    override def logEvents = ProxyingInterceptor { ctx =>
      println("Calling method: " + ctx.method.getName)
      ctx.proceed()
    }
  }

  modules.trainStation.prepareAndDispatchNextTrain()

}
