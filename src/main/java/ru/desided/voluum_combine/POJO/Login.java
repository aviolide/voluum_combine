package ru.desided.voluum_combine.POJO;

public class Login {

//    @Size(min = 30, message = "please enter right api ID")
    private String apiId;
//    @Size(min = 30, message = "please enter right api KEY")
    private String apiKey;
//    @Size(min = 30, message = "please enter right api KEY")
//    @NotNull(message = "empty formula")
    private String formula;
    private String propeller;
    private Integer month;
    private Integer days;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getPropeller() {
        return propeller;
    }

    public void setPropeller(String propeller) {
        this.propeller = propeller;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
