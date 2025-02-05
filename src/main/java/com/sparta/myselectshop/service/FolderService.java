package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public void addFolders(List<String> folderNames, User user) {
        List<Folder> folderList = folderNames.stream()
            .map(name -> {
                if (folderRepository.existsByUserAndName(user, name)) {
                    throw new IllegalArgumentException("폴더명이 중복되었습니다.");
                }
                return new Folder(name, user);
            })
            .collect(Collectors.toList());

        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        return folderRepository.findAllByUser(user).stream().map(FolderResponseDto::new).collect(Collectors.toList());
    }

    private boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            return folderName.equals(existFolder.getName());
        }
        return false;
    }

}
