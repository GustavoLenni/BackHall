package com.example.Back_Hall.model;

import com.example.Back_Hall.model.enums.Type;
import jakarta.persistence.*;

@Entity
@Table(name = "finances")
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String product;
    private Double value;
    private Integer amount;


    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Finance(){

    }
    public Finance(String product,Double value,Integer amount,Type type, User user){
        this.product = product;
        this.value = value;
        this.amount = amount;
        this.type = type;
        this.user = user;
    }

    public String getProduct(){
        return product;
    }
    public Double getValue(){
        return value;
    }
    public Integer getAmount(){
        return amount;
    }
    public Type getType(){
        return type;
    }
    public User getUser(){
        return user;
    }
    public Integer getId(){
        return id;
    }



    public void setProduct(String product){
        this.product = product;
    }
    public void setValue(Double value){
        this.value = value;
    }
    public void setAmount(Integer amount){
        this.amount = amount;
    }
    public void setType(Type type){
        this.type = type;
    }
    public void setUser(User user){
        this.user = user;
    }
}
