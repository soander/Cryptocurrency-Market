package com.demo.enums;

public enum OrderDirection {

    BUY(1,"Buy") ,
    SELL(2,"Sell") ;

    private int code;

    private String desc;

    OrderDirection(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OrderDirection getOrderDirection(int code){
        OrderDirection[] values = OrderDirection.values();
        for (OrderDirection value : values) {
            if(value.getCode()==code) {
                return value;
            }
        }
        return null;
    }
}
