package com.example.supletorio_cobena;

public class PaisModels {
    public static class DetallePaisResponse {
        public Results Results;
    }

    public static class Results {
        public String Name;
        public Capital Capital;
        public CountryCodes CountryCodes;
        public String TelPref;
        public double[] GeoPt;
        public GeoRectangle GeoRectangle;
    }

    public static class Capital {
        public String Name;
    }

    public static class CountryCodes {
        public String iso2;
        public String iso3;
        public String isoN;
        public String fips;
    }

    public static class GeoRectangle {
        public double West;
        public double East;
        public double North;
        public double South;
    }
}
