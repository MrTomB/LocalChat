package com.example.thomasburch.localchat.ChatResponse;

/**
 * Created by thomasburch on 2/13/16.
 */
import java.util.ArrayList;
import java.util.List;
//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class Location {

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("crossStreet")
    @Expose
    private String crossStreet;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("cc")
    @Expose
    private String cc;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("formattedAddress")
    @Expose
    private List<String> formattedAddress = new ArrayList<String>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Location() {
    }

    /**
     *
     * @param distance
     * @param postalCode
     * @param address
     * @param state
     * @param formattedAddress
     * @param lng
     * @param cc
     * @param lat
     * @param country
     * @param city
     * @param crossStreet
     */
    public Location(String address, String crossStreet, Double lat, Double lng, Integer distance, String postalCode, String cc, String city, String state, String country, List<String> formattedAddress) {
        this.address = address;
        this.crossStreet = crossStreet;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.postalCode = postalCode;
        this.cc = cc;
        this.city = city;
        this.state = state;
        this.country = country;
        this.formattedAddress = formattedAddress;
    }

    /**
     *
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     *     The crossStreet
     */
    public String getCrossStreet() {
        return crossStreet;
    }

    /**
     *
     * @param crossStreet
     *     The crossStreet
     */
    public void setCrossStreet(String crossStreet) {
        this.crossStreet = crossStreet;
    }

    /**
     *
     * @return
     *     The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     *     The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     *     The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     *     The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     *     The distance
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     *     The distance
     */
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     *     The postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode
     *     The postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     *
     * @return
     *     The cc
     */
    public String getCc() {
        return cc;
    }

    /**
     *
     * @param cc
     *     The cc
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     *
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     *     The formattedAddress
     */
    public List<String> getFormattedAddress() {
        return formattedAddress;
    }

    /**
     *
     * @param formattedAddress
     *     The formattedAddress
     */
    public void setFormattedAddress(List<String> formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

}