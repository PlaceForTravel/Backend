package com.traveler.core.controller;

import com.traveler.core.dto.BoardListResponseDTO;
import com.traveler.core.dto.BoardPlaceListResponseDTO;
import com.traveler.core.dto.SavedBoardPlaceRequestDTO;
import com.traveler.core.dto.UserDTO;
import com.traveler.core.entity.User;
import com.traveler.core.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/savedBoard/list")
    public Page<BoardListResponseDTO> savedBoardList(@RequestParam String userId, @PageableDefault Pageable pageable){
        Page<BoardListResponseDTO> boardListResponseDTOs=  userService.savedBoardPaging(userId, pageable);
        return boardListResponseDTOs;
    }
    @RequestMapping(value = "/savedBoardPlace/list")
    public Page<BoardPlaceListResponseDTO> savedBoardPlaceList(@RequestParam String userId, @RequestParam String cityName, @PageableDefault Pageable pageable){
        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs=userService.savedBoardPlacePaging(userId,cityName, pageable);
        return boardPlaceListResponseDTOs;
    }
    @RequestMapping(value = "/savedBoardPlace/city")
    public List<String> savedBoardPlaceList(@RequestParam String userId){
        List<String> cities = userService.savedBoardPlaceCity(userId);
        return cities;
    }

    @RequestMapping(value = "/login")
    public void join(@RequestBody User user){
        userService.login(user);
    }

    @RequestMapping(value="/deleteFCMToken")
    public void join(@RequestBody UserDTO userDTO){
        userService.deleteToken(userDTO.getUserId());
    }

    @RequestMapping(value = "/nickname")
    public boolean checkNickname(@RequestParam String nickname){
        boolean notUnique = userService.isNicknameUnique(nickname);
        return notUnique;
    }

    @RequestMapping(value = "/id")
    public boolean checkId(@RequestParam String userId){
        boolean notUnique = userService.isIdUnique(userId);
        return notUnique;
    }
}
