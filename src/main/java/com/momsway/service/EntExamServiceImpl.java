package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntExamServiceImpl implements EntExamService {

    @Value("${spring.servlet.multipart.location}")
    private String saveFolder;

    @Value("${spring.servlet.multipart.location}")
    private String filePath;
    private final EntExamRepository entExamRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(EntExamService.class);

    @Override
    public Page<EntExamDTO> entlist(Pageable pageable, String search_txt) {
        Page<EntExamDTO> orderlist = entExamRepository.orderlist(pageable, search_txt);
        return orderlist;
    }

    @Override
    public long upload(String saveFolder, EntExamDTO dto,String username) {
        User user = userRepository.findByEmail(username);
        EntExam entity = EntExam.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .imgPath(dto.getImgPath())
                .entExamUser(user)
                .readNo(0L)
                .build();
        EntExam savedEntity = entExamRepository.save(entity);
        log.info("service..uid...{}",dto.getUid());
        List<String> fnames = new ArrayList<>();
        if (dto.getFiles() != null && dto.getFiles().size() != 0) {
            fnames = fileUpload(saveFolder, dto.getFiles());
            if (!fnames.isEmpty()) {
                savedEntity.setImgPath(fnames.get(0));
            }
            for (String item : fnames)
                log.info("....items..{}", item);
        }
        entExamRepository.save(savedEntity);

        return fnames.size();
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
                .readNo(detail.getReadNo()+1)
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
                    File file = new File(saveFolder,imgPath);
                    log.info("절대 경로: {}",file.getAbsolutePath());
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

    @Override
    @Transactional
    public long entUpdate(Long eid, String saveFolder, EntExamDTO dto) {
        log.info("Starting update for eid: {}", eid);
        EntExam entExam = entExamRepository.findById(eid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid eid: " + eid));
        log.info("Fetched EntExam: {}", entExam);
        entExam.setTitle(dto.getTitle());
        entExam.setContent(dto.getContent());
        EntExam saveEntity = entExamRepository.save(entExam);
        log.info("Updated title and content for EntExam: {}", saveEntity);
        String imgPath = entExam.getImgPath();
        log.info("...이미지경로 가져오기:{}",imgPath);
        List<String> fnames = new ArrayList<>();
        if(dto.getFiles()!=null && !dto.getFiles().isEmpty()){

            fnames = fileUpload(saveFolder, dto.getFiles());
            if (!fnames.isEmpty()) {
                File file = new File(saveFolder, imgPath);
                log.info("기존 이미지:{}",imgPath);
                file.delete();
                saveEntity.setImgPath(fnames.get(0));
                log.info("Set imgPath for EntExam: {}", saveEntity.getImgPath());
            }
            for (String item : fnames)
                log.info("....items..{}", item);


        }

        entExamRepository.save(saveEntity);
        log.info("Final saved EntExam: {}", saveEntity);
        return saveEntity.getEid();
    }


    private List<String> fileUpload(String saveFolder, List<MultipartFile> files) {
        List<File> saveFile = new ArrayList<>();
        List<String> saveFileNames = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            UUID uuid = UUID.randomUUID();
            String fname = files.get(i).getOriginalFilename();
            URLEncoder.encode(fname, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            String filename = uuid + "_" + fname;
            saveFile.add(new File(saveFolder, filename));
            saveFileNames.add(filename);
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
        return saveFileNames;
    }


}
