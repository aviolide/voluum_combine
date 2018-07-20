package ru.desided.voluum_combine.logic.setCloak;

import ru.desided.voluum_combine.entity.Campaign;
import ru.desided.voluum_combine.entity.User;

import java.io.IOException;
import java.util.List;

public interface SetupCloak {
    void propellerAuth() throws IOException;
    void parsingCampaigns() throws IOException, InterruptedException;
//    List<Campaign> getDataFromPropeller() throws IOException;
//    List<Campaign> updateCompaign(List<Campaign> updateList) throws IOException;
    void setupVoluum(User user, String shortId) throws IOException;
}
