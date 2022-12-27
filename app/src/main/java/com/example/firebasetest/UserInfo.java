package com.example.firebasetest;

public class UserInfo {

    private String Name;
    private String Phone_Number;
    private String Age;
    private String Email;

    public UserInfo(){}

    public UserInfo(String name, String phone_Number, String age, String email) {
        Name = name;
        Phone_Number = phone_Number;
        Age = age;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
