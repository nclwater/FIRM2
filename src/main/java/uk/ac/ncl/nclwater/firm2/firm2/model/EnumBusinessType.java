package uk.ac.ncl.nclwater.firm2.firm2.model;

public enum EnumBusinessType {
    SHOP_AND_PREMISES(211, "Shop and Premises"),
    STORE_AND_PREMISES(212, "Store and Premises"),
    SUPERSTORE_HYPERSTORE_AND_PREMISES(213, "Superstore/Hyperstore and Premises"),
    RETAIL_WAREHOUSE_AND_PREMISES(214, "Retail Warehouse and Premises"),
    SHOWROOM_AND_PREMISES(215, "Showroom and Premises"),
    KIOSK_AND_PREMISES(216, "Kiosk and Premises"),
    MARKET_OUTDOOR_AND_PREMISES(217, "Market Outdoor and Premises"),
    MARKET_INDOOR_AND_PREMISES(218, "Market Indoor and Premises"),
    GARAGE_VEHICLE_REPAIR_WORKSHOP_AND_PREMISES(221, "Garage/Vehicle Repair Workshop and Premises"),
    PETROL_FILLING_STATION_AND_PREMISES(222, "Petrol Filling Station and Premises"),
    CAR_SHOWROOM_AND_PREMISES(223, "Car Showroom and Premises"),
    PLANT_HIRE(224, "Plant Hire"),
    HAIRDRESSING_SALON_AND_PREMISES(231, "Hairdressing Salon and Premises"),
    BETTING_SHOP_AND_PREMISES(232, "Betting Shop and Premises"),
    LAUNDERETTE_AND_PREMISES(233, "Launderette and Premises"),
    PUBLIC_HOUSE_AND_PREMISES_CLUB_CLUBHOUSE(234, "Public House and Premises/Club/Clubhouse"),
    RESTAURANT_AND_PREMISES(235, "Restaurant and Premises"),
    CAFE_AND_PREMISES(236, "Café and Premises"),
    POST_OFFICE_AND_PREMISES(237, "Post Office and Premises"),
    OFFICES_AND_PREMISES(310, "Offices and Premises"),
    HI_TECH_OFFICES(311, "Hi Tech Offices"),
    BANK_AND_PREMISES(320, "Bank and Premises"),
    WAREHOUSE_AND_PREMISES(410, "Warehouse and Premises"),
    LAND_USED_FOR_STORAGE_AND_PREMISES(420, "Land Used for Storage and Premises"),
    ROAD_HAULAGE_DEPOT_AND_PREMISES(430, "Road Haulage Depot and Premises"),
    HOTEL_AND_PREMISES(511, "Hotel and Premises"),
    BOARDING_HOUSE_AND_PREMISES_HOLIDAY_UNIT(512, "Boarding House and Premises/Holiday Unit"),
    CARAVAN_PARK_AND_PREMISES(513, "Caravan Park and Premises"),
    BEACH_HUT(514, "Beach Hut"),
    SELF_CATERING_UNIT(515, "Self Catering Unit"),
    HOSTEL_AND_PREMISES(516, "Hostel and Premises"),
    BINGO_HALL_AND_PREMISES(517, "Bingo Hall and Premises"),
    THEATRE_CINEMA_AND_PREMISES(518, "Theatre/Cinema and Premises"),
    SPORTS_GROUND_PLAYING_FIELD_AND_PREMISES(521, "Sports Ground/Playing Field and Premises"),
    GOLF_COURSE_AND_PREMISES(522, "Golf Course and Premises"),
    SPORTS_LEISURE_CENTRE_AND_PREMISES(523, "Sports & Leisure Centre and Premises"),
    AMUSEMENT_ARCADE_PARK_AND_PREMISES(524, "Amusement Arcade/Park and Premises"),
    FOOTBALL_GROUND_AND_PREMISES(525, "Football Ground and Premises"),
    MOORING_WHARF_MARINA_AND_PREMISES(526, "Mooring/Wharf/Marina and Premises"),
    SWIMMING_POOL_AND_PREMISES(527, "Swimming Pool and Premises"),
    SCHOOL_COLLEGE_UNIVERSITY_NURSERY_AND_PREMISES(610, "School/College/University/Nursery and Premises"),
    SURGERY_AND_HEALTH_CENTRE_AND_PREMISES(620, "Surgery and Health Centre and Premises"),
    RESIDENTIAL_HOME(625, "Residential Home"),
    COMMUNITY_CENTRE_HALLS_AND_PREMISES(630, "Community Centre/Halls and Premises"),
    LIBRARY_AND_PREMISES(640, "Library and Premises"),
    POLICE_STATION_AND_PREMISES(650, "Police Station and Premises"),
    HOSPITAL_AND_PREMISES(660, "Hospital and Premises"),
    MUSEUM_AND_PREMISES(670, "Museum and Premises"),
    LAW_COURT_AND_PREMISES(680, "Law Court and Premises"),
    CHURCH(690, "Church"),
    WORKSHOP_AND_PREMISES(810, "Workshop and Premises"),
    FACTORY_WORKS_AND_PREMISES(820, "Factory/Works and Premises"),
    QUARRY_TIP_SITE_CONCRETE_BATCHING_AND_PREMISES(830, "Quarry/Tip Site/Concrete Batching and Premises"),
    SEWAGE_TREATMENT_WORKS_AND_PREMISES(840, "Sewage Treatment Works and Premises"),
    CAR_PARK_PREMISES(910, "Car Park/Premises"),
    PUBLIC_CONVENIENCE(920, "Public Convenience"),
    CEMETERY_AND_CREMATORIUM_AND_PREMISES(930, "Cemetery and Crematorium and Premises"),
    BUS_STATION_AND_PREMISES(940, "Bus Station and Premises"),
    DOCK_HEREDITAMENT_AND_PREMISES(950, "Dock Hereditament and Premises"),
    ELECTRICITY_HEREDITAMENT_AND_PREMISES(960, "Electricity Hereditament and Premises");

    private final int code;
    private final String description;

    EnumBusinessType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
