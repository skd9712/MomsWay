package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.dto.EntExamDTO;
import com.momsway.repository.entexam.EntExamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntExamServiceImpl implements EntExamService{
    private final EntExamRepository entExamRepository;
    private final ModelMapper modelMapper;
//    @Override
//    public List<EntExamDTO> entlist() {
//
//        List<EntExam> entList = entExamRepository.orderlist();
//        List<EntExamDTO> entDTOList = entList.stream().map(item ->
//                        modelMapper.map(item, EntExamDTO.class))
//                .collect(Collectors.toList());
//        return entDTOList;
//    }

    @Override
    public Page<EntExamDTO> entlist(Pageable pageable) {
        Page<EntExamDTO> orderlist = entExamRepository.orderlist(pageable);
        return orderlist;
    }

    @Override
    public long upload(String saveFolder, EntExamDTO dto) {
        EntExam entity = EntExam.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imgPath(dto.getImgPath())
                .build();
        EntExam savedEntity = entExamRepository.save(entity);
        List<String> fnames = new ArrayList<>();
        if(dto.getFiles()!=null && dto.getFiles().size()!=0){
            fnames = fileUpload(saveFolder,dto.getFiles());
            if(!fnames.isEmpty()){
                savedEntity.setImgPath(fnames.get(0));
            }
            for(String item:fnames)
                log.info("....items..{}",item);
        }
        entExamRepository.save(savedEntity);

        return fnames.size();
    }

    private List<String> fileUpload(String saveFolder, List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        for(MultipartFile file: files){
            if(!file.isEmpty()){
                String fileName = file.getOriginalFilename();
                File dest = new File(saveFolder,fileName);
                try {
                    file.transferTo(dest);
                    fileNames.add(dest.getAbsolutePath());
                }catch (IOException e){
                    System.out.println(e);
                }
            }
        }
        return fileNames;
    }


}
