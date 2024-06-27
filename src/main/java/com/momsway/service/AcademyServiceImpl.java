package com.momsway.service;

import com.momsway.domain.Academy;
import com.momsway.domain.User;
import com.momsway.dto.AcademyDTO;
import com.momsway.dto.NoticeDTO;
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
import java.util.*;


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
    public void addAcademyReadNo(Long aid) {
        Optional<Academy> find = academyRepository.findById(aid);
        Academy academy = find.orElseThrow(() -> {
            throw new RuntimeException(" from addAcademyReadNo ");
        });
        academy.setReadNo(academy.getReadNo()+1);
    }

    @Override
    @Transactional
    public int delAcademy(Long aid, String saveFolder) {
        int result = 0;
        try{
            AcademyDTO find = findByAid(aid);
            if(find.getImgPath()!=null){
                List<String> imgPaths = Arrays.stream(find.getImgPath().split(";-;")).toList();
                academyImgFileRemove(imgPaths,saveFolder);
            }
            academyRepository.deleteById(aid);
            result = 1;
        }catch (Exception e){
            log.error("delAcademy...{}",e);
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
        String imgPaths = null;
        if(!dto.getFiles().get(0).getOriginalFilename().equals(""))
            imgPaths = academyImgFileUpload(dto.getFiles(),saveFolder);
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

    @Override
    @Transactional
    public Long updateAcademy(AcademyDTO dto, String saveFolder) {
        // find academy entity
        Optional<Academy> find = academyRepository.findById(dto.getAid());
        Academy academy = find.orElseThrow(() -> {
            throw new RuntimeException("from AcademyServiceImpl updateAcademy");
        });
        StringBuilder updateImgPaths = new StringBuilder();
        // 기존 이미지가 있는 경우
        if(academy.getImgPath()!=null){
            // original imgList
            List<String> originPaths = Arrays.stream(academy.getImgPath().split(";-;")).toList();
            // dto.imgPaths file delete
            if(dto.getImgPaths()!=null && dto.getImgPaths().size()>0){
                academyImgFileRemove(dto.getImgPaths(),saveFolder);
            }
            // updateImgPaths 에 기존 이미지는 넣고 삭제할 이미지는 빼기
            updateImgPaths = updateImgList(updateImgPaths, originPaths, dto.getImgPaths());
            log.info("updateImgPaths after del...{}",updateImgPaths);
        }
        // 새롭게 추가된 이미지 insert
        String addPaths = "";
        if(dto.getFiles()!=null && !dto.getFiles().get(0).getOriginalFilename().equals("")){
            addPaths = academyImgFileUpload(dto.getFiles(), saveFolder);
        }
        updateImgPaths.append(addPaths);
        log.info("updateImgPaths after add...{}",updateImgPaths);
        // set entity
        academy.setTitle(dto.getTitle());
        academy.setContent(dto.getContent());
        System.out.println(updateImgPaths.length());
        if(updateImgPaths==null || updateImgPaths.length()==0)
            academy.setImgPath(null);
        else
            academy.setImgPath(updateImgPaths.toString());
        return academy.getAid();
    }

    private StringBuilder updateImgList(StringBuilder updateImgPaths
            , List<String> originPaths, List<String> delPaths){
        if(delPaths==null)
            delPaths = new ArrayList<>();
        //System.out.println(originPaths.size()+","+delPaths.size());
        boolean isSearchAll = false;
        for(int i=0; i<originPaths.size(); i++){
            int j=0;
            while(j<=delPaths.size()){
                if(j==delPaths.size()){
                    isSearchAll = true;
                    break;
                }
                if(originPaths.get(i).equals(delPaths.get(j)))
                    break;
                j++;
            }
            if(isSearchAll){
                updateImgPaths.append(originPaths.get(i));
                updateImgPaths.append(";-;");
                isSearchAll = false;
            }
        }
        System.out.println(updateImgPaths);
        return updateImgPaths;
    }

    private String academyImgFileUpload(List<MultipartFile> files, String saveFolder) {
        List<File> saveFile = new ArrayList<>();
        StringBuilder imgPaths = new StringBuilder();
        for(int i=0; i<files.size(); i++){
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+","%20");
            String filename = uuid+"_"+fname;
            saveFile.add(new File(saveFolder,filename));
            imgPaths.append(filename);
            imgPaths.append(";-;");
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
