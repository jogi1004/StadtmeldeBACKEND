package com.stadtmeldeapp.service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.IconDTO;
import com.stadtmeldeapp.DTO.MainCategoryDTO;
import com.stadtmeldeapp.Entity.IconEntity;
import com.stadtmeldeapp.Repository.IconRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class IconService {

    @Autowired
    private IconRepository iconRepository;

    @Autowired
    private CategoryService categoryService;

    public IconEntity findIconById(int id) throws NotFoundException {
        return iconRepository.findById(id).orElseThrow(() -> new NotFoundException("Icon nicht gefunden"));
    }

    public List<IconDTO> findIconByLocationName(String locationName) {
        List<MainCategoryDTO> maincats = categoryService.getMaincategoriesByLocationName(locationName);
        Set<Integer> iconIds = new HashSet<>();
        for (MainCategoryDTO maincat : maincats) {
            if (maincat.iconId() != null) {
                iconIds.add(maincat.iconId());
            }
        }

        List<IconDTO> entites = new ArrayList<>();
        for (Integer iconId : iconIds) {
            Optional<IconEntity> iconEnt = iconRepository.findById(iconId);
            if (iconEnt.isPresent()) {
                IconEntity ic = iconEnt.get();
                entites.add(new IconDTO(ic.getId(), ic.getIcon()));
            }
        }
        return entites;
    }
}