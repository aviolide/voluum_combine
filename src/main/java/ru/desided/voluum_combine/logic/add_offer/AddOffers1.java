package ru.desided.voluum_combine.logic.add_offer;

import ru.desided.voluum_combine.entity.*;

import java.io.IOException;

public interface AddOffers1 {
    void setup();
    void login();
    Offer addOffer() throws IOException;
    Offer getCreo() throws IOException;
    void addVoluum() throws IOException;
    Offer setProp();


}
