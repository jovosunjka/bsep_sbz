package com.bsep.SIEMCenter.controller;

import com.bsep.SIEMCenter.controller.dto.MessageDto;
import com.bsep.SIEMCenter.service.interfaces.IRestTemplateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/logs")
public class LogsController
{
    @Autowired
    public  LogsController() {

    }

    @PreAuthorize("hasAuthority('WRITE_LOG')")
    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public ResponseEntity processLogs(@RequestBody MessageDto messageDto) {
        System.out.println(messageDto.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
