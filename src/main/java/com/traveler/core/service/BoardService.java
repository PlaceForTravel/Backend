package com.traveler.core.service;

import com.traveler.core.dto.*;
import com.traveler.core.entity.*;
import com.traveler.core.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final PlaceRepository placeRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    private final SavedBoardRepository savedBoardRepository;
    private final SavedBoardPlaceRepository savedBoardPlaceRepository;

    private final S3Service s3Service;

    public BoardService(BoardRepository boardRepository, PlaceRepository placeRepository, BoardPlaceRepository boardPlaceRepository, ImageRepository imageRepository, UserRepository userRepository, UserServiceImpl userService, SavedBoardRepository savedBoardRepository, SavedBoardPlaceRepository savedBoardPlaceRepository, S3Service s3Service) {
        this.boardRepository = boardRepository;
        this.placeRepository = placeRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.savedBoardRepository = savedBoardRepository;
        this.savedBoardPlaceRepository = savedBoardPlaceRepository;

        this.s3Service = s3Service;
    }

    public Page<BoardListResponseDTO> placePaging(Pageable pageable){
        int page = pageable.getPageNumber()-1; // 현재페이지 반환
        int pagelimit = 5;

        Page<Board> boardPages = boardRepository.findAll(PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

        Page<BoardListResponseDTO> boardListResponseDTOs = boardPages.map(
                boardPage -> new BoardListResponseDTO(boardPage));

        return boardListResponseDTOs;
    }
    public Page<BoardListResponseDTO> placePaging(Pageable pageable,String cityName){
        int page = pageable.getPageNumber()-1; // 현재페이지 반환
        int pagelimit = 5;
        Page<Board> boardPages = boardRepository.findAllByCityName(cityName, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

        Page<BoardListResponseDTO> boardListResponseDTOs = boardPages.map(
                boardPage -> new BoardListResponseDTO(boardPage));

        return boardListResponseDTOs;
    }
    public Page<BoardPlaceListResponseDTO> boardPlacePaging(Pageable pageable, String placeName){
        int page = pageable.getPageNumber()-1; // 현재페이지 반환
        int pagelimit = 5;
        Page<BoardPlace> boardPlacePages = boardPlaceRepository.findBoardPlaceByPlacePlaceName(placeName, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs = boardPlacePages.map(
                boardPlacePage -> new BoardPlaceListResponseDTO(boardPlacePage));

        return boardPlaceListResponseDTOs;
    }

    public void save(BoardSaveDTO boardSaveDTO,List<MultipartFile> multipartFiles){
        User user = userRepository.findById(boardSaveDTO.getUserId()).orElse(null);
        Board board = boardSaveDTO.toBoardEntity(user);
        List<PlaceRequestDTO> places = boardSaveDTO.getPlaces();
        List<BoardPlace> boardPlaces = new ArrayList<>();
        boardRepository.save(board);

        places.forEach(place -> {
            Place placeEntity = place.toPlaceEntity();
            placeRepository.save(placeEntity);
            BoardPlace boardPlace = BoardPlace.builder().board(board).place(placeEntity).regDate(board.getRegDate()).build();
            boardPlaceRepository.save(boardPlace);
            boardPlaces.add(boardPlace);
            String fileName = board.getBoardId()+"_"+placeEntity.getPlaceId()+"_"+board.getRegDate().toLocalDate().toString();
            int imgIndex = place.getImgIndex();
            try {
                String imgUrl = s3Service.upload(multipartFiles.get(imgIndex),fileName);
                Image img = place.toImageEntity(imgUrl,boardPlace);
                imageRepository.save(img);
                boardPlace.setImage(img);
                boardPlaceRepository.save(boardPlace);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
    public void edit(int boardId, BoardEditDTO boardEditDTO){
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board.getUser().getUserId().equals( boardEditDTO.getUserId())){

            board.setContent(boardEditDTO.getContent());
            board.setModifiedDate(LocalDateTime.now());
            boardRepository.save(board);
        }else {

            //오류 날리기
        }

    }

    public void delete(int boardId){
        Board board = boardRepository.findById(boardId).orElse(null);
        board.setDeletedDate(LocalDateTime.now());
        boardRepository.save(board);
    }
    public void like(int boardId, String userId){
        Board board = boardRepository.findById(boardId).orElse(null);
        board.setLikeCount();
        boardRepository.save(board);

        User user = userRepository.findById(userId).orElse(null);
        SavedBoard savedBoard= new SavedBoard(board, user, LocalDateTime.now());

        savedBoardRepository.save(savedBoard);
        // 알림보내기 서비스 필요
    }

    public void likeBoardPlace(int boardPlaceId, String userId){
        BoardPlace boardPlace = boardPlaceRepository.findById(boardPlaceId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        SavedBoardPlace savedBoardPlace = new SavedBoardPlace(user,boardPlace, LocalDateTime.now() );

        savedBoardPlaceRepository.save(savedBoardPlace);
    }

}
