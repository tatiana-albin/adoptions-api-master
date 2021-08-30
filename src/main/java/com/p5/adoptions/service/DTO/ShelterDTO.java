package com.p5.adoptions.service.DTO;


import java.util.ArrayList;
import java.util.List;

public class ShelterDTO {
    private Integer id;

    private String name;

    private String location;

    private List<CatDTO> cats = new ArrayList<>();
    private List<DogDTO> dogs = new ArrayList<>();

    public ShelterDTO() {
    }

    public ShelterDTO(Integer id, String name, String location, List<CatDTO> cats, List<DogDTO> dogs) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cats = cats;
        this.dogs = dogs;
    }

    public Integer getId() {
        return id;
    }

    public ShelterDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ShelterDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ShelterDTO setLocation(String location) {
        this.location = location;
        return this;
    }

    public List<CatDTO> getCats() {
        return cats;
    }

    public ShelterDTO setCats(List<CatDTO> cats) {
        this.cats = cats;
        return this;
    }

    public List<DogDTO> getDogs() {
        return dogs;
    }

    public ShelterDTO setDogs(List<DogDTO> dogs) {
        this.dogs = dogs;
        return this;
    }
}
