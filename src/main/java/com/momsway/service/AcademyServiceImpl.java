package com.momsway.service;

import com.momsway.dto.AcademyDTO;
import com.momsway.repository.academy.AcademyRepository;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
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
    public int delAcademy(Long aid) {
        int result = 0;
        try{
            academyRepository.deleteById(aid);
            result = 1;
        }catch (Exception e){

        }
        return result;
    }

    @Override
    public Long insertAcademy(AcademyDTO dto, String saveFolder) {
//        Academy newAcademy = Academy.builder()
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .readNo(0L)
        return null;
    }
}
