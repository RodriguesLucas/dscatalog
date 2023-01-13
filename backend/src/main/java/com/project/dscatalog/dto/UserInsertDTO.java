package com.project.dscatalog.dto;

import com.project.dscatalog.entities.User;

public class UserInsertDTO extends UserDTO {

    private String passWord;

    public UserInsertDTO(String passWord) {
        this.passWord = passWord;
    }

    public UserInsertDTO(Long id, String firstName, String lastName, String email, String passWord) {
        super(id, firstName, lastName, email);
        this.passWord = passWord;
    }

    public UserInsertDTO(User user, String passWord) {
        super(user);
        this.passWord = passWord;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
