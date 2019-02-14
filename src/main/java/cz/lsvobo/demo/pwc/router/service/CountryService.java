package cz.lsvobo.demo.pwc.router.service;

import cz.lsvobo.demo.pwc.router.model.Country;
import java.util.Map;

public interface CountryService {

    Map<String, Country> getAllCountries();
}
