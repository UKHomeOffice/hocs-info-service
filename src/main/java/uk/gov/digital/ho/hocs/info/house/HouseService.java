package uk.gov.digital.ho.hocs.info.house;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.House;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.IngestException;
import uk.gov.digital.ho.hocs.info.house.ingest.ListConsumerService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
@Slf4j
public class HouseService {

    private final HouseRepository houseRepository;

    private final ListConsumerService listConsumerService;

    @Autowired
    public HouseService(HouseRepository houseRepository, ListConsumerService listConsumerService) {
        this.houseRepository = houseRepository;
        this.listConsumerService = listConsumerService;
    }

    @CacheEvict(value = "members", allEntries = true)
    public void updateHouse(House newHouse) {
        if(newHouse != null) {
            House jpaHouse = houseRepository.findOneByName(newHouse.getName());

            // Update existing house
            if (jpaHouse != null) {
                Set<Member> newMembers = newHouse.getMembers();
                Set<Member> jpaMembers = jpaHouse.getMembers();

                // Update existing members
                jpaMembers.forEach(item -> {
                    item.setDeleted(!newMembers.contains(item));
                });

                // Add new members
                newMembers.forEach(newMember -> {
                    if (!jpaMembers.contains(newMember)) {
                        jpaMembers.add(newMember);
                    }
                });

                jpaHouse.setMembers(jpaMembers);

                // Set the house to deleted if there are no visible members
                jpaHouse.setDeleted(jpaHouse.getMembers().stream().allMatch(Member::getDeleted));
            } else {
                jpaHouse = newHouse;
            }

            saveMembers(jpaHouse);
//        } else{
//            throw new EntityCreationException("Unable to update entity");
        }
    }

    @PostConstruct
    @CacheEvict(value = "members", allEntries = true)
    public void updateWebMemberLists() throws IngestException {
        updateHouse(listConsumerService.createFromWelshAssemblyAPI());
        updateHouse(listConsumerService.createFromEuropeanParliamentAPI());
        updateHouse(listConsumerService.createFromIrishAssemblyAPI());
        updateHouse(listConsumerService.createFromScottishParliamentAPI());
        updateHouse(listConsumerService.createCommonsFromUKParliamentAPI());
        updateHouse(listConsumerService.createLordsFromUKParliamentAPI());
    }

    private void saveMembers(House house) {
        try {
            if(house != null && house.getName() != null) {
                houseRepository.save(house);
            }
        } catch (DataIntegrityViolationException e) {

//            if (e.getCause() instanceof ConstraintViolationException &&
//                    ((ConstraintViolationException) e.getCause()).getConstraintName().toLowerCase().contains("house_name_idempotent") ||
//                    ((ConstraintViolationException) e.getCause()).getConstraintName().toLowerCase().contains("member_name_ref_idempotent")) {
//                throw new EntityCreationException("Identified an attempt to recreate existing entity, rolling back");
//            }

            throw e;
        }

    }

}