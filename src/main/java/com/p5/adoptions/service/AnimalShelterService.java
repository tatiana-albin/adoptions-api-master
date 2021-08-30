package com.p5.adoptions.service;

import com.p5.adoptions.repository.animal.Animal;
import com.p5.adoptions.repository.animal.AnimalRepository;
import com.p5.adoptions.repository.cats.Cat;
import com.p5.adoptions.repository.dogs.Dog;
import com.p5.adoptions.repository.shelters.AnimalShelter;
import com.p5.adoptions.repository.shelters.AnimalShelterRepository;
import com.p5.adoptions.service.DTO.CatDTO;
import com.p5.adoptions.service.DTO.DogDTO;
import com.p5.adoptions.service.DTO.ListDTO;
import com.p5.adoptions.service.DTO.ShelterDTO;
import com.p5.adoptions.service.adapters.CatAdapter;
import com.p5.adoptions.service.adapters.DogAdapter;
import com.p5.adoptions.service.adapters.ShelterAdapter;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.boot.origin.TextResourceOrigin;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalShelterService {
    private final AnimalShelterRepository animalShelterRepository;

    public AnimalShelterService(AnimalShelterRepository animalShelterRepository) {
        this.animalShelterRepository = animalShelterRepository;
    }

    public ListDTO<ShelterDTO> findAll() {
        List<ShelterDTO> data =
                ShelterAdapter.toDTOList(animalShelterRepository.findAll());

        Long totalCount = animalShelterRepository.count();
        ListDTO<ShelterDTO> response = new ListDTO<>();
        response.setData(data);
        response.setTotalCount(totalCount);
        return response;
    }

    public ShelterDTO createShelter(ShelterDTO animalShelter) {
        AnimalShelter shelter = ShelterAdapter.fromDTO(animalShelter);

        AnimalShelter savedShelter = animalShelterRepository.save(shelter);

        return ShelterAdapter.toDTO(savedShelter);
    }

    public ShelterDTO updateShelter(Integer id, ShelterDTO animalShelter) {
        AnimalShelter shelter = getShelterById(id);
        if (!shelter.getId().equals(animalShelter.getId())) {
            throw new RuntimeException("Id of entity not the same with path id");
        }

        return ShelterAdapter
                .toDTO(animalShelterRepository
                        .save(ShelterAdapter.fromDTO(animalShelter)));
    }

    public ShelterDTO findById(Integer id) {
        AnimalShelter shelter = getShelterById(id);
        return ShelterAdapter.toDTO(shelter);
    }

    public void deleteShelter(Integer id) {
        animalShelterRepository.deleteById(id);
    }

    public ListDTO<CatDTO> findAllCatsByShelter(Integer shelterId) {
        AnimalShelter shelter = getShelterById(shelterId);
        List<CatDTO> data =
                CatAdapter.toDTOList(shelter.getCats());
        Long totalCount = (long) data.size();
        ListDTO<CatDTO> response = new ListDTO<>();
        response.setData(data);
        response.setTotalCount(totalCount);
        return response;
    }

    public ListDTO<CatDTO> addNewCatToShelter(Integer shelterId, CatDTO cat) {
        AnimalShelter shelter = getShelterById(shelterId);

        shelter.getCats().add(CatAdapter.fromDTO(cat));

        animalShelterRepository.save(shelter);
        List<CatDTO> data =
                CatAdapter.toDTOList(shelter.getCats());
        Long totalCount = (long) data.size();
        ListDTO<CatDTO> response = new ListDTO<>();
        response.setData(data);
        response.setTotalCount(totalCount);
        return response;
    }

    public CatDTO updateCatInShelter(Integer shelterId, Integer catId, CatDTO cat) {
        AnimalShelter shelter = getShelterById(shelterId);
        Cat newCat = CatAdapter.fromDTO(cat);
        boolean exists = shelter.getCats().stream().anyMatch(c -> c.getId().equals(catId));
        if(!exists){
            throw new RuntimeException("Cat with id " + catId + " not found.");
        }
        List<Cat> newCats = shelter.getCats().stream().map(c -> {
            if (c.getId().equals(catId)) {

                newCat.setId(catId);
                return newCat;
            }
            return c;
        }).collect(Collectors.toList());

        shelter.setCats(newCats);

        animalShelterRepository.save(shelter);

        return CatAdapter.toDTO(newCat);
    }

    public void deleteCatInShelter(Integer shelterId, Integer catId) {
        AnimalShelter shelter = getShelterById(shelterId);
        List<Cat> newCats = shelter.getCats().stream().filter(c -> !c.getId().equals(catId)).collect(Collectors.toList());
        shelter.setCats(newCats);
        animalShelterRepository.save(shelter);
    }

    public ListDTO<DogDTO> findAllDogsByShelter(Integer shelterId) {
        AnimalShelter shelter = getShelterById(shelterId);
        List<DogDTO> data =
                DogAdapter.toDTOList(shelter.getDogs());
        Long totalCount = (long) data.size();
        ListDTO<DogDTO> response = new ListDTO<>();
        response.setData(data);
        response.setTotalCount(totalCount);
        return response;
    }

    private AnimalShelter getShelterById(Integer id) {
        Optional<AnimalShelter> optional = animalShelterRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Shelter with id " + id + " not found"));
    }

    public ListDTO<DogDTO> addNewDogToShelter(Integer shelterId, DogDTO dogDTO) {
        AnimalShelter shelter = getShelterById(shelterId);

        shelter.getDogs().add(DogAdapter.fromDTO(dogDTO));

        animalShelterRepository.save(shelter);
        List<DogDTO> data =
                DogAdapter.toDTOList(shelter.getDogs());
        Long totalCount = (long) data.size();
        ListDTO<DogDTO> response = new ListDTO<>();
        response.setData(data);
        response.setTotalCount(totalCount);
        return response;
    }

    public DogDTO updateDogInShelter(Integer shelterId, Integer dogId, DogDTO dogDTO) {
        AnimalShelter shelter = getShelterById(shelterId);
        Dog newDog = DogAdapter.fromDTO(dogDTO);
        boolean exists = shelter.getDogs().stream().anyMatch(d -> d.getId().equals(dogId));
        if(!exists){
            throw new RuntimeException("Dog with id " + dogId + " not found.");
        }
        List<Dog> newDogs = shelter.getDogs().stream().map(d -> {
            if (d.getId().equals(dogId)) {

                newDog.setId(dogId);
                return newDog;
            }
            return d;
        }).collect(Collectors.toList());

        shelter.setDogs(newDogs);

        animalShelterRepository.save(shelter);

        return DogAdapter.toDTO(newDog);

    }

    public void deleteDogInShelter(Integer shelterId, Integer dogId) {
        AnimalShelter shelter = getShelterById(shelterId);

        boolean removed = shelter.getDogs().removeIf(d -> d.getId().equals(dogId));

        animalShelterRepository.save(shelter);

        if(!removed) {
            throw new RuntimeException("Already deleted or entity missing");
        }
    }
}
