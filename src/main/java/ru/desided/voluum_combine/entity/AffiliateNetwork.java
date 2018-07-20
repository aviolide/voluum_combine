package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "affiliate_network", schema = "combine", catalog = "")
public class AffiliateNetwork {
    @JsonProperty("id")
    private int affiliateNetworkId;
    @JsonProperty("id_voluum")
    private String idVoluum;
    @JsonProperty("name_voluum")
    private String nameVoluum;
    private String login;
    private String password;
    @JsonProperty("domain_mob")
    private String domainMob;
    @JsonProperty("domain_web")
    private String domainWeb;
    @JsonProperty("cd_affiliate_id")
    private String affiliateId;
    @JsonProperty("cd_api_key")
    private String apiKey;
    private User user;
    @JsonProperty("proxy_host")
    private String proxyHost;
    @JsonProperty("proxy_port")
    private Integer proxyPort;
    @JsonProperty("proxy_user")
    private String proxyUser;
    @JsonProperty("proxy_password")
    private String proxyPassword;

    @ManyToOne
    @JoinColumn(name = "nick")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "affiliate_network_id")
    public int getAffiliateNetworkId() {
        return affiliateNetworkId;
    }

    public void setAffiliateNetworkId(int affiliateNetworkId) {
        this.affiliateNetworkId = affiliateNetworkId;
    }

    @Basic
    @Column(name = "id_voluum")
    public String getIdVoluum() {
        return idVoluum;
    }

    public void setIdVoluum(String idVoluum) {
        this.idVoluum = idVoluum;
    }

    @Basic
    @Column(name = "name_voluum")
    public String getNameVoluum() {
        return nameVoluum;
    }

    public void setNameVoluum(String nameVoluum) {
        this.nameVoluum = nameVoluum;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "domain_mob")
    public String getDomainMob() {
        return domainMob;
    }

    public void setDomainMob(String domainMob) {
        this.domainMob = domainMob;
    }

    @Basic
    @Column(name = "domain_web")
    public String getDomainWeb() {
        return domainWeb;
    }

    public void setDomainWeb(String domainWeb) {
        this.domainWeb = domainWeb;
    }

    @Basic
    @Column(name = "affiliate_id")
    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    @Basic
    @Column(name = "api_key")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffiliateNetwork that = (AffiliateNetwork) o;
        return affiliateNetworkId == that.affiliateNetworkId &&
                Objects.equals(idVoluum, that.idVoluum) &&
                Objects.equals(nameVoluum, that.nameVoluum) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(domainMob, that.domainMob) &&
                Objects.equals(domainWeb, that.domainWeb) &&
                Objects.equals(affiliateId, that.affiliateId) &&
                Objects.equals(apiKey, that.apiKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(affiliateNetworkId, idVoluum, nameVoluum, login, password, domainMob, domainWeb, affiliateId, apiKey);
    }

    @Basic
    @Column(name = "proxy_host")
    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    @Basic
    @Column(name = "proxy_port")
    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Basic
    @Column(name = "proxy_user")
    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    @Basic
    @Column(name = "proxy_password")
    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}
