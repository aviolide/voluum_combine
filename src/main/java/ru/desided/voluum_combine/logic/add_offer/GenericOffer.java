package ru.desided.voluum_combine.logic.add_offer;

import ru.desided.voluum_combine.entity.Offer;
import ru.desided.voluum_combine.logic.add_offer.POJO_JSON.Adtrafico.OfferAdtrafico;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericOffer<T> {
    T obj;
    public Offer compare(Offer offer, T clazz) throws NullPointerException{
        Method method = null;
        try {
            if (offer.getName() == null) {
                method = clazz.getClass().getDeclaredMethod("getName");
                offer.setName((String) method.invoke(clazz));
            }
            if (offer.getOfferId() == null) {
                method = clazz.getClass().getDeclaredMethod("getOfferId");
                offer.setOfferId((String) method.invoke(clazz));
            }
            if (offer.getLandId() == null) {
                method = clazz.getClass().getDeclaredMethod("getLandId");
                offer.setLandId((String) method.invoke(clazz));
            }
            if (offer.getStatus() == null) {
                method = clazz.getClass().getDeclaredMethod("getStatus");
                offer.setStatus((String) method.invoke(clazz));
            }
            if (offer.getTypeTraffic() == null) {
                method = clazz.getClass().getDeclaredMethod("getTypeTraffic");
                offer.setTypeTraffic((String) method.invoke(clazz));
            }
            if (offer.getCountryCode() == null) {
                method = clazz.getClass().getDeclaredMethod("getCountryCode");
                offer.setCountryCode((String) method.invoke(clazz));
            }
            if (offer.getCountryName() == null) {
                method = clazz.getClass().getDeclaredMethod("getCountryName");
                offer.setCountryName((String)method.invoke(clazz));
            }
            if (offer.getPayoutConverted() == null) {
                method = clazz.getClass().getDeclaredMethod("getPayout");
                offer.setPayoutConverted((String) method.invoke(clazz));
            }
            if (offer.getLink() == null) {
                method = clazz.getClass().getDeclaredMethod("getLink");
                offer.setLink((String) method.invoke(clazz));
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
//        } catch (NullPointerException e){
//            e.printStackTrace();
        }
        return offer;
    };
}
