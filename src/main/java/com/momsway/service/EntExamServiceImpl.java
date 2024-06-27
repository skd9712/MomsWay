package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntExamServiceImpl implements EntExamService {

    @Value("D:\\uploadImg")
    private String saveFolder;

    @Value("D:\\uploadImg")
    private String filePath;
    private final EntExamRepository entExamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(EntExamService.class);

    @Override
    public Page<EntExamDTO> entlist(Pageable pageable, String search_txt) {
        Page<EntExamDTO> orderlist = entExamRepository.orderlist(pageable, search_txt);
        return orderlist;
    }

    @Override
    public long upload(String saveFolder, EntExamDTO dto, String username) {
        String imgPaths = null;
        if (!dto.getFiles().get(0).getOriginalFilename().equals(""))
            imgPaths = fileUpload(saveFolder, dto.getFiles());
        User user = userRepository.findByEmail(username);
        EntExam entity = EntExam.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imgPath(imgPaths)
                .entExamUser(user)
                .readNo(0L)
                .build();
        EntExam savedEntity = entExamRepository.save(entity);
        log.info("service..uid...{}", dto.getUid());
//        String fnames = null;
//        if (dto.getFiles() != null && dto.getFiles().size() != 0) {
//            fnames = fileUpload(saveFolder, dto.getFiles());
//            if (!fnames.isEmpty()) {
//                savedEntity.setImgPath(fnames);
//            }
//
//        }
        entExamRepository.save(savedEntity);

        return savedEntity.getEid();
    }

    @Override
    public EntExamDTO findByEid(Long eid) {
        EntExamDTO detail = entExamRepository.findByEid(eid);
//조회수 증가
        entExamRepository.incrementReadNo(eid);
        return EntExamDTO.builder()
                .eid(detail.getEid())
                .title(detail.getTitle())
                .content(detail.getContent())
                .readNo(detail.getReadNo() + 1)
                .createAt(detail.getCreateAt())
                .imgPath(detail.getImgPath())
                .uid(detail.getUid())
                .email(detail.getEmail())
                .nickname(detail.getNickname())
                .build();
    }

    @Override
    public int delEnt(Long eid) {
        int result = 0;
        try {
            EntExamDTO entExamDTO = entExamRepository.findByEid(eid);
            if (entExamDTO != null) {
                String imgPath = entExamDTO.getImgPath();
                log.info("해당파일을 삭제합니다: {}", imgPath);
                if (imgPath != null && !imgPath.isEmpty()) {
                    File file = new File(saveFolder, imgPath);
                    log.info("절대 경로: {}", file.getAbsolutePath());
                    if (file.exists()) {
                        if (file.delete()) {
                            log.info("파일이 성공적으로 삭제되었습니다: {}", file.getAbsolutePath());
                        } else {
                            log.warn("파일삭제 실패: {}", file.getAbsolutePath());
                        }
                    } else {
                        log.info("파일이 존재하지 않습니다: {}", file.getAbsolutePath());
                    }

                }
                entExamRepository.deleteById(eid);
                result = 1;
            } else {
                log.info("엔티티가 존재하지 않습니다: {}", eid);
            }
        } catch (Exception e) {
            System.out.println(e);
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
    @Override
    @Transactional
    public long entUpdate(Long eid, String saveFolder, EntExamDTO dto) {
        String fnames = null;
        log.info("Starting update for eid: {}", eid);
        EntExam entExam = entExamRepository.findById(eid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid eid: " + eid));
        log.info("Fetched EntExam: {}", entExam);
        entExam.setTitle(dto.getTitle());
        entExam.setContent(dto.getContent());
        EntExam saveEntity = entExamRepository.save(entExam);
        log.info("Updated title and content for EntExam: {}", saveEntity);

        String imgPath = entExam.getImgPath();
        log.info("...이미지경로 가져오기:{}", imgPath);
        StringBuilder updateImgPaths = new StringBuilder();
        // 기존 이미지가 있는 경우
        if(entExam.getImgPath()!=null){
            // original imgList
            List<String> originPaths = Arrays.stream(entExam.getImgPath().split(";-;")).toList();
            // dto.imgPaths file delete
            if(dto.getFilePath()!=null && dto.getFilePath().size()>0){
                academyImgFileRemove(dto.getFilePath(),saveFolder);
            }
            // updateImgPaths 에 기존 이미지는 넣고 삭제할 이미지는 빼기
            updateImgPaths = updateImgList(updateImgPaths, originPaths, dto.getFilePath());
            log.info("updateImgPaths after del...{}",updateImgPaths);
        }
        // 새롭게 추가된 이미지 insert
        String addPaths = "";
        if(dto.getFiles()!=null && !dto.getFiles().get(0).getOriginalFilename().equals("")){
            addPaths = fileUpload(saveFolder,dto.getFiles());
        }
        updateImgPaths.append(addPaths);
        log.info("updateImgPaths after add...{}",updateImgPaths);


        if(updateImgPaths==null || updateImgPaths.length()==0)
            entExam.setImgPath(null);
        else
            entExam.setImgPath(updateImgPaths.toString());
//        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
//            fnames = fileUpload(saveFolder, dto.getFiles());
//            if (!fnames.isEmpty()) {
//                File file = new File(saveFolder, imgPath);
//                log.info("기존 이미지:{}", imgPath);
//                file.delete();
//                saveEntity.setImgPath(fnames);
//                log.info("Set imgPath for EntExam: {}", saveEntity.getImgPath());
//            }
//        }else if (imgPath == null) {
//            String addPaths = fileUpload(saveFolder, dto.getFiles());
//            if (!addPaths.isEmpty()) {
//                saveEntity.setImgPath(addPaths);
//                log.info("Set imgPath for EntExam: {}", saveEntity.getImgPath());
//            }
//        }

        entExamRepository.save(saveEntity);
        log.info("Final saved EntExam: {}", saveEntity);
        return saveEntity.getEid();
    }


    private String fileUpload(String saveFolder, List<MultipartFile> files) {
        List<File> saveFile = new ArrayList<>();
        StringBuilder imgPaths = new StringBuilder();
//        List<String> saveFileNames = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            String filename = uuid + "_" + fname;
            saveFile.add(new File(saveFolder, filename));
            imgPaths.append(filename);
//            saveFileNames.add(filename);
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                files.get(i).transferTo(saveFile.get(i));
            }
        } catch (IOException e) {
            System.out.println(e);
            for (int i = 0; i < files.size(); i++)
                saveFile.get(i).delete();
        }
        return imgPaths.toString();
    }


}
