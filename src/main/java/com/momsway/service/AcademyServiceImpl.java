package com.momsway.service;

import com.momsway.domain.Academy;
import com.momsway.domain.User;
import com.momsway.dto.AcademyDTO;
import com.momsway.repository.academy.AcademyRepository;
import com.momsway.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AcademyServiceImpl implements AcademyService{
    private final AcademyRepository academyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public Page<AcademyDTO> findAcademyList(Pageable pageable, String search_txt) {
        long count = listCount();
        List<AcademyDTO> list = academyRepository.findList(pageable, search_txt);
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public long listCount() {
        return academyRepository.count();
    }

    @Override
    public AcademyDTO findByAid(Long aid) {
        return academyRepository.findByAid(aid);
    }

    @Override
    @Transactional
    public int delAcademy(Long aid, String saveFolder) {
        int result = 0;
        try{
            // aid 로 객체 불러와서
            AcademyDTO find = findByAid(aid);
            // imgPaths 를 리스트로 만든다음에
            List<String> imgPaths = Arrays.stream(find.getImgPath().split(";-;")).toList();
            // 이미지 파일 삭제 메서드
            academyImgFileRemove(imgPaths,saveFolder);
            academyRepository.deleteById(aid);
            result = 1;
        }catch (Exception e){

        }
        return result;
    }

    private void academyImgFileRemove(List<String> imgPaths, String saveFolder) {
        File file = null;
        for(String fname:imgPaths){
            file = new File(saveFolder + "/" + fname);
            file.delete();
        }
    }

    @Override
    @Transactional
    public Long insertAcademy(AcademyDTO dto, String saveFolder) {
        String imgPaths = academyImgFileUpload(dto.getFiles(),saveFolder);
        User user = userRepository.findByEmail(dto.getEmail());
        Academy newAcademy = Academy.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .readNo(0L)
                .createAt(LocalDateTime.now())
                .academyUser(user)
                .imgPath(imgPaths)
                .build();
        Academy academy = academyRepository.save(newAcademy);
        return academy.getAid();
    }

    private String academyImgFileUpload(List<MultipartFile> files, String saveFolder) {
        List<File> saveFile = new ArrayList<>();
        StringBuilder imgPaths = new StringBuilder();
        for(int i=0; i<files.size(); i++){
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+","%20");
            String filename = uuid+"_"+fname+";-;";
            saveFile.add(new File(saveFolder,filename));
            imgPaths.append(filename);
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).transferTo(saveFile.get(i));
            }
        }catch (IOException e){
            log.info("academyImgFileUpload exception...{}",e);
            for(int i=0; i<files.size(); i++){
                saveFile.get(i).delete();
            }
        }
        return imgPaths.toString();
    }
}
