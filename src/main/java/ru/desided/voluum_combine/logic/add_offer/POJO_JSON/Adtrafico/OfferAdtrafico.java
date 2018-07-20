package ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico;

import ru.desided.voluum_combine.logic.add_offer.MainPropsJson;

public class OfferAdtrafico extends Data implements MainPropsJson {

    private String id;
    private String name;
    private String status;
    private String require_approval;
    private String approval_status;
    private String default_payout;
    private String country_code;
    private String country_name;
    private String type_traffic;
    private String land_id;
    private String link;

    @Override
    public String getOfferId() {
        return id;
    }

    @Override
    public String getLandId() {
        return land_id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTypeTraffic() {
        return type_traffic;
    }

    @Override
    public String getCountryCode() {
        return country_code;
    }

    @Override
    public String getCountryName() {
        return country_name;
    }

    @Override
    public String getPayout() {
        return default_payout;
    }

    @Override
    public String getLink() {
        return link;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequire_approval(String require_approval) {
        this.require_approval = require_approval;
    }

    public void setApproval_status(String approval_status) {
        this.approval_status = approval_status;
    }

    public void setDefault_payout(String default_payout) {
        this.default_payout = default_payout;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public void setType_traffic(String type_traffic) {
        this.type_traffic = type_traffic;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getApproval_status() {
        return approval_status;
    }
}
