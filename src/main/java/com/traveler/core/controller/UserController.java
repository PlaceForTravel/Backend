package com.traveler.core.controller;

import com.traveler.core.dto.BoardListResponseDTO;
import com.traveler.core.dto.BoardPlaceListResponseDTO;
import com.traveler.core.entity.User;
import com.traveler.core.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/savedBoard/list")
    public Page<BoardListResponseDTO> savedBoardList(@RequestBody User user, @PageableDefault Pageable pageable){
        Page<BoardListResponseDTO> boardListResponseDTOs=userService.savedBoardPaging(user.getUserId(), pageable);
        return boardListResponseDTOs;
    }
    @RequestMapping(value = "/savedBoardPlace/list")
    public Page<BoardPlaceListResponseDTO> savedBoardPlaceList(@RequestBody User user, @PageableDefault Pageable pageable){
        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs=userService.savedBoardPlacePaging(user.getUserId(), pageable);
        return boardPlaceListResponseDTOs;
    }
}
