package com.momsway.repository.user;

import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserQueryDSL {
    List<EntExamDTO> myentlist(long uidByEmail);

    int getCount(String search, String search_txt);
}
