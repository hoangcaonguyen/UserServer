package com.example.userserver.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private String itemName;
    private String type;

    private String itemData;

    private int folderId;


    private int ownerId;

    private int status;

    public  long upDateTime;
}
