package org.helianto.ingress.domain;

/**
 * Geo location by ip.
 */
public class IpGeoLocation {

    private String ip;

    private String countryCode;

    private String countryName;

    private String city;

    private String zipCode;

    private String timeZone;

    private double latitude;

    private double longitude;

    private String metroCode;

    public IpGeoLocation() {
        super();
    }

    public IpGeoLocation(String ip, String countryCode, String countryName, String city
            , String zipCode, String timeZone, double latitude, double longitude, String metroCode) {
        this();
        this.ip = ip;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.city = city;
        this.zipCode = zipCode;
        this.timeZone = timeZone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.metroCode = metroCode;
    }

    public String getIp() {
        return this.ip;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getCity() {
        return this.city;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getMetroCode() {
        return this.metroCode;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMetroCode(String metroCode) {
        this.metroCode = metroCode;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IpGeoLocation)) return false;
        final IpGeoLocation other = (IpGeoLocation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$ip = this.getIp();
        final Object other$ip = other.getIp();
        if (this$ip == null ? other$ip != null : !this$ip.equals(other$ip)) return false;
        final Object this$countryCode = this.getCountryCode();
        final Object other$countryCode = other.getCountryCode();
        if (this$countryCode == null ? other$countryCode != null : !this$countryCode.equals(other$countryCode))
            return false;
        final Object this$countryName = this.getCountryName();
        final Object other$countryName = other.getCountryName();
        if (this$countryName == null ? other$countryName != null : !this$countryName.equals(other$countryName))
            return false;
        final Object this$city = this.getCity();
        final Object other$city = other.getCity();
        if (this$city == null ? other$city != null : !this$city.equals(other$city)) return false;
        final Object this$zipCode = this.getZipCode();
        final Object other$zipCode = other.getZipCode();
        if (this$zipCode == null ? other$zipCode != null : !this$zipCode.equals(other$zipCode)) return false;
        final Object this$timeZone = this.getTimeZone();
        final Object other$timeZone = other.getTimeZone();
        if (this$timeZone == null ? other$timeZone != null : !this$timeZone.equals(other$timeZone)) return false;
        if (Double.compare(this.getLatitude(), other.getLatitude()) != 0) return false;
        if (Double.compare(this.getLongitude(), other.getLongitude()) != 0) return false;
        final Object this$metroCode = this.getMetroCode();
        final Object other$metroCode = other.getMetroCode();
        if (this$metroCode == null ? other$metroCode != null : !this$metroCode.equals(other$metroCode)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $ip = this.getIp();
        result = result * PRIME + ($ip == null ? 43 : $ip.hashCode());
        final Object $countryCode = this.getCountryCode();
        result = result * PRIME + ($countryCode == null ? 43 : $countryCode.hashCode());
        final Object $countryName = this.getCountryName();
        result = result * PRIME + ($countryName == null ? 43 : $countryName.hashCode());
        final Object $city = this.getCity();
        result = result * PRIME + ($city == null ? 43 : $city.hashCode());
        final Object $zipCode = this.getZipCode();
        result = result * PRIME + ($zipCode == null ? 43 : $zipCode.hashCode());
        final Object $timeZone = this.getTimeZone();
        result = result * PRIME + ($timeZone == null ? 43 : $timeZone.hashCode());
        final long $latitude = Double.doubleToLongBits(this.getLatitude());
        result = result * PRIME + (int) ($latitude >>> 32 ^ $latitude);
        final long $longitude = Double.doubleToLongBits(this.getLongitude());
        result = result * PRIME + (int) ($longitude >>> 32 ^ $longitude);
        final Object $metroCode = this.getMetroCode();
        result = result * PRIME + ($metroCode == null ? 43 : $metroCode.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof IpGeoLocation;
    }

    public String toString() {
        return "org.helianto.ingress.domain.IpGeoLocation(ip=" + this.getIp() + ", countryCode=" + this.getCountryCode() + ", countryName=" + this.getCountryName() + ", city=" + this.getCity() + ", zipCode=" + this.getZipCode() + ", timeZone=" + this.getTimeZone() + ", latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ", metroCode=" + this.getMetroCode() + ")";
    }
}
