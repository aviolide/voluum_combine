package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "traffic_source", schema = "combine", catalog = "")
public class TrafficSource {
    @JsonProperty(value = "id")
    private int trafficSourceId;
    @JsonProperty(value = "id_voluum")
    private String idVoluum;
    @JsonProperty(value = "name_voluum")
    private String nameVoluum;
    private String login;
    private String password;
    private User user;

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
    @Column(name = "traffic_source_id")
    public int getTrafficSourceId() {
        return trafficSourceId;
    }

    public void setTrafficSourceId(int trafficSourceId) {
        this.trafficSourceId = trafficSourceId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficSource that = (TrafficSource) o;
        return trafficSourceId == that.trafficSourceId &&
                Objects.equals(idVoluum, that.idVoluum) &&
                Objects.equals(nameVoluum, that.nameVoluum) &&
                Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(trafficSourceId, idVoluum, nameVoluum, login, password);
    }
}
