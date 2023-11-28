package com.traveler.core.controller;


import com.traveler.core.dto.*;

import com.traveler.core.entity.Board;
import com.traveler.core.entity.BoardPlace;
import com.traveler.core.repository.BoardPlaceRepository;
import com.traveler.core.repository.BoardRepository;
import com.traveler.core.service.BoardService;
import com.traveler.core.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardRepository boardRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final BoardService boardService;
    private final UserServiceImpl userService;

    public BoardController(BoardRepository boardRepository, BoardPlaceRepository boardPlaceRepository, BoardService boardService, UserServiceImpl userService) {
        this.boardRepository = boardRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.boardService = boardService;
        this.userService = userService;
    }

    @GetMapping(value = "/list")
    public Page<BoardListResponseDTO> boardList(@PageableDefault(page = 1) Pageable pageable){
//        List<Board> optionalBoardList= Optional.ofNullable(boardRepository.findAll()).orElse(Collections.emptyList());
//        List<BoardListResponseDTO> boardListResponseDTOList = optionalBoardList.stream().map(emp-> new BoardListResponseDTO(emp)).collect(Collectors.toList());

            Page<BoardListResponseDTO> boardListResponseDTOs = boardService.placePaging(pageable);
        return boardListResponseDTOs;
    }
    @GetMapping(value = "/search/city/{cityName}")
    public Page<BoardListResponseDTO> searchCityList(@PageableDefault(page = 1) Pageable pageable, @PathVariable String cityName){
        Page<BoardListResponseDTO> boardListResponseDTOs = boardService.placePaging(pageable,cityName);
        return boardListResponseDTOs;
    }
    @GetMapping(value = "/search/place/{placeName}")
    public Page<BoardPlaceListResponseDTO> searchPlaceList(@PageableDefault(page = 1) Pageable pageable, @PathVariable String placeName){
        Page<BoardPlaceListResponseDTO> placeListResponseDTOs = boardService.boardPlacePaging(pageable,placeName);
        return placeListResponseDTOs;
    }
    @GetMapping(value = "/detail/{boardId}")
    public BoardDetailResponseDTO boardDetail(@PathVariable int boardId){
        Board board = boardRepository.findById(boardId).orElse(null);
        List<BoardPlace> boardPlace = boardPlaceRepository.findBoardPlacesByBoardBoardId(boardId);
        if(board !=null && boardPlace != null){
            BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board, boardPlace);
        return boardDetailResponseDTO;} else return null;

    }
    @PostMapping(value = "/save")
    public void saveBoard(@RequestPart("board") BoardSaveDTO boardSaveDTO, @RequestPart("images")List<MultipartFile> multipartFiles){
        boardService.save(boardSaveDTO, multipartFiles);
    }
    @DeleteMapping(value = "/delete/{boardId}")
    public void deleteBoard(@PathVariable int boardId){
        boardService.delete(boardId);
    }
    @PostMapping(value = "/like/{boardId}")
    public void like(@PathVariable int boardId, @RequestBody UserDTO userDTO){
        boardService.like(boardId, userDTO.getUserId());
    }
    @PostMapping(value = "/saveBoardPlace/{boardPlaceId}")
    public void likeBoardPlace(@PathVariable int boardPlaceId, @RequestBody UserDTO userDTO){
        boardService.likeBoardPlace(boardPlaceId, userDTO.getUserId());
    }
    @PutMapping(value = "/edit/{boardId}")
    public void editBoard(@PathVariable int boardId, @RequestBody BoardEditDTO boardEditDTO){
        boardService.edit(boardId, boardEditDTO);
    }


}
