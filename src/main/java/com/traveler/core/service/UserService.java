package com.traveler.core.service;

import com.traveler.core.dto.BoardListResponseDTO;
import com.traveler.core.dto.BoardPlaceListResponseDTO;
import com.traveler.core.entity.*;
import com.traveler.core.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SavedBoardRepository savedBoardRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final BoardRepository boardRepository;
    private final SavedBoardPlaceRepository savedBoardPlaceRepository;
    private final ImageRepository imageRepository;
    private final CommentService commentService;
    private final BoardService boardService;
    public UserService(UserRepository userRepository, BoardRepository boardRepository, SavedBoardRepository savedBoardRepository, BoardPlaceRepository boardPlaceRepository, BoardRepository boardRepository1, SavedBoardPlaceRepository savedBoardPlaceRepository, ImageRepository imageRepository, CommentService commentService, BoardService boardService) {
        this.userRepository = userRepository;
        this.savedBoardRepository = savedBoardRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.boardRepository = boardRepository;
        this.savedBoardPlaceRepository = savedBoardPlaceRepository;
        this.imageRepository = imageRepository;
        this.commentService = commentService;
        this.boardService = boardService;
    }

    public Page<BoardListResponseDTO> savedBoardPaging(String userId, Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageLimit = 5;
        User user = userRepository.findById(userId).orElse(null);
        Page<SavedBoard> savedBoardPages = savedBoardRepository.findAllByUser(user, PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"regDate")));
        Page<BoardListResponseDTO> boardListResponseDTOs = savedBoardPages.map(
                savedBoardPage -> {
                    Board board = savedBoardPage.getBoard();
                    return new BoardListResponseDTO(board, true);
                });

        return boardListResponseDTOs;
    }

    public Page<BoardPlaceListResponseDTO> savedBoardPlacePaging(String userId, String cityName,Pageable pageable){
        int page = pageable.getPageNumber()-1; // 현재페이지 반환
        int pagelimit = 5;
        User user = userRepository.findById(userId).orElse(null);
        Page<SavedBoardPlace> savedBoardPlacePages = savedBoardPlaceRepository.findAllByUserAndCityName(user,cityName, PageRequest.of(page, pagelimit, Sort.by(Sort.Direction.DESC,"regDate")));

        Page<BoardPlaceListResponseDTO> boardPlaceListResponseDTOs = savedBoardPlacePages.map(
                savedBoardPlacePage -> {
                    BoardPlace boardPlace = savedBoardPlacePage.getBoardPlace();
                    return new BoardPlaceListResponseDTO(boardPlace);
                });

        return boardPlaceListResponseDTOs;
    }
    public List<String> savedBoardPlaceCity(String userId){
        User user = userRepository.findById(userId).orElse(null);
        List<String> cities = savedBoardPlaceRepository.findCityName(user);

        return cities;

    }
    public List<Place> savedPlace(String userId){
        List<Place> places = new ArrayList<>();
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            List<SavedBoardPlace> savedPlaceList = savedBoardPlaceRepository.findAllByUser(user);
            for (SavedBoardPlace savedBoardPlace : savedPlaceList) {
                places.add(savedBoardPlace.getBoardPlace().getPlace());
            }
            // 중복 제거
            if (!places.isEmpty()) {
                places = places.stream().distinct().collect(Collectors.toList());
            }
        }
        return places;
    }
    public List<BoardPlaceListResponseDTO> savedBoardPlaceList(String userId, int placeId){
        List<BoardPlaceListResponseDTO> boardPlaceListResponseDTOS = new ArrayList<>();
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            List<SavedBoardPlace> savedPlaceList = savedBoardPlaceRepository.findAllByUser(user);
            for (SavedBoardPlace savedBoardPlace : savedPlaceList) {
                BoardPlace boardPlace = savedBoardPlace.getBoardPlace();
                if(placeId == boardPlace.getPlace().getPlaceId()){
                    boardPlaceListResponseDTOS.add(new BoardPlaceListResponseDTO(boardPlace));
                }
            }
        }
        return boardPlaceListResponseDTOS;
    }
    public void login(User user){
        userRepository.save(user);
    }
    public void deleteToken(String userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            user.deleteFCMToken();
            userRepository.save(user);
        }
    }
    public boolean isNicknameUnique(String nickname){
        boolean notUnique = userRepository.existsUserByNickname(nickname);
        return notUnique;
    }
    public String isIdUnique(String userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getNickname();
        }
        return null;
    }
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // 사용자가 존재하지 않을 때의 처리
            return;
        }
        List<Board> boards = boardRepository.findAllByUser(user);
        for (Board board : boards) {
            List<BoardPlace> boardPlaces = boardPlaceRepository.findAllByBoard(board);
            for (BoardPlace boardPlace : boardPlaces) {
                imageRepository.deleteAllByBoardPlace(boardPlace);
                boardPlaceRepository.delete(boardPlace);
            }
            commentService.deleteAllCommentByBoard(board);
        } // map이 아닌 forEach를 사용해야함
        commentService.deleteAllCommentByUser(user);
        boardService.deleteAllBoard(user);
        savedBoardPlaceRepository.deleteAllByUser(user);
        savedBoardRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

}
