package ru.desided.voluum_combine.logic.monitoring;

import ru.desided.voluum_combine.entity.User;

import java.io.IOException;

public interface StartMonitoring {
    void propellerAuth() throws IOException;
    void monitoring(User user) throws IOException, InterruptedException;
    void stopMonitoring();
}
