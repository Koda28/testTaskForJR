package com.game.dto;

import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Calendar;
import java.util.Date;

public class PlayerDto {
    private String name;

    private String title;

    private Race race;

    private Profession profession;

    private Date birthday;

    private Boolean banned;

    private Integer experience;

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    private Integer untilNextLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }


    public boolean isAllFieldsNotNull() {
        return name != null && title != null && experience != null
                && race != null && profession != null && birthday != null;
    }

    public boolean isNameAndTitleValid() {
        return name.length() <= 12 && title.length() <= 30 && !name.equals("");
    }

    public boolean isExperienceValid() {
        return experience >= 0 && experience <= 10000000;
    }

    public boolean isBirthdayValid() {
        return birthday.getTime() >= 0 && birthday.getYear() >= 100 && birthday.getYear() <= 1100;
    }
}
