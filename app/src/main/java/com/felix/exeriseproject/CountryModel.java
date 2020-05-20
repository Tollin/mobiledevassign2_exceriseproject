package com.felix.exeriseproject;

public class CountryModel {
    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getSlug() {
        return Slug;
    }

    public void setSlug(String slug) {
        Slug = slug;
    }

    public String getISO2() {
        return ISO2;
    }

    public void setISO2(String ISO2) {
        this.ISO2 = ISO2;
    }

    public CountryModel() {
    }

    /**
     *
     * @param country
     * @param slug
     * @param ISO2
     */
    public CountryModel(String country, String slug, String ISO2) {
        Country = country;
        Slug = slug;
        this.ISO2 = ISO2;
    }

    private String Country;
    private String Slug;
    private String ISO2;
}
