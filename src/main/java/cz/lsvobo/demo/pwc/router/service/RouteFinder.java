package cz.lsvobo.demo.pwc.router.service;

import cz.lsvobo.demo.pwc.router.model.Country;
import cz.lsvobo.demo.pwc.router.model.RouteNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class RouteFinder {

    private final Logger logger = LoggerFactory.getLogger(RouteFinder.class);

    private final Country toCountry;
    private final Country fromCountry;
    private final Map<String, Country> allCountries;
    private Set<String> visitedCountryNames = new HashSet<>();
    private List<LinkedList<Country>> routes = new LinkedList<>();


    public RouteFinder(Country fromCountry, Country toCountry, Map<String, Country> allCountries) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.allCountries = allCountries;
    }

    public List<Country> findRoute() {
        logger.debug("Find new route " + fromCountry + " -> " + toCountry);

        LinkedList<Country> firstRoute = new LinkedList<>();
        firstRoute.add(fromCountry);
        this.routes.add(firstRoute);
        this.visitedCountryNames.add(fromCountry.getName());

        return expandRoutes();
    }

    private List<Country> expandRoutes() {
        if (logger.isDebugEnabled()) {
            logger.debug("Visited countries: " + visitedCountryNames);
            routes.forEach((route) -> logger.debug(route.toString()));
        }

        List<LinkedList<Country>> newRoutes = new ArrayList<>();

        for (LinkedList<Country> route: routes) {
            Country lastCountry = route.getLast();
            if (CollectionUtils.isEmpty(lastCountry.getBorders())) {
                continue;
            }
            if (lastCountry.getBorders().contains(toCountry.getName())) {
                route.add(toCountry);
                logger.debug("Route found: " + route);
                return route;
            }
            for (String country: lastCountry.getBorders()) {
                if (visitedCountryNames.contains(country)) {
                    continue;
                } else {
                    visitedCountryNames.add(country);
                }
                LinkedList<Country> newRoute = new LinkedList<>(route);
                newRoute.add(allCountries.get(country));
                newRoutes.add(newRoute);
            }
        }
        if (visitedCountryNames.size() == allCountries.size() || newRoutes.size() == 0) {
            throw new RouteNotFoundException();
        }
        routes = newRoutes;
        return expandRoutes();
    }
}
