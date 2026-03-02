package simulations.core;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import simulations.protocol.HttpProtocolFactory;

public abstract class BaseSimulation extends Simulation {

    protected HttpProtocolBuilder httpProtocol() {
        return HttpProtocolFactory.createHttpProtocol();
    }
}