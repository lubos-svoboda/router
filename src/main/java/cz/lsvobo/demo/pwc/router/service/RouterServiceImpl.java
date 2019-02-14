package cz.lsvobo.demo.pwc.router.service;

import cz.lsvobo.demo.pwc.router.model.Country;
import cz.lsvobo.demo.pwc.router.model.RouteNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouterServiceImpl implements RouterService {

    private Map<String, Country> countries;

    @Autowired
    private CountryService countryService;

    @Override
    public List<String> route(String from, String to) {
        Map<String, Country> countries = countryService.getAllCountries();

        Country fromCountry = countries.get(from);
        if (fromCountry == null) {
            throw new RouteNotFoundException();
        }
        Country toCountry = countries.get(to);
        if (toCountry == null) {
            throw new RouteNotFoundException();
        }
        if (from.equals(to)) {
            return Collections.singletonList(fromCountry.getName());
        }
        RouteFinder routeFinder = new RouteFinder(fromCountry, toCountry, countries);
        return routeFinder.findRoute().stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());
    }
}
