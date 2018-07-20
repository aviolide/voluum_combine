package ru.desided.voluum_combine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @JsonProperty(value = "name")
    private String nick;
    private String email;
    private String login;
    private String chatId;
    @JsonProperty(value = "voluum_login")
    private String voluumLogin;
    @JsonProperty(value = "voluum_password")
    private String voluumPassword;
    @JsonProperty(value = "voluum_access_id")
    private String voluumAccessId;
    @JsonProperty(value = "voluum_access_key")
    private String voluumAccessKey;
    @JsonProperty(value = "voluum_client_id")
    private String voluumClientId;
    @JsonProperty(value = "workspace")
    private String workspace;
    @JsonProperty(value = "workspace_id")
    private String workspaceId;
    @JsonProperty(value = "offer_count")
    private Integer offerCount;
    private String cloakConst;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "nick")
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "chat_id")
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Basic
    @Column(name = "voluum_login")
    public String getVoluumLogin() {
        return voluumLogin;
    }

    public void setVoluumLogin(String voluumLogin) {
        this.voluumLogin = voluumLogin;
    }

    @Basic
    @Column(name = "voluum_password")
    public String getVoluumPassword() {
        return voluumPassword;
    }

    public void setVoluumPassword(String voluumPassword) {
        this.voluumPassword = voluumPassword;
    }

    @Basic
    @Column(name = "voluum_access_id")
    public String getVoluumAccessId() {
        return voluumAccessId;
    }

    public void setVoluumAccessId(String voluumAccessId) {
        this.voluumAccessId = voluumAccessId;
    }

    @Basic
    @Column(name = "voluum_access_key")
    public String getVoluumAccessKey() {
        return voluumAccessKey;
    }

    public void setVoluumAccessKey(String voluumAccessKey) {
        this.voluumAccessKey = voluumAccessKey;
    }

    @Basic
    @Column(name = "voluum_client_id")
    public String getVoluumClientId() {
        return voluumClientId;
    }

    public void setVoluumClientId(String voluumClientId) {
        this.voluumClientId = voluumClientId;
    }

    @Basic
    @Column(name = "workspace")
    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    @Basic
    @Column(name = "workspace_id")
    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    @Basic
    @Column(name = "offer_count")
    public Integer getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(Integer offerCount) {
        this.offerCount = offerCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nick, user.nick) &&
                Objects.equals(email, user.email) &&
                Objects.equals(login, user.login) &&
                Objects.equals(chatId, user.chatId) &&
                Objects.equals(voluumLogin, user.voluumLogin) &&
                Objects.equals(voluumPassword, user.voluumPassword) &&
                Objects.equals(voluumAccessId, user.voluumAccessId) &&
                Objects.equals(voluumAccessKey, user.voluumAccessKey) &&
                Objects.equals(voluumClientId, user.voluumClientId) &&
                Objects.equals(workspace, user.workspace) &&
                Objects.equals(workspaceId, user.workspaceId) &&
                Objects.equals(offerCount, user.offerCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nick, email, login, chatId, voluumLogin, voluumPassword, voluumAccessId, voluumAccessKey, voluumClientId, workspace, workspaceId, offerCount);
    }

    @Basic
    @Column(name = "cloak_const")
    public String getCloakConst() {
        return cloakConst;
    }

    public void setCloakConst(String cloakConst) {
        this.cloakConst = cloakConst;
    }
}
