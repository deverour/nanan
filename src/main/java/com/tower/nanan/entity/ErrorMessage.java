package com.tower.nanan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private StringBuilder message;
    private int index ;

    public void addMessage(String message){
        message ="  "+index+"."+message+"|";
        this.message = this.message.append(message);
        System.out.println(index+">>>>"+this.message);
        index++;

    }

}
