package com.momsway.controller;

import com.momsway.dto.EntReplyDTO;
import com.momsway.service.EntReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntReplyController {
    private final EntReplyService entReplyService;

    @GetMapping("/replist/{eid}")
    public ResponseEntity<List<EntReplyDTO>>list(@PathVariable Long eid){
        List<EntReplyDTO> repList = entReplyService.repList(eid);
        return ResponseEntity.ok().body(repList);
    }
    @PostMapping("/insertrep")
    public List<EntReplyDTO> insertRep(@RequestBody EntReplyDTO dto, Model model){
        Long id = entReplyService.insertRep(dto);
        List<EntReplyDTO> subList = entReplyService.repList(dto.getEid());
        return subList;
    }
}
