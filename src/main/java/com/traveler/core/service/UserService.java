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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SavedBoardRepository savedBoardRepository;
    private final BoardPlaceRepository boardPlaceRepository;
    private final BoardRepository boardRepository;
    private final SavedBoardPlaceRepository savedBoardPlaceRepository;

    public UserService(UserRepository userRepository, BoardRepository boardRepository, SavedBoardRepository savedBoardRepository, BoardPlaceRepository boardPlaceRepository, BoardRepository boardRepository1, SavedBoardPlaceRepository savedBoardPlaceRepository) {
        this.userRepository = userRepository;
        this.savedBoardRepository = savedBoardRepository;
        this.boardPlaceRepository = boardPlaceRepository;
        this.boardRepository = boardRepository;
        this.savedBoardPlaceRepository = savedBoardPlaceRepository;
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
    public boolean isIdUnique(String userId){
        boolean notUnique = userRepository.existsUserByUserId(userId);
        return notUnique;
    }

}
