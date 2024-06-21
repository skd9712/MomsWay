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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntExamServiceImpl implements EntExamService{
    private final EntExamRepository entExamRepository;
    private final ModelMapper modelMapper;

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
                .readNo(0L)
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

    @Override
    public EntExamDTO findByEid(Long eid) {
        EntExamDTO detail = entExamRepository.findByEid(eid);

        return EntExamDTO.builder()
                .eid(detail.getEid())
                .title(detail.getTitle())
                .content(detail.getContent())
                .readNo(detail.getReadNo())
                .createAt(detail.getCreateAt())
                .imgPath(detail.getImgPath())
                .nickname(detail.getNickname())
                .build();
    }

    private List<String> fileUpload(String saveFolder, List<MultipartFile> files) {
        List<File> saveFile = new ArrayList<>();
        List<String> saveFileNames = new ArrayList<>();
        for(int i =0;i< files.size();i++){
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+","%20");
            String filename = uuid+"_"+fname;
            saveFile.add(new File(saveFolder,filename));
            saveFileNames.add(filename);
        }
        try {
            for(int i =0;i< files.size();i++){
                files.get(i).transferTo(saveFile.get(i));
            }
        }catch (IOException e){
            System.out.println(e);
            for (int i =0;i<files.size();i++)
                saveFile.get(i).delete();
        }
        return saveFileNames;

    }


}
