package ru.desided.voluum_combine.logic.add_offer.impl;

import ru.desided.voluum_combine.entity.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface AddOffersLogic {
    void login() throws IOException;
    Offer addOffer() throws IOException;
    Offer getCreo() throws IOException;
    void addVoluum() throws IOException;
    void propellerSmart() throws IOException;
    void propellerHigh() throws IOException;
    Offer setProp();


}
