package com.dormitory.entity;

public class StudentInfo {
    private Long id;
    private Long userId;
    private String building;
    private String room;
    private Integer isBound;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getIsBound() {
        return isBound;
    }

    public void setIsBound(Integer isBound) {
        this.isBound = isBound;
    }

    public String getFullDormitory() {
        return building + "-" + room;
    }
}
